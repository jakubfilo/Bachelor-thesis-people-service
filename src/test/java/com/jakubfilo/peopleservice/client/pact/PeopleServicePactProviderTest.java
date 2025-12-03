package com.jakubfilo.peopleservice.client.pact;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.jakubfilo.peopleservice.facade.StudentsFacade;
import com.jakubfilo.peopleservice.mapper.NotificationMapper;
import com.jakubfilo.peopleservice.rest.StudentsController;

import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junitsupport.Consumer;
import au.com.dius.pact.provider.junitsupport.IgnoreNoPactsToVerify;
import au.com.dius.pact.provider.junitsupport.Provider;
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
		context.verifyInteraction();
	}



}
