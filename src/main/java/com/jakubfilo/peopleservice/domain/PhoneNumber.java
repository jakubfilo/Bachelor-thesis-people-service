package com.jakubfilo.peopleservice.domain;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PhoneNumber {

	String number;
	PhoneNumberCountryCode country;
}
