package com.jakubfilo.peopleservice.client.pact;

import static au.com.dius.pact.consumer.dsl.LambdaDsl.newJsonArrayUnordered;
import static au.com.dius.pact.consumer.dsl.LambdaDsl.newJsonBody;
import static com.jakubfilo.peopleservice.client.SchoolServiceClient.SchoolServiceResponseCodes.BATCH_LOOKUP_NO_COURSES_FOUND;
import static com.jakubfilo.peopleservice.client.pact.PactConstants.PEOPLE_SERVICE_COMPONENT_NAME;
import static com.jakubfilo.peopleservice.client.pact.PactConstants.SCHOOL_SERVICE_COMPONENT_NAME;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import com.jakubfilo.peopleservice.client.SchoolServiceClient;
import com.jakubfilo.peopleservice.client.model.CourseTimetableDetail;
import com.jakubfilo.peopleservice.client.model.DayOfWeek;
import com.jakubfilo.peopleservice.config.ClientConfig;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit.MockServerConfig;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import au.com.dius.pact.provider.junitsupport.Consumer;
import jakarta.inject.Inject;

@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.NONE,
		classes = {
				ClientConfig.class, SchoolServiceClient.class
		}
)
@Consumer(PEOPLE_SERVICE_COMPONENT_NAME)
@MockServerConfig(port = "8000")
@ExtendWith({ MockitoExtension.class, PactConsumerTestExt.class })
class SchoolServiceClientPactTest {

	private static final String WORK_DAYS_REGEX = "^(MONDAY|TUESDAY|WEDNESDAY|THURSDAY|FRIDAY)$";
	private static final String HOURS_24_FORMAT_REGEX = "^(?:[0-9]|1[0-9]|2[0-3])$";
	private static final String MINUTES_FORMAT_REGEX = "^(?:[0-9]|[1-5][0-9])$";

	@Inject
	private SchoolServiceClient schoolServiceClient;

	@Pact(consumer = PEOPLE_SERVICE_COMPONENT_NAME, provider = SCHOOL_SERVICE_COMPONENT_NAME)
	V4Pact getCoursesDetailBatchLookupPact(PactDslWithProvider builder) {
		return builder
			.given("Courses with details exist")
			.uponReceiving("A request for course details batch")
			.method("GET")
			.path("/school/course/lookup/batch")
			.matchQuery("courseIds", "[A-z0-9]+(,[A-z0-9]+)*", "COURSE1,COURSE2")
			.willRespondWith()
			.status(HttpStatus.OK.value())
			.body(courseDetailsResponseBody())
			.toPact(V4Pact.class);
	}

	@Test
	@PactTestFor(pactMethod = "getCoursesDetailBatchLookupPact", pactVersion = PactSpecVersion.V4)
	void getCoursesDetailBatchLookupTest() {
		var response = schoolServiceClient.getCoursesDetailBatchLookup(Set.of("COURSE1", "COURSE2"));

		assertThat(response).isNotNull();
		assertThat(response.getCourses()).hasSize(2);
		assertThat(response.isCourseInfoComplete()).isTrue();
	}

