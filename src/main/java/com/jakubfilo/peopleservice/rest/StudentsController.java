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

import com.jakubfilo.peopleservice.domain.StudentNotification;
import com.jakubfilo.peopleservice.facade.StudentsFacade;
import com.jakubfilo.peopleservice.mapper.NotificationMapper;
import com.jakubfilo.peopleservice.rest.dto.StudentNotificationDto;
import com.jakubfilo.peopleservice.rest.exception.EnrollStudentInvalidCoursesException;
import com.jakubfilo.peopleservice.rest.response.EnrichedStudentRepresentation;
import com.jakubfilo.peopleservice.rest.response.MultipleStudentsDetailRepresentation;
import com.jakubfilo.peopleservice.rest.response.StudentDetailRepresentation;
import com.jakubfilo.peopleservice.rest.response.StudentTimetableRepresentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
	private NotificationMapper notificationMapper = NotificationMapper.INSTANCE;

	@Operation(
			description = "Returns list of student details (enrolled courses, personal details) for the given set of student IDs",
			responses = {
					@ApiResponse(responseCode = "200", description = "Returned student details successfully"),
			})
	@GetMapping("/detail/batch-lookup")
	public ResponseEntity<MultipleStudentsDetailRepresentation> getStudentDetailsBatchLookup(
			@NotEmpty @RequestParam(name = "ids") Set<String> studentIds) {

		var studentDetails = studentFacade.getMultipleStudentDetailsBatchLookup(studentIds);
		return ResponseEntity.ok(studentDetails);
	}

	@Operation(
			description = "Returns brief details about student",
			responses = {
					@ApiResponse(responseCode = "200", description = "Returned student detail successfully"),
					@ApiResponse(responseCode = "404", description = "Student with given ID not found")
			})
	@GetMapping("/detail/{studentId}/brief")
	public ResponseEntity<StudentDetailRepresentation> getStudentDetailsBrief(@NotBlank @PathVariable(name = "studentId") String studentId) {
		var studentDetail = studentFacade.getStudentDetailBrief(studentId);
		return studentDetail.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@Operation(
			description = "Returns complete details about student, enriched with full course info",
			responses = {
					@ApiResponse(responseCode = "200", description = "Returned student detail successfully"),
					@ApiResponse(responseCode = "404", description = "Student with given ID not found")
			})
	@GetMapping("/detail/{studentId}/complete")
	public ResponseEntity<EnrichedStudentRepresentation> getStudentEnrichedWithCourseInfo(
			@NotBlank @PathVariable(name = "studentId") String studentId) {

		var studentDetail = studentFacade.getStudentDetailEnriched(studentId);
		return studentDetail.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	/**
	 * A good example of API spec benefits. Outsider doesn't need to know the internal logic of the service.
	 * I can have various exception mappings {@link EnrollStudentInvalidCoursesException}
	 * mapped to various responses (e.g. in {@link ControllerAdvisor})
	 * With the API spec, one can simply go over the expected return codes and responses and know what to expect.
	 */
	@Operation(
			description = "Enrolls a new student into the system",
			responses = {
					@ApiResponse(responseCode = "201", description = "Student enrolled successfully")
			})
	@PostMapping(path = "/enroll", consumes = APPLICATION_JSON_VALUE)
	public ResponseEntity<StudentDetailRepresentation> enrollStudent(@Valid @RequestBody StudentDetailRepresentation studentDetail) {
		var enrolledStudent = studentFacade.enrollStudent(studentDetail);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(enrolledStudent);
	}

	@Operation(
			description = "Returns timetable for the given student ID",
			responses = {
					@ApiResponse(responseCode = "200", description = "Returned timetable successfully"),
					@ApiResponse(responseCode = "404", description = "Student with given ID not found")
			})
	@GetMapping("/timetable/{studentId}")
	public ResponseEntity<StudentTimetableRepresentation> getTimetableForStudent(@PathVariable(name = "studentId") String studentId) {
		var timetable = studentFacade.getTimetableForStudent(studentId);
		return timetable.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@Operation(
			description = "Sends notification to the given student",
			responses = {
					@ApiResponse(responseCode = "201", description = "Notification sent successfully")
			})
	@PostMapping("/notify/{studentId}")
	public ResponseEntity<StudentNotification> notifyStudent(@PathVariable(name = "studentId") String studentId,
			@RequestBody StudentNotificationDto studentNotificationApi) {

		var studentNotification = notificationMapper.map(studentNotificationApi, studentId);
		studentFacade.notifyStudent(studentNotification);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(studentNotification);
	}

//	/**
//	 * This is a demo endpoint to show why we should use DTOs instead of domain objects directly.
//	 * By using a DTO, I can avoid having to write the studentId in the request body.
//	 * However, in the domain object, I need the studentId to identify the student.
//	 * By splitting DTOs and domain objects I am no longer required to make sacrifices in any of the 2 ways
//	 * NOTE: I am not saying that using or not using the DI in the object is either good or bad practice. It just shows
//	 * how we can achieve better isolation of domain and API layers.
//	 */
//	@PostMapping("/notify/{id}/demo-why-to-use-dto")
//	public ResponseEntity<StudentNotification> notifyStudentDemoNoIdInRequestBody(
//			@PathVariable(name = "id") String studentId,
//			@RequestBody @Valid StudentNotification studentNotification) {
//		studentFacade.notifyStudent(studentNotification);
//		return ResponseEntity.ok(studentNotification);
//	}
}
