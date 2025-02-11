package com.telus.credit.profile.sync.base.model;

import javax.validation.Valid;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class TelusCreditProfile extends CreditProfile {
	@Valid
	//private TelusCreditProfileCharacteristic telusCharacteristic;


	/*
	 * public TelusCreditProfileCharacteristic getTelusCharacteristic() { return
	 * telusCharacteristic; }
	 * 
	 * public void setTelusCharacteristic(TelusCreditProfileCharacteristic
	 * telusCharacteristic) { this.telusCharacteristic = telusCharacteristic; }
	 */

	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
