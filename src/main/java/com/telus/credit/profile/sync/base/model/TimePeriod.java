package com.telus.credit.profile.sync.base.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class TimePeriod {
   private String endDateTime;
   private String startDateTime;


   public String getEndDateTime() {
      return endDateTime;
   }
   public void setEndDateTime(String endDateTime) {
      this.endDateTime = endDateTime;
   }
   public String getStartDateTime() {
      return startDateTime;
   }
   public void setStartDateTime(String startDateTime) {
      this.startDateTime = startDateTime;
   }


   @Override
   public String toString() {
      return new ToStringBuilder(this)
              .append("endDateTime", endDateTime)
              .append("startDateTime", startDateTime)
              .toString();
   }
}
