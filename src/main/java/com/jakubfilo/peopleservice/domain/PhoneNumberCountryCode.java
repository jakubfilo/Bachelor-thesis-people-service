package com.jakubfilo.peopleservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PhoneNumberCountryCode {

	CZ("Czech Republic", "+420"),
	SK("Slovakia", "+421"),
	US("United States", "+1"),
	UK("United Kingdom", "+44"),
	DE("Germany", "+49");

	private String country;
	private String countryCode;
}
