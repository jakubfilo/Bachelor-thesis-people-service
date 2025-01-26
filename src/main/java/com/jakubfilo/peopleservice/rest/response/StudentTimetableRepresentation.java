package com.jakubfilo.peopleservice.rest.response;

import java.time.DayOfWeek;
import java.util.Map;
import java.util.Set;

import com.jakubfilo.peopleservice.client.api.CourseTimetableDetail;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class StudentTimetableRepresentation {

	String studentId;
	Map<DayOfWeek, Set<CourseTimetableDetail>> timetable;
}
