package com.jakubfilo.peopleservice.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jakubfilo.peopleservice.client.SchoolServiceClient;

@Configuration
@EnableFeignClients(basePackages = "com.jakubfilo.peopleservice.client")
public class FeignClientConfiguration {

	public static class SchoolServiceClientConfiguration {

		@Bean
		public FallbackFactory<SchoolServiceClient> fallbackFactory() {
			return new SchoolServiceClient.Fallback();
		}
	}
}
