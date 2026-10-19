package com.example.library.entity;

import java.sql.Date;

public class Issuance {
    private Long issuanceID;
    private Long userID;
    private Long copyID;
    private Date issuanceDate;
    private Date dueDate;
    private Date returningDate;
    private Long bookID; //додала це, щоб адмін міг перейти за посиланням на сторінку примірника
    public Issuance() {}

    public Issuance(Long id, Long userID, Long copyID, Date issuanceDate,
                    Date dueDate, Date returningDate) {
        this.issuanceID = id;
        this.userID = userID;
        this.copyID = copyID;
        this.issuanceDate = issuanceDate;
        this.dueDate = dueDate;
        this.returningDate = returningDate;
    }

    public Long getIssuanceID() { return issuanceID; }
    public void setIssuanceID(Long id) {
        this.issuanceID = id;
    }

    public Long getUserID() { return userID; }
    public void setUserID(Long userID) { this.userID = userID; }

    public Long getCopyID() { return copyID; }
    public void setCopyID(Long copyID) {
        this.copyID = copyID;
    }

    public Date getIssuanceDate() {return issuanceDate; }
    public void setIssuanceDate(Date issuanceDate) {
        this.issuanceDate = issuanceDate;
    }

    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getReturningDate() { return returningDate; }
    public void setReturningDate(Date returningDate) {
        this.returningDate = returningDate;
    }

    public Long getBookID() { return bookID; }
    public void setBookID(Long bookID) { this.bookID = bookID; }
}
