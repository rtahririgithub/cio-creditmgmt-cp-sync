package com.telus.credit.profile.sync.base.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class TelusIndividualIdentificationCharacteristic {

   private String provinceCd;
   private String identificationTypeCd;
   private String countryCd;

   public String getProvinceCd() {
      return provinceCd;
   }
   public void setProvinceCd(String provinceCd) {
      this.provinceCd = provinceCd;
   }
   public String getIdentificationTypeCd() {
      return identificationTypeCd;
   }
   public void setIdentificationTypeCd(String identificationTypeCd) {
      this.identificationTypeCd = identificationTypeCd;
   }

   public String getCountryCd() {
      return countryCd;
   }
   public void setCountryCd(String countryCd) {
      this.countryCd = countryCd;
   }

   @Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
