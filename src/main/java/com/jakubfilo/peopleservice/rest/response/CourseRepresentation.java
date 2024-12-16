package com.jakubfilo.peopleservice.rest.response;

import com.jakubfilo.peopleservice.api.DepartmentType;
import com.jakubfilo.peopleservice.api.Term;
import com.jakubfilo.peopleservice.client.api.CourseDetailRepresentationApi;

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

	public static CourseRepresentation from(CourseDetailRepresentationApi courseDetailRepresentationApi) {
		return CourseRepresentation.builder()
				.id(courseDetailRepresentationApi.getId())
				.courseName(courseDetailRepresentationApi.getCourseName())
				.courseCode(courseDetailRepresentationApi.getCourseCode())
				.credits(courseDetailRepresentationApi.getCredits())
				.description(courseDetailRepresentationApi.getDescription())
				.departmentId(courseDetailRepresentationApi.getDepartmentId())
				.departmentType(courseDetailRepresentationApi.getDepartmentType())
				.teacherId(courseDetailRepresentationApi.getTeacherId())
				.term(courseDetailRepresentationApi.getTerm())
				.startYear(courseDetailRepresentationApi.getStartYear())
				.enrolledStudentsCount(courseDetailRepresentationApi.getEnrolledStudentsCount())
				.build();
		// wanted to separate Api object from school service, as it can contain unnecessary fields like enrolledStudentsIds
		// could also be done using Mapstruct. This is an alternative
	}
}
