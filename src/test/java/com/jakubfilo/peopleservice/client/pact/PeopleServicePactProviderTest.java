package com.jakubfilo.peopleservice.client.pact;

import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.jakubfilo.peopleservice.domain.PhoneNumber;
import com.jakubfilo.peopleservice.facade.StudentsFacade;
import com.jakubfilo.peopleservice.mapper.NotificationMapper;
import com.jakubfilo.peopleservice.rest.StudentsController;
import com.jakubfilo.peopleservice.rest.response.MultipleStudentsDetailRepresentation;
import com.jakubfilo.peopleservice.rest.response.StudentDetailRepresentation;

import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junitsupport.Consumer;
import au.com.dius.pact.provider.junitsupport.IgnoreNoPactsToVerify;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.junitsupport.loader.PactBrokerAuth;
import au.com.dius.pact.provider.spring.junit5.MockMvcTestTarget;
import au.com.dius.pact.provider.spring.junit5.PactVerificationSpringProvider;
import jakarta.inject.Inject;

@Provider(PactConstants.PEOPLE_SERVICE_COMPONENT_NAME)
@Consumer(PactConstants.SCHOOL_SERVICE_COMPONENT_NAME)
@IgnoreNoPactsToVerify
@PactBroker(url = "${pactbroker.url:https://pact-broker-app.gentlefield-825c3ee5.polandcentral.azurecontainerapps.io}",
			authentication = @PactBrokerAuth(username = "${pactbroker.auth.username}", password = "${pactbroker.auth.password}"))
@WebMvcTest(
		controllers = {
				StudentsController.class
		}
)
@AutoConfigureMockMvc(addFilters = false)
/*
 you need to add environemnt variables into junit configuration

 In IntelliJ, right-click your test class → Run 'PeopleServicePactProviderTest'
 It will create a temporary run configuration.
 Open Run → Edit Configurations…
 Find your JUnit test configuration
 Add environment variables:
 */
public class PeopleServicePactProviderTest {

	@Inject
	private MockMvc mockMvc;

	@MockitoBean
	private StudentsFacade studentsFacade;

	@MockitoBean
	private NotificationMapper notificationMapper;

	@BeforeEach
	void setUp(PactVerificationContext context) {
		if (context != null) {
			context.setTarget(new MockMvcTestTarget(mockMvc));
		}
	}

	@TestTemplate
	@ExtendWith(PactVerificationSpringProvider.class)
	void verifyPact(PactVerificationContext context) {
		if (context != null) {
			context.verifyInteraction();
		}
	}

	@State("People exist with provided IDs")
	void batchLookupStudents() {
		// by itself this also tests API response codes - if consumer expects 200 and I change it to 206 in provider, this test would fail
		when(studentsFacade.getMultipleStudentDetailsBatchLookup(Set.of("STUDENT1", "STUDENT2")))
			.thenReturn(new MultipleStudentsDetailRepresentation(Set.of(
				StudentDetailRepresentation.builder()
					.id("STUDENT1")
					.name("Jakub Filo") // in provider tests, the value doesnt have to match exact value that is in consumner, only match the form and format - in this case, regex for "^[A-Za-z]+( [A-Za-z]+)+$"
					.email("student1@muni.cz")
					.gpa(3.75f)
					.phoneNumber(PhoneNumber.builder().number("777123456").countryCode("+420").build())
					.courses(Set.of("COURSE1", "COURSE2"))
					.build(),
				StudentDetailRepresentation.builder()
					.id("STUDENT2")
					.name("Mario Rodrigo")
					.email("student2@gmail.com")
					.gpa(2.2f)
					.phoneNumber(PhoneNumber.builder().number("944035978").countryCode("+421").build())
					.courses(Set.of("COURSE1", "COURSE42"))
					.build()
			), 2));
	}



}
