package com.jakubfilo.peopleservice.rest.exception;

import java.util.Set;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class InvalidCourseIdsException extends ServiceException {

	private static final String ERROR = "INVALID_COURSE_IDS";
	private static final int STATUS_CODE = 404;

	private final String studentId;
	private final Set<String> courseIds;

	@Override
	public int getStatusCode() {
		return STATUS_CODE;
	}

	@Override
	public String getMessage() {
		return "Student " + studentId + " is enrolled in some invalid courses. Students courses: " + courseIds;
	}
	// I don't have a way to tell which courses are invalid as this microserv. is not connected to courses DB.-
	// I would have to add invalid courses field into the response from people service, filling the responses with garbage
	// or I would have to wrap every response in a special wrapper that would include error field filled with potential errors in that specific request

	@Override
	public String getError() {
		return ERROR;
	}
}
