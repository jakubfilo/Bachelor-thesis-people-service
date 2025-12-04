package com.jakubfilo.peopleservice.mapper;

import java.time.DayOfWeek;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.jakubfilo.peopleservice.domain.CourseTimetableDetail;

@Mapper
public interface CoursesApiMapper {

	CoursesApiMapper INSTANCE = Mappers.getMapper(CoursesApiMapper.class);

	CourseTimetableDetail mapToDomain(com.jakubfilo.peopleservice.client.model.CourseTimetableDetail dto);

	DayOfWeek mapToDomain(com.jakubfilo.peopleservice.client.model.DayOfWeek dto);

}
