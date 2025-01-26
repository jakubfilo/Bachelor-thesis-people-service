package com.jakubfilo.peopleservice.rest.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ServiceErrorRepresentation {

	private final String message;
	private final String error;
	private final int statusCode;

	public static ServiceErrorRepresentationBuilder builder(ServiceException exception) {
		return new ServiceErrorRepresentationBuilder(exception);
	}

	public static class ServiceErrorRepresentationBuilder {

		private ServiceErrorRepresentationBuilder(ServiceException exception) {
			this.message = exception.getMessage();
			this.error = exception.getError();
			this.statusCode = exception.getStatusCode();
		}
	}
}
