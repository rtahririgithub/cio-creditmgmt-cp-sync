package com.telus.credit.profile.sync.base.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.cloud.firestore.annotation.Exclude;

import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class Channel {

    String id;
    String href;
    String name;
    
    @JsonProperty("channelOrgId")    
    String channelOrgId;
    
     @JsonProperty("originatorAppId")    
    String originatorAppId;
    
    @JsonProperty("@type")
    private String type;
    @JsonProperty("@schemaLocation")
    private String schemaLocation;
    @JsonProperty("@baseType")
    private String atBaseType;

    private String userId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getChannelOrgId() {
        return channelOrgId;
    }

    public void setChannelOrgId(String channelOrgId) {
        this.channelOrgId = channelOrgId;
    }

    public String getOriginatorAppId() {
        return originatorAppId;
    }

    public void setOriginatorAppId(String originatorAppId) {
        this.originatorAppId = originatorAppId;
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

    public String getAtBaseType() {
        return atBaseType;
    }

    public void setAtBaseType(String atBaseType) {
        this.atBaseType = atBaseType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Exclude
    @JsonIgnore
    public String[] getNotNullFieldNames() {
        List<String> fieldNames = new ArrayList<>(18);
        if(ObjectUtils.isNotEmpty(this.id)) {
            fieldNames.add("id");
        }
        if(ObjectUtils.isNotEmpty(this.href)) {
            fieldNames.add("href");
        }
        if(ObjectUtils.isNotEmpty(this.name)) {
            fieldNames.add("name");
        }
        if(ObjectUtils.isNotEmpty(this.channelOrgId)) {
            fieldNames.add("channelOrgId");
        }
        if(ObjectUtils.isNotEmpty(this.originatorAppId)) {
            fieldNames.add("originatorAppId");
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
