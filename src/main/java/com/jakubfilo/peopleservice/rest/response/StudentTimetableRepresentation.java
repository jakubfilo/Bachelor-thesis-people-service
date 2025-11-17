package com.jakubfilo.peopleservice.rest.response;

import java.time.DayOfWeek;
import java.util.Map;
import java.util.Set;

import com.jakubfilo.peopleservice.domain.CourseTimetableDetail;

import lombok.AllArgsConstructor;

public record StudentTimetableRepresentation(String studentId, Map<DayOfWeek, Set<CourseTimetableDetail>> timetable) {
}
