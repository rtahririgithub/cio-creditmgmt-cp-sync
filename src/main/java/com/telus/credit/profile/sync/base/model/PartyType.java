package com.telus.credit.profile.sync.base.model;

public enum PartyType {

	INDIVIDUAL("Individual"),
	ORGANIZATION("Organization");
	
	private final String type;
	
	PartyType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
}
