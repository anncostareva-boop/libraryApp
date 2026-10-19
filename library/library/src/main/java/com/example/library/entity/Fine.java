package com.example.library.entity;

import com.example.library.enums.FineReason;

import java.math.BigDecimal;

public class Fine {

    private Long fineID;

    private Long userID;

    private BigDecimal amount;

    private Boolean paid;

    private FineReason reason;

    public Fine() {
    }

    public Fine(
            Long fineID,
            Long userID,
            BigDecimal amount,
            Boolean paid,
            FineReason reason
    ) {
        this.fineID = fineID;
        this.userID = userID;
        this.amount = amount;
        this.paid = paid;
        this.reason = reason;
    }

    public Long getFineID() {
        return fineID;
    }

    public void setFineID(Long fineID) {
        this.fineID = fineID;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public FineReason getReason() {
        return reason;
    }

    public void setReason(FineReason reason) {
        this.reason = reason;
    }
}