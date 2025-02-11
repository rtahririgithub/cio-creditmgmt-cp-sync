package com.telus.credit.profile.sync.base.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

@JsonTypeInfo( use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@referredType")
@JsonSubTypes({ 
  @Type(value = Individual.class, name = "Individual"), 
  @Type(value = Organization.class, name = "Organization"), 
  
})


public interface RelatedPartyInterface {

	
	
	@JsonIgnore
	PartyType getRelatedPartyType();

	@JsonProperty("@referredType")
	public String AtReferredType="";
	
	default String getAtReferredType() {
		return getRelatedPartyType().getType();
	}
	
	

}
