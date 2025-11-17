package com.jakubfilo.peopleservice.api;

public enum Term {

	WINTER, SUMMER;

	public static Term fromGeneratedApiLayer(com.jakubfilo.peopleservice.client.model.Term term) {
		return switch (term) {
			case WINTER -> WINTER;
			case SUMMER -> SUMMER;
		};
	}
}
