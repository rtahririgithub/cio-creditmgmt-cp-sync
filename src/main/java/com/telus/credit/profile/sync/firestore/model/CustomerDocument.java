package com.telus.credit.profile.sync.firestore.model;

import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.cloud.firestore.annotation.Exclude;
import com.google.cloud.firestore.annotation.PropertyName;
import com.telus.credit.profile.sync.base.model.Customer;

@JsonInclude(Include.NON_NULL)
public class CustomerDocument {
	
	
	private Map<String,Object> metaData;

	
	private Customer customer;
	private String fireStoreId;

	private long publishTimeinNanos;	
	private String eventDescription;
	


	@PropertyName("metadata")
	public Map<String,Object> getMetaData() {
		return metaData;
	}

	@PropertyName("metadata")
	public void setMetaData(Map<String,Object> metaData) {
		this.metaData = metaData;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	@Exclude
	public String getFireStoreId() {
		return fireStoreId;
	}

	@Exclude
	public void setFireStoreId(String fireStoreId) {
		this.fireStoreId = fireStoreId;
	}


	public long getPublishTimeinNanos() {
		return publishTimeinNanos;
	}

	public void setPublishTimeinNanos(long publishTimeinNanos) {
		this.publishTimeinNanos = publishTimeinNanos;
	}
	
	
	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String val) {
		this.eventDescription = val;
	}	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
