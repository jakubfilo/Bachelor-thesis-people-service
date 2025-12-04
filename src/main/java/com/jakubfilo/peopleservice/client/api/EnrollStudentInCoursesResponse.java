package com.jakubfilo.peopleservice.client.api;

import java.util.Set;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class EnrollStudentInCoursesResponse {

	String studentId;
	@Builder.Default
	Set<String> enrolledCourses = Set.of();
}