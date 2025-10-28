package com.jakubfilo.peopleservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class StudentNotification {

	private String studentId;
	private String courseId;
	private NotificationType type;
	private String message;
}
