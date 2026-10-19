package com.example.library.entity;

import com.example.library.enums.BookStatus;

import java.sql.Date;

public class IssuedBookInfo {
    private Long issuanceID;
    private Long copyID;
    private String title;
    private String author;
    private Date issuanceDate;
    private Date dueDate;
    private Boolean overdue;
    private BookStatus status;

    public IssuedBookInfo() {}
    public IssuedBookInfo(Long issuanceID,
                          Long copyID,
                          String title,
                          String author,
                          Date issuanceDate,
                          Date dueDate,
                          Boolean overdue,
                          BookStatus status) {

        this.issuanceID = issuanceID;
        this.copyID = copyID;
        this.title = title;
        this.author = author;
        this.issuanceDate = issuanceDate;
        this.dueDate = dueDate;
        this.overdue = overdue;
        this.status = status;
    }

    public Long getIssuanceID() { return issuanceID; }
    public void setIssuanceID(Long issuanceID) {
        this.issuanceID = issuanceID;
    }

    public Long getCopyID() {
        return copyID;
    }
    public void setCopyID(Long copyID) {
        this.copyID = copyID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getIssuanceDate() {
        return issuanceDate;
    }

    public void setIssuanceDate(Date issuanceDate) {
        this.issuanceDate = issuanceDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Boolean getOverdue() {
        return overdue;
    }

    public void setOverdue(Boolean overdue) {
        this.overdue = overdue;
    }

    public BookStatus getStatus() {
        return status;
    }
    public void setStatus(BookStatus status) {
        this.status = status;
    }
}
