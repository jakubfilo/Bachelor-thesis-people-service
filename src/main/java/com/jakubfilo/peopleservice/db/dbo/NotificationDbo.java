package com.jakubfilo.peopleservice.db.dbo;

import org.springframework.data.annotation.Id;

import com.jakubfilo.peopleservice.domain.NotificationType;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
public record NotificationDbo(
		@Id
		String id,
		String studentId,
		String courseId,
		NotificationType type,
		String message) {
}
