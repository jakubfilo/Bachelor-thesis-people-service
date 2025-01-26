package com.jakubfilo.peopleservice.facade;

import java.time.DayOfWeek;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jakubfilo.peopleservice.client.SchoolServiceClient;
import com.jakubfilo.peopleservice.client.api.CourseTimetableDetail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class CoursesFacade {

	private final SchoolServiceClient schoolServiceClient;

	public Map<DayOfWeek, Set<CourseTimetableDetail>> getTimetableForCourses(Set<String> courseIds, String studentId) {
		var coursesTimeResponse = schoolServiceClient.getCourseTimetableBatchLookup(courseIds, studentId);
		return buildTimetable(coursesTimeResponse);
	}

	private Map<DayOfWeek, Set<CourseTimetableDetail>> buildTimetable(Set<CourseTimetableDetail> courses) {
		return courses.stream()
				.sorted(CourseTimetableDetail::compareTo)
				.collect(Collectors.groupingBy(
						course -> course.courseTime().getDayOfWeek(),
						Collectors.toSet()));
	}
}
