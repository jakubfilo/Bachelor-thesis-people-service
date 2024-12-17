package com.jakubfilo.peopleservice.rest.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class DuplicateStudentException extends ServiceException {

	private static final int DUPLICATE_STUDENT_STATUS_CODE = 409;
	private static final String ERROR = "DUPLICATE_STUDENT_EXCEPTION";

	private final String studentId;

	@Override
	public int getStatusCode() {
		return DUPLICATE_STUDENT_STATUS_CODE;
	}

	@Override
	public String getMessage() {
		return "Student with id " + studentId + " already exists";
	}

	@Override
	public String getError() {
		return ERROR;
	}
}
