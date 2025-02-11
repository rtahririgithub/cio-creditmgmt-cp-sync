package com.telus.credit.profile.sync.base.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.cloud.firestore.annotation.Exclude;

public class AccountInfo {

    private String accountType;

    private String accountSubType;

    private String status;

    private String brandId;

    private String language;

    private String statusDateNew;

    private String startServiceDateNew;

    private String statusActivityCode;

    private String statusActivityReasonCode;

    private String dealerCode;

    private String salesRepCode;

    private String corpAcctRepCode;

    private String fullName;

    private String title;

    private String firstName;

    private String middleInitial;

    private String lastName;

    private String legalBusinessName;

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountSubType() {
        return accountSubType;
    }

    public void setAccountSubType(String accountSubType) {
        this.accountSubType = accountSubType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }


    public String getStatusDateNew() {
		return statusDateNew;
	}

    @JsonProperty("statusDate")
	public void setStatusDateNew(String statusDateNew) {
		this.statusDateNew = statusDateNew;
	}

	
	public String getStartServiceDateNew() {
		return startServiceDateNew;
	}

	@JsonProperty("startServiceDate")
	public void setStartServiceDateNew(String startServiceDateNew) {
		this.startServiceDateNew = startServiceDateNew;
	}

	public String getStatusActivityCode() {
        return statusActivityCode;
    }

    public void setStatusActivityCode(String statusActivityCode) {
        this.statusActivityCode = statusActivityCode;
    }

    public String getStatusActivityReasonCode() {
        return statusActivityReasonCode;
    }

    public void setStatusActivityReasonCode(String statusActivityReasonCode) {
        this.statusActivityReasonCode = statusActivityReasonCode;
    }

    public String getDealerCode() {
        return dealerCode;
    }

    public void setDealerCode(String dealerCode) {
        this.dealerCode = dealerCode;
    }

    public String getSalesRepCode() {
        return salesRepCode;
    }

    public void setSalesRepCode(String salesRepCode) {
        this.salesRepCode = salesRepCode;
    }

    public String getCorpAcctRepCode() {
        return corpAcctRepCode;
    }

    public void setCorpAcctRepCode(String corpAcctRepCode) {
        this.corpAcctRepCode = corpAcctRepCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = StringUtils.upperCase(fullName);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = StringUtils.upperCase(firstName);
    }

    public String getMiddleInitial() {
        return middleInitial;
    }

    public void setMiddleInitial(String middleInitial) {
        this.middleInitial = StringUtils.upperCase(middleInitial);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = StringUtils.upperCase(lastName);
    }

    public String getLegalBusinessName() {
        return legalBusinessName;
    }

    public void setLegalBusinessName(String legalBusinessName) {
        this.legalBusinessName = legalBusinessName;
    }
    
    @Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
    
    @Exclude
    @JsonIgnore
    public String[] getNotNullFieldNames() {
    	List<String> fieldNames = new ArrayList<>(18);
    	if(ObjectUtils.isNotEmpty(this.accountType)) {
    		fieldNames.add("accountType");
    	}
    	if(ObjectUtils.isNotEmpty(this.accountSubType)) {
    		fieldNames.add("accountSubType");
    	}
    	if(ObjectUtils.isNotEmpty(this.status)) {
    		fieldNames.add("status");
    	}
    	if(ObjectUtils.isNotEmpty(this.brandId)) {
    		fieldNames.add("brandId");
    	}
    	if(ObjectUtils.isNotEmpty(this.language)) {
    		fieldNames.add("language");
    	}
    	if(ObjectUtils.isNotEmpty(this.statusDateNew)) {
    		fieldNames.add("statusDateNew");
    	}
    	if(ObjectUtils.isNotEmpty(this.startServiceDateNew)) {
    		fieldNames.add("startServiceDateNew");
    	}
    	if(ObjectUtils.isNotEmpty(this.statusActivityCode)) {
    		fieldNames.add("statusActivityCode");
    	}
    	if(ObjectUtils.isNotEmpty(this.statusActivityReasonCode)) {
    		fieldNames.add("statusActivityReasonCode");
    	}
    	if(ObjectUtils.isNotEmpty(this.dealerCode)) {
    		fieldNames.add("dealerCode");
    	}
    	if(ObjectUtils.isNotEmpty(this.salesRepCode)) {
    		fieldNames.add("salesRepCode");
    	}
    	if(ObjectUtils.isNotEmpty(this.corpAcctRepCode)) {
    		fieldNames.add("corpAcctRepCode");
    	}
    	if(ObjectUtils.isNotEmpty(this.fullName)) {
    		fieldNames.add("fullName");
    	}
    	if(ObjectUtils.isNotEmpty(this.title)) {
    		fieldNames.add("title");
    	}
    	if(ObjectUtils.isNotEmpty(this.firstName)) {
    		fieldNames.add("firstName");
    	}
    	if(ObjectUtils.isNotEmpty(this.middleInitial)) {
    		fieldNames.add("middleInitial");
    	}
    	if(ObjectUtils.isNotEmpty(this.lastName)) {
    		fieldNames.add("lastName");
    	}
    	if(ObjectUtils.isNotEmpty(this.legalBusinessName)) {
    		fieldNames.add("legalBusinessName");
    	}
    	return fieldNames.toArray(new String[fieldNames.size()]);
    }
}
