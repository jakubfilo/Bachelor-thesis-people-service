package com.jakubfilo.peopleservice.facade;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jakubfilo.peopleservice.db.StudentsRepository;
import com.jakubfilo.peopleservice.rest.response.MultipleStudentsDetailRepresentation;
import com.jakubfilo.peopleservice.rest.response.StudentDetailRepresentation;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component // TODO do I want this to be annotated as component or as configuration clases
public class StudentsFacade {

	private final StudentsRepository studentsRepository;

	public StudentDetailRepresentation getStudentDetail(String studentId) {
		return studentsRepository.findById(studentId)
				.map(StudentDetailRepresentation::from)
				.orElseGet(() -> StudentDetailRepresentation.unknownStudent(studentId));
	}

	public Set<StudentDetailRepresentation> getMultipleStudentDetails(Set<String> studentIds) {
		return studentIds.stream()
				.map(this::getStudentDetail)
				.collect(Collectors.toSet());
	}

	public MultipleStudentsDetailRepresentation getMultipleStudentDetailsBatchLookup(Set<String> studentIds) {
		var studentDetails = getMultipleStudentDetails(studentIds);
		return new MultipleStudentsDetailRepresentation(studentDetails, studentDetails.size());
	}
}
