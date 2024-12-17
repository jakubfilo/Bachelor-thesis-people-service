package com.jakubfilo.peopleservice.rest;

import static com.jakubfilo.peopleservice.rest.StudentsController.STUDENTS_PATH;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jakubfilo.peopleservice.facade.StudentsFacade;
import com.jakubfilo.peopleservice.rest.response.EnrichedStudentRepresentation;
import com.jakubfilo.peopleservice.rest.response.MultipleStudentsDetailRepresentation;
import com.jakubfilo.peopleservice.rest.response.StudentDetailRepresentation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = STUDENTS_PATH, produces = APPLICATION_JSON_VALUE)
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

	@GetMapping("/detail/{id}/complete")
	public ResponseEntity<EnrichedStudentRepresentation> getStudentEnrichedWithCourseInfo(
			@NotBlank @PathVariable(name = "id") String studentId) {

		var studentDetail = studentFacade.getStudentDetailEnriched(studentId);
		return studentDetail.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	/**
	 * A good example of API spec benefits. Outsider doesn't need to know the internal logic of the service.
	 * I can have various exception mappings {@link com.jakubfilo.peopleservice.rest.exception.InvalidCoursesException}
	 * mapped to various responses (e.g. in {@link ControllerAdvisor})
	 * With the API spec, one can simply go over the expected return codes and responses and know what to expect.
	 */
	@PostMapping(path = "/enroll", consumes = APPLICATION_JSON_VALUE)
	public ResponseEntity<StudentDetailRepresentation> enrollStudent(@Valid @RequestBody StudentDetailRepresentation studentDetail) {
		var enrolledStudent = studentFacade.enrollStudent(studentDetail);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(enrolledStudent);
	}
}
