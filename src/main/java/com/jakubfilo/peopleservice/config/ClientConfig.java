package com.jakubfilo.peopleservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jakubfilo.peopleservice.client.api.CourseControllerApi;
import com.jakubfilo.peopleservice.client.api.ExternalCourseControllerApi;
import com.jakubfilo.peopleservice.client.invoker.ApiClient;

@Configuration
public class ClientConfig {

	@Bean
	public ExternalCourseControllerApi externalCourseControllerApi(ApiClient apiClient) {
		return new ExternalCourseControllerApi(apiClient);
	}

	@Bean
	public CourseControllerApi courseControllerApi(ApiClient apiClient) {
		return new CourseControllerApi(apiClient);
	}

	@Bean
	public ApiClient apiClient() {
		return new ApiClient();
	}
}
