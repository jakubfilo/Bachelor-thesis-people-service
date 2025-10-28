package com.jakubfilo.peopleservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.jakubfilo.peopleservice.domain.StudentNotification;
import com.jakubfilo.peopleservice.rest.dto.StudentNotificationDto;

@Mapper
public interface NotificationMapper {

	NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);

	@Mapping(source = "studentId", target = "studentId")
	StudentNotification map(StudentNotificationDto dto, String studentId);
}
