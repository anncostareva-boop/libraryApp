package com.example.library.entity;

import com.example.library.enums.CopyStatus;

public class BookCopy {

    private Long copyID;
    private Long bookID;
    private CopyStatus status;

    public BookCopy() {}
    public BookCopy(Long copyID, Long bookID, CopyStatus status) {
        this.copyID = copyID;
        this.bookID = bookID;
        this.status = status;
    }

    public Long getCopyID() { return copyID; }
    public void setCopyID(Long copyID) { this.copyID = copyID; }

    public Long getBookID() { return bookID; }
    public void setBookID(Long bookID) { this.bookID = bookID; }

    public CopyStatus getStatus() { return status; }
    public void setStatus(CopyStatus status) { this.status = status; }
}
