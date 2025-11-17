package com.jakubfilo.peopleservice.facade;

import java.time.DayOfWeek;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jakubfilo.peopleservice.client.SchoolServiceClient;
import com.jakubfilo.peopleservice.domain.CourseTimetableDetail;
import com.jakubfilo.peopleservice.mapper.CoursesApiMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class CoursesFacade {

	private final SchoolServiceClient schoolServiceClient;
	private final CoursesApiMapper coursesApiMapper = CoursesApiMapper.INSTANCE;

	public Map<DayOfWeek, Set<CourseTimetableDetail>> getTimetableForCourses(Set<String> courseIds, String studentId) {
		var coursesTimeResponse = schoolServiceClient.getCourseTimetableBatchLookup(courseIds, studentId);

		var courseTimeableDetails = coursesTimeResponse.stream()
				.map(coursesApiMapper::mapToDomain)
				.collect(Collectors.toSet());

		return buildTimetable(courseTimeableDetails);
	}

	private Map<DayOfWeek, Set<CourseTimetableDetail>> buildTimetable(Set<CourseTimetableDetail> courses) {
		return courses.stream()
				.sorted(CourseTimetableDetail::compareTo)
				.collect(Collectors.groupingBy(
						course -> course.courseTime().getDayOfWeek(),
						Collectors.toSet()));
	}
}
