package com.jakubfilo.peopleservice.domain;

import java.time.DayOfWeek;

import lombok.Data;

@Data
public class CourseTime implements Comparable<CourseTime> {

	private DayOfWeek dayOfWeek;
	private int startHour;
	private int startMinute;
	private int endHour;
	private int endMinute;

	@Override
	public int compareTo(CourseTime other) {
		if (startHour == other.startHour) {
			return Integer.compare(startMinute, other.startMinute);
		}
		return Integer.compare(startHour, other.startHour);
	}
}