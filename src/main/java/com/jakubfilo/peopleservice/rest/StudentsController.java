package com.jakubfilo.peopleservice.rest;

import static com.jakubfilo.peopleservice.rest.StudentsController.STUDENTS_PATH;

import java.util.Set;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jakubfilo.peopleservice.facade.StudentsFacade;
import com.jakubfilo.peopleservice.rest.response.MultipleStudentsDetailRepresentation;
import com.jakubfilo.peopleservice.rest.response.StudentDetailRepresentation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = STUDENTS_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class StudentsController {

	public static final String STUDENTS_PATH = "/students";

	private final StudentsFacade studentFacade;

	@GetMapping("/detail/batch-lookup")
	public ResponseEntity<MultipleStudentsDetailRepresentation> getStudentDetailsBatchLookup(
			@NotEmpty @RequestParam(name = "ids") Set<String> studentIds) {

		var studentDetails = studentFacade.getMultipleStudentDetailsBatchLookup(studentIds);
		return ResponseEntity.ok(studentDetails);
	}

	@GetMapping("/detail/{id}/brief")
	public ResponseEntity<StudentDetailRepresentation> getStudentDetailsBrief(@NotBlank @PathVariable(name = "id") String studentId) {
		var studentDetail = studentFacade.getStudentDetailBrief(studentId);
		return studentDetail.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
}
