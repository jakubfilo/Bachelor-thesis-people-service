package com.jakubfilo.peopleservice.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PhoneNumber {

	@NotBlank
	String number;
	@NotBlank
	String countryCode;
}
