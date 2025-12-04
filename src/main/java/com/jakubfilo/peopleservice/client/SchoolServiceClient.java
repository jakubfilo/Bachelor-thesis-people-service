package com.jakubfilo.peopleservice.client;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.jakubfilo.peopleservice.client.api.CourseControllerApi;
import com.jakubfilo.peopleservice.client.api.ExternalCourseControllerApi;
import com.jakubfilo.peopleservice.client.api.MultipleCourseDetail;
import com.jakubfilo.peopleservice.client.model.CourseTimetableDetail;
import com.jakubfilo.peopleservice.client.model.EnrollStudentInCoursesResponse;
import com.jakubfilo.peopleservice.rest.exception.InvalidCourseIdsException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class SchoolServiceClient {

	private final ExternalCourseControllerApi externalCourseControllerApi;
	private final CourseControllerApi courseControllerApi;

	public MultipleCourseDetail getCoursesDetailBatchLookup(Set<String> courseIds) {
		var apiResponse = externalCourseControllerApi.getCourseDetailsBatchWithHttpInfo(courseIds);

		return MultipleCourseDetail.builder()
				.courses(apiResponse.getBody())
				.courseInfoComplete(apiResponse.getStatusCode().value() != SchoolServiceResponseCodes.BATCH_LOOKUP_INCOMPLETE_COURSE_INFO.getStatusCode()
						&& !apiResponse.getStatusCode().isError())
				.build();
	}

	public Set<CourseTimetableDetail> getCourseTimetableBatchLookup(Set<String> courseIds, String studentId) {
		var response = courseControllerApi.getCourseTimesWithHttpInfo(courseIds);

		if (response.getStatusCode().value() == SchoolServiceResponseCodes.SINGLE_COURSE_LOOKUP_INVALID_COURSE_ID.getStatusCode()) {
			throw new InvalidCourseIdsException(studentId, courseIds);
		}
		return response.getBody();
	}

	public EnrollStudentInCoursesResponse enrollStudentToCourses(String studentId, Set<String> courseIds) {
		return externalCourseControllerApi.enrollStudentInCourses(studentId, courseIds);
	}

	@RequiredArgsConstructor
	@Getter
	// Status codes of School service, as mentioned in its API spec
	public enum SchoolServiceResponseCodes {
		BATCH_LOOKUP_INCOMPLETE_COURSE_INFO(206),
		BATCH_LOOKUP_NO_COURSES_FOUND(404),
		SINGLE_COURSE_LOOKUP_INVALID_COURSE_ID(404);

		private final int statusCode;
	}
}
