package com.jakubfilo.peopleservice.rest.response;

import java.util.Set;

public record MultipleStudentsDetailRepresentation(
		Set<StudentDetailRepresentation> students,
		int studentsCount
) {
}
