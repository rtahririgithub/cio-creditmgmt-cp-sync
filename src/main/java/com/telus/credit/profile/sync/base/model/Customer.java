package com.telus.credit.profile.sync.base.model;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.cloud.firestore.annotation.Exclude;

@JsonInclude(Include.NON_NULL)
public class Customer extends BaseResponse {
	private static final long serialVersionUID = 1L;

	private String id;// Customer id
	private String custUid;
	private List<TelusCreditProfile> creditProfile;

	private String baseType;
	private String schemaLocation;
	private String type;

	private AccountInfo accountInfo;
	
	private String createUpdateFlag;

	private long eventReceivedTime;
	private long submitterEventTime;
	
	private String eventDescription;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCustUid() {
		return custUid;
	}

	public void setCustUid(String custUid) {
		this.custUid = custUid;
	}

	@JsonProperty("@baseType")
	public String getBaseType() {
		return this.baseType;
	}

	public void setBaseType(String baseType) {
		this.baseType = baseType;
	}

	@Exclude
	@JsonProperty("@schemaLocation")
	public String getSchemaLocation() {
		return schemaLocation;
	}

	@Exclude
	public void setSchemaLocation(String schemaLocation) {
		this.schemaLocation = schemaLocation;
	}

	@JsonProperty("@type")
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<TelusCreditProfile> getCreditProfile() {
		return creditProfile;
	}

	public void setCreditProfile(List<TelusCreditProfile> creditProfile) {
		this.creditProfile = creditProfile;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}

	public AccountInfo getAccountInfo() {
		return accountInfo;
	}

	public void setAccountInfo(AccountInfo accountInfo) {
		this.accountInfo = accountInfo;
	}

   public String getCreateUpdateFlag() {
      return createUpdateFlag;
   }

   public void setCreateUpdateFlag(String createUpdateFlag) {
      this.createUpdateFlag = createUpdateFlag;
   }

   public long getEventReceivedTime() {
      return eventReceivedTime;
   }

   public void setEventReceivedTime(long eventTimeInMillis) {
      this.eventReceivedTime = eventTimeInMillis;
   }

public String getEventDescription() {
	return eventDescription;
}

public void setEventDescription(String eventDescription) {
	this.eventDescription = eventDescription;
}

public long getSubmitterEventTime() {
	return submitterEventTime;
}

public void setSubmitterEventTime(long submitterEventTime) {
	this.submitterEventTime = submitterEventTime;
}

}
