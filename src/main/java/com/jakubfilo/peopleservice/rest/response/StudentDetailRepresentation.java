package com.jakubfilo.peopleservice.rest.response;

import java.util.Set;

import com.jakubfilo.peopleservice.db.dbo.StudentDbo;
import com.jakubfilo.peopleservice.domain.PhoneNumber;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class StudentDetailRepresentation {

	@NotBlank
	String id;
	@NotBlank
	String name;
	@NotBlank
	String email;
	@NotNull
	PhoneNumber phoneNumber;
	@Builder.Default
	float gpa = 0;
	@Builder.Default
	Set<String> courses = Set.of();
	// TODO extend?

	public static StudentDetailRepresentation from(StudentDbo studentDbo) {
		return StudentDetailRepresentation.builder()
				.id(studentDbo.getId())
				.name(studentDbo.getName())
				.email(studentDbo.getEmail())
				.phoneNumber(studentDbo.getPhoneNumber())
				.gpa(studentDbo.getGpa())
				.courses(studentDbo.getCourses())
				.build();
	}

	public static StudentDetailRepresentation unknownStudent(String studentId) {
		return StudentDetailRepresentation.builder()
				.id(studentId)
				.name("Unknown")
				.email("Unknown")
				.phoneNumber(PhoneNumber.builder()
						.number("Unknown")
						.countryCode("Unknown")
						.build())
				.build();
	}
}
