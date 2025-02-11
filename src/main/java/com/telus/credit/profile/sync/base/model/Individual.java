package com.telus.credit.profile.sync.base.model;

import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Individual implements RelatedPartyInterface {
	
	public Individual() {
	}
	private String id;
	private String birthDate;
	private String role;
	private List<ContactMedium> contactMedium;
	private List<TelusIndividualIdentification> individualIdentification;
	private List<TelusCharacteristic> characteristic;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public List<ContactMedium> getContactMedium() {
		return contactMedium;
	}

	public void setContactMedium(List<ContactMedium> contactMedium) {
		this.contactMedium = contactMedium;
	}

	public List<TelusIndividualIdentification> getIndividualIdentification() {
		return individualIdentification;
	}

	public void setIndividualIdentification(List<TelusIndividualIdentification> individualIdentification) {
		this.individualIdentification = individualIdentification;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public PartyType getRelatedPartyType() {
		return PartyType.INDIVIDUAL;
	}
	public List<TelusCharacteristic> getCharacteristic() {
		return characteristic;
	}

	public void setCharacteristic(List<TelusCharacteristic> characteristicList) {
		this.characteristic = characteristicList;
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
