package com.jakubfilo.peopleservice.facade;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jakubfilo.peopleservice.client.SchoolServiceClient;
import com.jakubfilo.peopleservice.db.StudentsRepository;
import com.jakubfilo.peopleservice.db.dbo.StudentDbo;
import com.jakubfilo.peopleservice.rest.exception.DuplicateStudentException;
import com.jakubfilo.peopleservice.rest.exception.InvalidCoursesException;
import com.jakubfilo.peopleservice.rest.response.CourseRepresentation;
import com.jakubfilo.peopleservice.rest.response.EnrichedStudentRepresentation;
import com.jakubfilo.peopleservice.rest.response.MultipleStudentsDetailRepresentation;
import com.jakubfilo.peopleservice.rest.response.StudentDetailRepresentation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Component
public class StudentsFacade {

	private final StudentsRepository studentsRepository;
	private final SchoolServiceClient schoolServiceClient;

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

	public Optional<EnrichedStudentRepresentation> getStudentDetailEnriched(String studentId) {
		var student = studentsRepository.findById(studentId);

		return student.map(StudentDbo::getCourses)
				.map(schoolServiceClient::getCourseDetailsBatchLookup)
				.map(multipleCourseDetail -> EnrichedStudentRepresentation.builder()
						.id(studentId)
						.name(student.get().getName())
						.email(student.get().getEmail())
						.phoneNumber(student.get().getPhoneNumber())
						.gpa(student.get().getGpa())
						.coursesDetailed(multipleCourseDetail.getCourses().stream()
								.map(CourseRepresentation::from)
								.collect(Collectors.toSet()))
						.allCoursesInfoPresent(multipleCourseDetail.isCourseInfoComplete())
						.build());
	}

	public StudentDetailRepresentation enrollStudent(StudentDetailRepresentation studentDetail) {

		if (studentsRepository.existsById(studentDetail.getId())) {
			LOGGER.warn("Student with id '{}' already exists", studentDetail.getId());
			throw new DuplicateStudentException(studentDetail.getId());
		}

		var studentDbo = StudentDbo.builder()
				.id(studentDetail.getId())
				.name(studentDetail.getName())
				.email(studentDetail.getEmail())
				.phoneNumber(studentDetail.getPhoneNumber())
				.gpa(studentDetail.getGpa())
				.courses(studentDetail.getCourses())
				.build();

		studentsRepository.save(studentDbo);
		var enrolledStudentResponse = schoolServiceClient.enrollStudentToCourses(studentDetail.getCourses(), studentDetail.getId());
		if (!enrolledStudentResponse.getInvalidCourses().isEmpty()) {
			throw new InvalidCoursesException(enrolledStudentResponse.getInvalidCourses());
		}

		LOGGER.info("Student with id '{}' successfully enrolled to courses '{}'", studentDetail.getId(), studentDetail.getCourses());
		return studentDetail;
	}
}
