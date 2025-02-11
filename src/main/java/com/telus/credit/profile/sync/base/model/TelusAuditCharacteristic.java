
/*
 package com.telus.credit.profile.sync.base.model;


import javax.validation.constraints.NotBlank;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class TelusAuditCharacteristic {

    @NotBlank( message = "1112")
    private String originatorApplicationId;
    private String channelOrganizationId;
    @NotBlank(message = "1113")
    private String userId;


    public String getOriginatorApplicationId() {
        return originatorApplicationId;
    }
    public void setOriginatorApplicationId(String originatorApplicationId) {
        this.originatorApplicationId = originatorApplicationId;
    }
    public String getChannelOrganizationId() {
        return channelOrganizationId;
    }
    public void setChannelOrganizationId(String channelOrganizationId) {
        this.channelOrganizationId = channelOrganizationId;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}

*/