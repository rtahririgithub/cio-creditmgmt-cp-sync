package com.telus.credit.profile.sync.base.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.cloud.firestore.annotation.Exclude;
import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RiskLevelRiskAssessment {


    private Long id;

    private String assessmentMessageCd;
    private String assessmentMessageTxtEn;
    private String assessmentMessageTxtFr;
    
    @JsonProperty("@schemaLocation")
    private String schemaLocation;
    @JsonProperty("@type")
    private String type;
    @JsonProperty("@referredType")
    private String atReferredType;
    @JsonProperty("@baseType")
    private String atBaseType;
    private String name;
    private String description;
    private String href;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssessmentMessageCd() {
        return assessmentMessageCd;
    }

    public void setAssessmentMessageCd(String assessmentMessageCd) {
        this.assessmentMessageCd = assessmentMessageCd;
    }

    public String getAssessmentMessageTxtEn() {
        return assessmentMessageTxtEn;
    }

    public void setAssessmentMessageTxtEn(String assessmentMessage) {
        this.assessmentMessageTxtEn = assessmentMessage;
    }

    public String getAssessmentMessageTxtFr() {
        return assessmentMessageTxtFr;
    }

    public void setAssessmentMessageTxtFr(String assessmentMessage_fr) {
        this.assessmentMessageTxtFr = assessmentMessage_fr;
    }

    public String getSchemaLocation() {
        return schemaLocation;
    }

    public void setSchemaLocation(String schemaLocation) {
        this.schemaLocation = schemaLocation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    @Exclude
    @JsonIgnore
    public String[] getNotNullFieldNames() {
        List<String> fieldNames = new ArrayList<>(18);
        if(ObjectUtils.isNotEmpty(this.id)) {
            fieldNames.add("id");
        }
        if(ObjectUtils.isNotEmpty(this.assessmentMessageTxtEn)) {
            fieldNames.add("assessmentMessageTxtEn");
        }
        if(ObjectUtils.isNotEmpty(this.assessmentMessageTxtFr)) {
            fieldNames.add("assessmentMessageTxtFr");
        }
        if(ObjectUtils.isNotEmpty(this.name)) {
            fieldNames.add("name");
        }
        if(ObjectUtils.isNotEmpty(this.description)) {
            fieldNames.add("description");
        }
        if(ObjectUtils.isNotEmpty(this.type)) {
            fieldNames.add("type");
        }
        if(ObjectUtils.isNotEmpty(this.schemaLocation)) {
            fieldNames.add("schemaLocation");
        }
        if(ObjectUtils.isNotEmpty(this.atBaseType)) {
            fieldNames.add("atBaseType");
        }
        return fieldNames.toArray(new String[fieldNames.size()]);
    }
}
