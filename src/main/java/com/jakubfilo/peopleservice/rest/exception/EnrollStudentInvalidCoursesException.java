package com.jakubfilo.peopleservice.rest.exception;

import java.util.Set;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class EnrollStudentInvalidCoursesException extends ServiceException {

	private static final int INVALID_COURSES_STATUS_CODE = 404;
	private static final String ERROR = "ENROLL_STUDENT_INVALID_COURSES_EXCEPTION";

	private final Set<String> invalidCourseIds;

	@Override
	public int getStatusCode() {
		return INVALID_COURSES_STATUS_CODE;
	}

	@Override
	public String getMessage() {
		return String.format("Courses with ids %s do not exist", invalidCourseIds);
	}

	@Override
	public String getError() {
		return ERROR;
	}
}