	private static DslPart courseDetailsResponseBody() {
		return newJsonArrayUnordered(array -> {
			array.object(course -> {
				course.stringType("id", "COURSE1");
				course.stringType("courseName", "Mathematics");
				course.stringType("courseCode", "MATH101");
				course.stringType("description", "An introductory course to Mathematics.");
				course.stringType("departmentId", "DEPT1");
				course.stringMatcher("departmentType", "COMPUTER_SCIENCE|MATHEMATICS|MECHANICAL_ENGINEERING|SCIENCE", "MATHEMATICS");
				course.integerType("credits", 5);
				course.stringType("teacherId", "TEACHER1");
				course.stringMatcher("term", "WINTER|SUMMER", "WINTER");
				course.integerType("startYear", 2024);
				course.unorderedArray("enrolledStudentsIds", student -> student.stringType("STUDENT1"));
				course.integerType("enrolledStudentsCount", 1);
				course.object("courseTime", ct -> {
					ct.stringMatcher("dayOfWeek", WORK_DAYS_REGEX, "MONDAY");
					ct.integerMatching("startHour", HOURS_24_FORMAT_REGEX, 8);
					ct.integerMatching("startMinute", MINUTES_FORMAT_REGEX,30);
					ct.integerMatching("endHour", HOURS_24_FORMAT_REGEX, 10);
					ct.integerMatching("endMinute", MINUTES_FORMAT_REGEX, 0);
				});
				course.stringType("roomId", "ROOM101");
			});
			array.object(course -> {
				course.stringType("id", "COURSE2");
				course.stringType("courseName", "Engineering");
				course.stringType("courseCode", "ENG101");
				course.stringType("description", "An introductory course to Engineering.");
				course.stringType("departmentId", "DEPT2");
				course.stringMatcher("departmentType", "COMPUTER_SCIENCE|MATHEMATICS|MECHANICAL_ENGINEERING|SCIENCE", "MECHANICAL_ENGINEERING");
				course.integerType("credits", 10);
				course.stringType("teacherId", "TEACHER42");
				course.stringMatcher("term", "WINTER|SUMMER", "SUMMER");
				course.integerType("startYear", 2024);
				course.unorderedArray("enrolledStudentsIds", student -> student.stringType("STUDENT31"));
				course.integerType("enrolledStudentsCount", 1);
				course.object("courseTime", ct -> {
					ct.stringType("dayOfWeek", "MONDAY");
					ct.integerMatching("startHour", HOURS_24_FORMAT_REGEX, 8);
					ct.integerMatching("startMinute", MINUTES_FORMAT_REGEX,30);
					ct.integerMatching("endHour", HOURS_24_FORMAT_REGEX, 10);
					ct.integerMatching("endMinute", MINUTES_FORMAT_REGEX, 0);
				});
				course.stringType("roomId", "ROOM101");
			});
		}).build();
	}

	@Pact(consumer = PEOPLE_SERVICE_COMPONENT_NAME, provider = SCHOOL_SERVICE_COMPONENT_NAME)
	V4Pact getCoursesDetailBatchLookupEmptyPact(PactDslWithProvider builder) {
		return builder
			.given("Courses with details do not exist")
			.uponReceiving("A request for course details batch")
			.method("GET")
			.path("/school/course/lookup/batch")
			.matchQuery("courseIds", "[A-z0-9]+(,[A-z0-9]+)*", "COURSE1,COURSE2")
			.willRespondWith()
			.status(BATCH_LOOKUP_NO_COURSES_FOUND.getStatusCode())
			.toPact(V4Pact.class);
	}

	@Test
	@PactTestFor(pactMethod = "getCoursesDetailBatchLookupEmptyPact", pactVersion = PactSpecVersion.V4)
	void getCoursesDetailBatchLookupEmptyTest() {
		var response = schoolServiceClient.getCoursesDetailBatchLookup(Set.of("COURSE1", "COURSE2"));

		assertThat(response).isNotNull();
		assertThat(response.getCourses()).isEmpty();
	}

	@Pact(consumer = PEOPLE_SERVICE_COMPONENT_NAME, provider = SCHOOL_SERVICE_COMPONENT_NAME)
	V4Pact getCoursesDetailBatchLookupIncompletePact(PactDslWithProvider builder) {
		return builder
				.given("Not all courses with details exist")
				.uponReceiving("A request for course details batch")
				.method("GET")
				.path("/school/course/lookup/batch")
				.matchQuery("courseIds", "[A-z0-9]+(,[A-z0-9]+)*", "COURSE1,COURSE2")
				.willRespondWith()
				.status(SchoolServiceClient.SchoolServiceResponseCodes.BATCH_LOOKUP_INCOMPLETE_COURSE_INFO.getStatusCode())
				.body(courseDetailsResponseBodyIncomplete())
				.toPact(V4Pact.class);
	}

	@Test
	@PactTestFor(pactMethod = "getCoursesDetailBatchLookupIncompletePact", pactVersion = PactSpecVersion.V4)
	void getCoursesDetailBatchLookupIncompleteTest() {
		var response = schoolServiceClient.getCoursesDetailBatchLookup(Set.of("COURSE1", "COURSE2"));

		assertThat(response).isNotNull();
		assertThat(response.getCourses()).hasSize(1);
		assertThat(response.isCourseInfoComplete()).isFalse();
	}

