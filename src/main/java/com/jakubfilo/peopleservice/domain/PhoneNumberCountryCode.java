package com.jakubfilo.peopleservice.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PhoneNumberCountryCode {

	CZ("CZ", "+420"),
	SK("SK", "+421"),
	US("US", "+1"),
	UK("UK", "+44"),
	DE("GE", "+49");

	@NotNull
	private final String country;
	@NotNull
	private final String countryCode;
}
