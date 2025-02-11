package com.telus.credit.profile.sync.base.model;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

// Used for the attachment size
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Size {

    private BigDecimal amount;
    private String units;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

}