	private static DslPart courseDetailsResponseBodyIncomplete() {
		return newJsonArrayUnordered(array -> {
			array.object(course -> {
				course.stringType("id", "COURSE1");
				course.stringType("courseName", "Mathematics");
				course.stringType("courseCode", "MATH101");
				course.stringType("description", "An introductory course to Mathematics.");
				course.stringType("departmentId", "DEPT1");
				course.stringMatcher("departmentType", "COMPUTER_SCIENCE|MATHEMATICS|MECHANICAL_ENGINEERING|SCIENCE", "MATHEMATICS");
				course.integerType("credits", 5);
				course.stringType("teacherId", "TEACHER1");
				course.stringMatcher("term", "WINTER|SUMMER", "WINTER");
				course.integerType("startYear", 2024);
				course.unorderedArray("enrolledStudentsIds", student -> student.stringType("STUDENT1"));
				course.integerType("enrolledStudentsCount", 1);
				course.object("courseTime", ct -> {
					ct.stringMatcher("dayOfWeek", WORK_DAYS_REGEX, "MONDAY");
					ct.integerMatching("startHour", HOURS_24_FORMAT_REGEX, 8);
					ct.integerMatching("startMinute", MINUTES_FORMAT_REGEX,30);
					ct.integerMatching("endHour", HOURS_24_FORMAT_REGEX, 10);
					ct.integerMatching("endMinute", MINUTES_FORMAT_REGEX, 0);
				});
				course.stringType("roomId", "ROOM101");
			});
		}).build();
	}

	@Pact(consumer = PEOPLE_SERVICE_COMPONENT_NAME, provider = SCHOOL_SERVICE_COMPONENT_NAME)
	V4Pact getCourseTimetableBatchLookupPact(PactDslWithProvider builder) {
		return builder
				.given("Course timetables exist")
				.uponReceiving("A request for course timetable batch lookup")
				.method("GET")
				.path("/school/course/time/batch")
				.matchQuery("courseIds", "[A-z0-9]+(,[A-z0-9]+)*", "COURSE1")
				.willRespondWith()
				.status(HttpStatus.OK.value())
				.body(courseTimetableDetailsResponseBody())
				.toPact(V4Pact.class);
	}

	@Test
	@PactTestFor(pactMethod = "getCourseTimetableBatchLookupPact", pactVersion = PactSpecVersion.V4)
	void getCourseTimetableBatchLookupTest() {
		var result = schoolServiceClient.getCourseTimetableBatchLookup(Set.of("COURSE1"), "STUDENT1");

//		var expectedCourseTimetable = CourseTimetableDetail.builder()
//				.courseId("COURSE1")
//				.courseTime(CourseTime.builder()
//						.dayOfWeek(DayOfWeek.MONDAY)
//						.startHour(8)
//						.startMinute(30)
//						.endHour(10)
//						.endMinute(0)
//						.build())
//				.roomId("ROOM101")
//				.build();

		assertThat(result).isNotNull();
		assertThat(result).hasSize(1);
		assertThat(result.iterator().next())
				.returns("COURSE1", CourseTimetableDetail::getCourseId)
				.returns("ROOM101", CourseTimetableDetail::getRoomId)
				.returns(DayOfWeek.MONDAY, c -> c.getCourseTime().getDayOfWeek())
				.returns(8, c -> c.getCourseTime().getStartHour())
				.returns(30, c -> c.getCourseTime().getStartMinute())
				.returns(10, c -> c.getCourseTime().getEndHour())
				.returns(0, c -> c.getCourseTime().getEndMinute());
	}

	private static DslPart courseTimetableDetailsResponseBody() {
		return newJsonArrayUnordered(courseTimetableDetails -> {
			courseTimetableDetails.object(courseTimetable -> {
				courseTimetable.stringType("courseId", "COURSE1");
				courseTimetable.object("courseTime", ct -> {
					ct.stringMatcher("dayOfWeek", WORK_DAYS_REGEX, "MONDAY");
					ct.integerMatching("startHour", HOURS_24_FORMAT_REGEX, 8);
					ct.integerMatching("startMinute", MINUTES_FORMAT_REGEX,30);
					ct.integerMatching("endHour", HOURS_24_FORMAT_REGEX, 10);
					ct.integerMatching("endMinute", MINUTES_FORMAT_REGEX, 0);
				});
				courseTimetable.stringType("roomId", "ROOM101");
			});
		}).build();
	}

