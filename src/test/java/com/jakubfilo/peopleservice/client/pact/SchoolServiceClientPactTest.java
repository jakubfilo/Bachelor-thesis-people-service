package com.jakubfilo.peopleservice.client.pact;

import static au.com.dius.pact.consumer.dsl.LambdaDsl.newJsonArrayUnordered;
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


	@Inject
	private SchoolServiceClient schoolServiceClient;

	@Pact(consumer = PEOPLE_SERVICE_COMPONENT_NAME, provider = SCHOOL_SERVICE_COMPONENT_NAME)
	V4Pact getCoursesDetailBatchLookupPact(PactDslWithProvider builder) {
		return builder
			.given("Courses with details exist")
			.uponReceiving("A request for course details batch")
			.method("GET")
			.path("/school/course/lookup/batch")
			.matchQuery("courseIds", "[a-zA-Z0-9,]+", "COURSE1,COURSE2")
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
		assertThat(response.getCourses()).hasSize(1);
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
				course.stringMatcher("departmentType", "COMPUTER_SCIENCE|MATHEMATICS|MECHANICAL_ENGINEERING|SCIENCE");
				course.numberType("credits", 5);
				course.stringType("teacherId", "TEACHER1");
				course.stringMatcher("term", "WINTER|SUMMER", "WINTER");
				course.numberType("startYear", 2024);
				course.unorderedArray("enrolledStudentsIds", student -> student.stringType("STUDENT1"));
				course.numberType("enrolledStudentsCount", 2);
				course.object("courseTime", ct -> {
					ct.stringType("daysOfWeek", "MONDAY");
					ct.numberType("startHour", 8);
					ct.numberType("startMinute", 30);
					ct.numberType("endHour", 10);
					ct.numberType("endMinute", 0);
				});
				course.stringType("roomId", "ROOM101");
			});
		}).build();
	}
}
