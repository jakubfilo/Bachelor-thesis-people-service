package com.jakubfilo.peopleservice.client;

import static com.jakubfilo.peopleservice.client.SchoolServiceClient.CLIENT_NAME;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jakubfilo.peopleservice.client.api.CourseDetailRepresentationApi;
import com.jakubfilo.peopleservice.client.api.EnrollStudentInCoursesResponse;
import com.jakubfilo.peopleservice.client.api.MultipleCourseDetail;
import com.jakubfilo.peopleservice.config.FeignClientConfiguration.SchoolServiceClientConfiguration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@FeignClient(
		name = CLIENT_NAME,
		url = "${clientsConfiguration.schoolService.host}",
		configuration = SchoolServiceClientConfiguration.class,
		fallbackFactory = SchoolServiceClient.Fallback.class)
public interface SchoolServiceClient {

	String CLIENT_NAME = "school-service-client";

	Logger LOGGER = LoggerFactory.getLogger(SchoolServiceClient.class);

	String COURSE_BATCH_LOOKUP_RESOURCE = "/course/lookup/batch";
	String COURSE_ENROLL_STUDENT_RESOURCE = "/course/enroll";

	default MultipleCourseDetail getCourseDetailsBatchLookup(Set<String> courseIds) {
		var response = getCourseDetailsBatchLookupApi(courseIds);

		var courseDetails = MultipleCourseDetail.builder()
				.courses(response.getBody())
				.build();

		if (response.getStatusCode().value() == SchoolServiceResponseCodes.INCOMPLETE_COURSE_INFO.getStatusCode()) {
			courseDetails.toBuilder()
					.courseInfoComplete(false)
					.build();
		}

		return courseDetails;
	}

	@GetMapping(COURSE_BATCH_LOOKUP_RESOURCE)
	ResponseEntity<Set<CourseDetailRepresentationApi>> getCourseDetailsBatchLookupApi(@RequestParam("courseIds") Set<String> courseIds);

	@PostMapping(COURSE_ENROLL_STUDENT_RESOURCE)
	EnrollStudentInCoursesResponse enrollStudentToCourses(@RequestParam("courseIds") Set<String> courseIds,
			@RequestParam("studentId") String studentId);

	class Fallback implements FallbackFactory<SchoolServiceClient> {

		@Override
		public SchoolServiceClient create(Throwable cause) {
			return new SchoolServiceClient() {
				@Override
				public ResponseEntity<Set<CourseDetailRepresentationApi>> getCourseDetailsBatchLookupApi(Set<String> courseIds) {
					LOGGER.warn("Exception during getCourseDetailsBatchLookup('{}'), returning fallback", courseIds, cause);
					return ResponseEntity
							.status(SchoolServiceResponseCodes.INCOMPLETE_COURSE_INFO.getStatusCode())
							.body(Set.of());
				}

				@Override
				public EnrollStudentInCoursesResponse enrollStudentToCourses(Set<String> courseIds, String studentId) {
					LOGGER.warn("Exception during enrollStudentToCourses('{}', '{}'), returning fallback", courseIds, studentId, cause);
					return EnrollStudentInCoursesResponse.builder()
							.studentId(studentId)
							.invalidCourses(courseIds)
							.build();
				}
			};
		}
	}

	@RequiredArgsConstructor
	@Getter
	// Status codes of School service, as mentioned in its API spec
	enum SchoolServiceResponseCodes {
		INCOMPLETE_COURSE_INFO(206);

		private final int statusCode;
	}
}

