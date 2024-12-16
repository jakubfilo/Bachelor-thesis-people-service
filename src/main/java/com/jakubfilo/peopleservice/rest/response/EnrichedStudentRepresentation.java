package com.jakubfilo.peopleservice.rest.response;

import java.util.Set;

import com.jakubfilo.peopleservice.domain.PhoneNumber;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class EnrichedStudentRepresentation {

	@NotBlank
	String id;
	@NotBlank
	String name;
	String email;
	PhoneNumber phoneNumber;
	float gpa;
	@Builder.Default
	Set<String> coursesIds = Set.of();
	@Builder.Default
	Set<CourseRepresentation> coursesDetailed = Set.of();
	boolean allCoursesInfoPresent;
}
