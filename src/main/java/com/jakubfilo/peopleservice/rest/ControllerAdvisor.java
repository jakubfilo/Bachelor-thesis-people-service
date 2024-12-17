package com.jakubfilo.peopleservice.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.jakubfilo.peopleservice.rest.exception.ServiceErrorRepresentation;
import com.jakubfilo.peopleservice.rest.exception.ServiceException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ControllerAdvisor {

	@ExceptionHandler(ServiceException.class)
	public ResponseEntity<ServiceErrorRepresentation> handleServiceException(ServiceException e) {
		var representation = ServiceErrorRepresentation
				.builder(e)
				.build();

		return ResponseEntity.status(e.getStatusCode()).body(representation);
	}
}
