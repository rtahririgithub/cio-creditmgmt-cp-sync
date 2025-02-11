package com.telus.credit.profile.sync.base.model;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Organization implements RelatedPartyInterface {
	
	public Organization() {
	}
	private String id;
	private String role;
	private List<ContactMedium> contactMedium;
	private List<OrganizationIdentification> organizationIdentification;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<ContactMedium> getContactMedium() {
		return contactMedium;
	}

	public void setContactMedium(List<ContactMedium> contactMedium) {
		this.contactMedium = contactMedium;
	}

	public List<OrganizationIdentification> getOrganizationIdentification() {
		return organizationIdentification;
	}

	public void setOrganizationIdentification(List<OrganizationIdentification> organizationIdentification) {
		this.organizationIdentification = organizationIdentification;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public PartyType getRelatedPartyType() {
		return PartyType.ORGANIZATION;
	}
	private String birthDate;
	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
