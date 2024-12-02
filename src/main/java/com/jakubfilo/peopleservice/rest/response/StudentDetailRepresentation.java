package com.jakubfilo.peopleservice.rest.response;

import java.util.Set;

import com.jakubfilo.peopleservice.db.dbo.StudentDbo;
import com.jakubfilo.peopleservice.domain.PhoneNumber;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class StudentDetailRepresentation {

	String id;
	String name;
	String email;
	PhoneNumber phoneNumber;
	float gpa;
	Set<String> courses;
	// TODO add deparment? Does every student need to have a department to be under?
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
						.country(null)
						.build())
				.gpa(0)
				.courses(Set.of())
				.build();
	}
}
