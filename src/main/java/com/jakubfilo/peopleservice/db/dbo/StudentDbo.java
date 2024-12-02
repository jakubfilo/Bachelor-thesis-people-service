package com.jakubfilo.peopleservice.db.dbo;

import java.util.Set;

import org.springframework.data.annotation.Id;

import com.jakubfilo.peopleservice.domain.PhoneNumber;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Jacksonized
@Builder
public class StudentDbo {

	@Id
	String id; // Student number, like MUNI's UCO - is unique
	String name;
	String email;
	PhoneNumber phoneNumber;
	float gpa;
	Set<String> courses;
}