	@Pact(consumer = PEOPLE_SERVICE_COMPONENT_NAME, provider = SCHOOL_SERVICE_COMPONENT_NAME)
	V4Pact enrollStudentToCoursesPact(PactDslWithProvider builder) {
		return builder
				.given("All selected courses exist")
				.uponReceiving("A request to enroll student in courses")
				.method("POST")
				.path("/school/course/enroll")
				.matchQuery("studentId", "[A-z0-9]+", "STUDENT1")
				.matchQuery("courseIds", "[A-z0-9]+(,[A-z0-9]+)*", "COURSE1,COURSE2")
				.willRespondWith()
				.status(HttpStatus.CREATED.value())
				.body(newJsonBody(enrollResponse -> {
					enrollResponse.stringType("studentId", "STUDENT1");
					enrollResponse.unorderedArray("enrolledCourses", course -> {
					    course.stringType("COURSE1");
					    course.stringType("COURSE2");
					});
				}).build())
				.toPact(V4Pact.class);
	}

	@Test
	@PactTestFor(pactMethod = "enrollStudentToCoursesPact", pactVersion = PactSpecVersion.V4)
	void enrollStudentToCoursesTest() {
		var response = schoolServiceClient.enrollStudentToCourses("STUDENT1", Set.of("COURSE1", "COURSE2"));

		assertThat(response).isNotNull();
		assertThat(response.getStudentId()).isEqualTo("STUDENT1");
		assertThat(response.getEnrolledCourses()).containsExactlyInAnyOrder("COURSE1", "COURSE2");
	}

	@Pact(consumer = PEOPLE_SERVICE_COMPONENT_NAME, provider = SCHOOL_SERVICE_COMPONENT_NAME)
	V4Pact enrollStudentToCoursesPartiallyPact(PactDslWithProvider builder) {
		return builder
				.given("Some of selected courses do not exist")
				.uponReceiving("A request to enroll student in courses")
				.method("POST")
				.path("/school/course/enroll")
				.matchQuery("studentId", "[A-z0-9]+", "STUDENT1")
				.matchQuery("courseIds", "[A-z0-9]+(,[A-z0-9]+)*", "COURSE1,COURSE2")
				.willRespondWith()
				.status(HttpStatus.PARTIAL_CONTENT.value())
				.body(newJsonBody(enrollResponse -> {
					enrollResponse.stringType("studentId", "STUDENT1");
					enrollResponse.unorderedArray("enrolledCourses", course -> {
						course.stringType("COURSE1");
					});
				}).build())
				.toPact(V4Pact.class);
	}

	@Test
	@PactTestFor(pactMethod = "enrollStudentToCoursesPartiallyPact", pactVersion = PactSpecVersion.V4)
	void enrollStudentToCoursesPartiallyTest() {
		var response = schoolServiceClient.enrollStudentToCourses("STUDENT1", Set.of("COURSE1", "COURSE2"));

		assertThat(response).isNotNull();
		assertThat(response.getStudentId()).isEqualTo("STUDENT1");
		assertThat(response.getEnrolledCourses()).containsExactlyInAnyOrder("COURSE1");
	}

	@Pact(consumer = PEOPLE_SERVICE_COMPONENT_NAME, provider = SCHOOL_SERVICE_COMPONENT_NAME)
	V4Pact enrollStudentToNonExistentCoursesPact(PactDslWithProvider builder) {
		return builder
				.given("None of selected courses exists")
				.uponReceiving("A request to enroll student in courses")
				.method("POST")
				.path("/school/course/enroll")
				.matchQuery("studentId", "[A-z0-9]+", "STUDENT1")
				.matchQuery("courseIds", "[A-z0-9]+(,[A-z0-9]+)*", "COURSE1,COURSE2")
				.willRespondWith()
				.status(HttpStatus.NOT_FOUND.value())
				.toPact(V4Pact.class);
	}

	@Test
	@PactTestFor(pactMethod = "enrollStudentToNonExistentCoursesPact", pactVersion = PactSpecVersion.V4)
	void enrollStudentToNonExistentCoursesTest() {
		var response = schoolServiceClient.enrollStudentToCourses("STUDENT1", Set.of("COURSE1", "COURSE2"));

		assertThat(response).isNotNull();
		assertThat(response.getStudentId()).isEqualTo("STUDENT1");
		assertThat(response.getEnrolledCourses()).isEmpty();
	}
}
