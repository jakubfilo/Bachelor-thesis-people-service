package com.jakubfilo.peopleservice.facade;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jakubfilo.peopleservice.db.StudentsRepository;
import com.jakubfilo.peopleservice.rest.response.EnrichedStudentRepresentation;
import com.jakubfilo.peopleservice.rest.response.MultipleStudentsDetailRepresentation;
import com.jakubfilo.peopleservice.rest.response.StudentDetailRepresentation;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class StudentsFacade {

	private final StudentsRepository studentsRepository;

	public StudentDetailRepresentation getStudentDetailWithFallback(String studentId) {
		return studentsRepository.findById(studentId)
				.map(StudentDetailRepresentation::from)
				.orElseGet(() -> StudentDetailRepresentation.unknownStudent(studentId));
	}

	public Set<StudentDetailRepresentation> getMultipleStudentDetails(Set<String> studentIds) {
		return studentIds.stream()
				.map(this::getStudentDetailWithFallback)
				.collect(Collectors.toSet());
	}

	public MultipleStudentsDetailRepresentation getMultipleStudentDetailsBatchLookup(Set<String> studentIds) {
		var studentDetails = getMultipleStudentDetails(studentIds);
		return new MultipleStudentsDetailRepresentation(studentDetails, studentDetails.size());
	}

	public Optional<StudentDetailRepresentation> getStudentDetailBrief(String studentId) {
		return studentsRepository.findById(studentId)
				.map(StudentDetailRepresentation::from);
	}

}
