package com.jakubfilo.peopleservice.domain;

public record CourseTimetableDetail(String courseId, CourseTime courseTime, String roomId) implements Comparable<CourseTimetableDetail> {

	@Override
	public int compareTo(CourseTimetableDetail other) {
		return courseTime().compareTo(other.courseTime());
	}
}