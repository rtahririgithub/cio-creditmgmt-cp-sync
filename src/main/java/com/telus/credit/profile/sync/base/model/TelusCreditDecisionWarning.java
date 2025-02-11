package com.telus.credit.profile.sync.base.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class TelusCreditDecisionWarning {
  
	@JsonProperty("id")
   private String id;
  
	//private Long warningHistoryLegacyId;

   @NotBlank( message = "1116")
   private String warningCategoryCd;

   private String warningCd;

   private String warningTypeCd;

   private String warningItemTypeCd;

   @NotBlank( message = "1117")
   private String warningStatusCd;

   @NotBlank( message = "1118")
   

   private String warningDetectionTs;

   @Pattern(regexp = "(0)|(1)|(true)|(false)")
   private String resolvedPermanentlyInd;
   
   
   private Long approvalCreditAssessmentId;
  
   private String approvalTs;
   
   private Long approvalExternalId;
   //private String memoTypeCd;
   private TimePeriod validFor;

   public String getId() {
      return id;
   }
   public void setId(String id) {
      this.id = id;
   }
   public String getWarningCategoryCd() {
      return warningCategoryCd;
   }
   public void setWarningCategoryCd(String warningCategoryCd) {
      this.warningCategoryCd = warningCategoryCd;
   }

   public String getWarningCd() {
      return warningCd;
   }
   public void setWarningCd(String warningCd) {
      this.warningCd = warningCd;
   }

   public String getWarningTypeCd() {
      return warningTypeCd;
   }
   public void setWarningTypeCd(String warningTypeCd) {
      this.warningTypeCd = warningTypeCd;
   }

   public String getWarningItemTypeCd() {
      return warningItemTypeCd;
   }
   public void setWarningItemTypeCd(String warningItemTypeCd) {
      this.warningItemTypeCd = warningItemTypeCd;
   }

   public String getWarningDetectionTs() {
      return warningDetectionTs;
   }
   public void setWarningDetectionTs(String warningDetectionTs) {
      this.warningDetectionTs = warningDetectionTs;
   }
   public String getWarningStatusCd() {
      return warningStatusCd;
   }
   public void setWarningStatusCd(String warningStatusCd) {
      this.warningStatusCd = warningStatusCd;
   }

   public String getResolvedPermanentlyInd() {
      return resolvedPermanentlyInd;
   }
   public void setResolvedPermanentlyInd(String resolvedPermanentlyInd) {
      this.resolvedPermanentlyInd = resolvedPermanentlyInd;
   }
   public TimePeriod getValidFor() {
      return validFor;
   }
   public void setValidFor(TimePeriod validFor) {
      this.validFor = validFor;
   }
/*
   public Long getWarningHistoryLegacyId() {
      return warningHistoryLegacyId;
   }
   public void setWarningHistoryLegacyId(Long warningHistoryLegacyId) {
      this.warningHistoryLegacyId = warningHistoryLegacyId;
   }
   */
   public Long getApprovalCreditAssessmentId() {
      return approvalCreditAssessmentId;
   }
   public void setApprovalCreditAssessmentId(Long approvalCreditAssessmentId) {
      this.approvalCreditAssessmentId = approvalCreditAssessmentId;
   }
   public String getApprovalTs() {
      return approvalTs;
   }
   public void setApprovalTs(String approvalTs) {
      this.approvalTs = approvalTs;
   }
   public Long getApprovalExternalId() {
      return approvalExternalId;
   }
   public void setApprovalExternalId(Long approvalExternalId) {
      this.approvalExternalId = approvalExternalId;
   }
	/*
	 * public String getMemoTypeCd() { return memoTypeCd; } public void
	 * setMemoTypeCd(String memoTypeCd) { this.memoTypeCd = memoTypeCd; }
	 */

   @Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
