package com.jakubfilo.peopleservice.api;

public enum DepartmentType {

	COMPUTER_SCIENCE, MATHEMATICS, MECHANICAL_ENGINEERING, SCIENCE;

	public static DepartmentType fromGeneratedApiLayer(com.jakubfilo.peopleservice.client.model.DepartmentType departmentType) {
		return switch (departmentType) {
			case COMPUTER_SCIENCE -> COMPUTER_SCIENCE;
			case MATHEMATICS -> MATHEMATICS;
			case MECHANICAL_ENGINEERING -> MECHANICAL_ENGINEERING;
			case SCIENCE -> SCIENCE;
		};
	}
}
