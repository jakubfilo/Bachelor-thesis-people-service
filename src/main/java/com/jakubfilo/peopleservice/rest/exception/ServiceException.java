package com.jakubfilo.peopleservice.rest.exception;

public abstract class ServiceException extends RuntimeException {

	public abstract int getStatusCode();

	@Override
	public abstract String getMessage();

	public abstract String getError();
}
