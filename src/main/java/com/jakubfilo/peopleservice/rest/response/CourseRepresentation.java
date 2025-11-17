package com.jakubfilo.peopleservice.rest.response;

import com.jakubfilo.peopleservice.api.DepartmentType;
import com.jakubfilo.peopleservice.api.Term;
import com.jakubfilo.peopleservice.client.model.CourseDetail;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value// TODO can be value?
@Builder
@Jacksonized
public class CourseRepresentation {

	String id;
	String courseName;
	String courseCode;
	int credits;
	String description;
	String departmentId;
	DepartmentType departmentType;
	String teacherId;
	Term term;
	@Min(1993)
	@Max(2100)
	int startYear;
	int enrolledStudentsCount;

	public static CourseRepresentation from(CourseDetail courseDetailApi) {
		return CourseRepresentation.builder()
				.id(courseDetailApi.getId())
				.courseName(courseDetailApi.getCourseName())
				.courseCode(courseDetailApi.getCourseCode())
				.credits(courseDetailApi.getCredits())
				.description(courseDetailApi.getDescription())
				.departmentId(courseDetailApi.getDepartmentId())
				.departmentType(DepartmentType.fromGeneratedApiLayer(courseDetailApi.getDepartmentType()))
				.teacherId(courseDetailApi.getTeacherId())
				.term(Term.fromGeneratedApiLayer(courseDetailApi.getTerm()))
				.startYear(courseDetailApi.getStartYear())
				.enrolledStudentsCount(courseDetailApi.getEnrolledStudentsCount())
				.build();
		// wanted to separate Api object from school service, as it can contain unnecessary fields like enrolledStudentsIds
		// could also be done using Mapstruct. This is an alternative
	}
}
