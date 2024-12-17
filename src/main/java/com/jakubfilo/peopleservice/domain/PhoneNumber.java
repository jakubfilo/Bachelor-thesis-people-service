package com.jakubfilo.peopleservice.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PhoneNumber {

	@NotBlank
	String number;
	@NotNull
	PhoneNumberCountryCode country;
}
