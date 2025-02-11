package com.telus.credit.profile.sync.base.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;




@JsonInclude(Include.NON_NULL)
public class RelatedParty {


    private String id;
    
    private String role;
    private String href;
    private String name;
    @JsonProperty("@type")
    private String type;
    @JsonProperty("@schemaLocation")
    private String schemaLocation;
    @JsonProperty("@referredType")
    private String atReferredType;
    @JsonProperty("@baseType")
    private String atBaseType;


    
    private RelatedPartyInterface engagedParty;
    public RelatedPartyInterface getEngagedParty() {
 	      return engagedParty;
 	   }
    public void setEngagedParty(RelatedPartyInterface engagedParty) {
       this.engagedParty = engagedParty;
    }  
  //************************    
    private Individual individual;
	public void setIndividual(Individual val) {
		this.individual=val;	
	}
    public Individual getIndividual() {
        return this.individual;
    }
    private Organization organization;
	public void setOrganization(Organization val) {
		this.organization=val;	
	}	
    public Organization getOrganization() {
        return this.organization;
    }	
  //************************     


    public String getId() {
        return id;
    }

    public void setId(String customerId) {
        this.id = customerId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSchemaLocation() {
        return schemaLocation;
    }

    public void setSchemaLocation(String schemaLocation) {
        this.schemaLocation = schemaLocation;
    }

    public String getAtReferredType() {
        return atReferredType;
    }

    public void setAtReferredType(String atReferredType) {
        this.atReferredType = atReferredType;
    }

    public String getAtBaseType() {
        return atBaseType;
    }

    public void setAtBaseType(String atBaseType) {
        this.atBaseType = atBaseType;
    }
  
}
