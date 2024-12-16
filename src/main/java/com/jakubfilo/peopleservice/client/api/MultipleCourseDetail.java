package com.jakubfilo.peopleservice.client.api;

import java.util.Set;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder(toBuilder = true)
@Jacksonized
public class MultipleCourseDetail {

	@Builder.Default
	@NotEmpty
	Set<CourseDetailRepresentationApi> courses = Set.of();
	@Builder.Default
	boolean courseInfoComplete = true;
}
