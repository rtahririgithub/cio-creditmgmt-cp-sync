package com.telus.credit.profile.sync.base.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class TelusOrganizationIdentificationCharacteristic {
   private String identificationTypeCd;

   public String getIdentificationTypeCd() {
      return identificationTypeCd;
   }
   public void setIdentificationTypeCd(String identificationTypeCd) {
      this.identificationTypeCd = identificationTypeCd;
   }
   
   @Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
