package com.jakubfilo.peopleservice.rest.dto;

import com.jakubfilo.peopleservice.domain.NotificationType;

import jakarta.annotation.Nullable;

public record StudentNotificationDto(
		@Nullable
		String courseId,
		NotificationType type,
		String message
) {
}
