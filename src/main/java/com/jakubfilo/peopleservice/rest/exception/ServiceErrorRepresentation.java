package com.jakubfilo.peopleservice.rest.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class ServiceErrorRepresentation {

	private final String message;
	private final String error;
	private final int statusCode;

	protected ServiceErrorRepresentation(Builder builder) {
		this.message = builder.message;
		this.error = builder.error;
		this.statusCode = builder.statusCode;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static Builder builder(ServiceException exception) {
		return new Builder(exception);
	}

	@NoArgsConstructor
	public static class Builder {
		private String message;
		private String error;
		private int statusCode;

		private Builder(ServiceException exception) {
			this.message = exception.getMessage();
			this.error = exception.getError();
			this.statusCode = exception.getStatusCode();
		}

		public Builder message(String message) {
			this.message = message;
			return this;
		}

		public Builder statusCode(int code) {
			this.statusCode = code;
			return this;
		}

		public Builder error(String error) {
			this.error = error;
			return this;
		}

		public ServiceErrorRepresentation build() {
			return new ServiceErrorRepresentation(this);
		}
	}
}
