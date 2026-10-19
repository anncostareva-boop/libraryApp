package com.example.library.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public class ReturnBookForm {

    @NotNull(message = "Введіть ID користувача")
    private Long userID;
    @NotNull(message = "Введіть ID копії що повертається")
    private Long copyID;
    private Boolean damaged;
    @PositiveOrZero(message = "Недійсне значення розміру штрафу")
    private BigDecimal fineAmount;

    public ReturnBookForm() { fineAmount = new BigDecimal(0); }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Long getCopyID() {
        return copyID;
    }

    public void setCopyID(Long copyID) {
        this.copyID = copyID;
    }

    public Boolean getDamaged() {
        return damaged;
    }

    public void setDamaged(Boolean damaged) {
        this.damaged = damaged;
    }

    public BigDecimal getFineAmount() { return fineAmount; }

    public void setFineAmount(BigDecimal fineAmount) { this.fineAmount = fineAmount; }
}
