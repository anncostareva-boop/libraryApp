package com.example.library.entity;

import com.example.library.enums.RequestStatus;

import java.sql.Date;

public class Request {
    private Long requestID;
    private Long userID;
    private Long bookID;
    private Long reservedCopyID;
    private Date createdAt;

    private RequestStatus status;

    public Request(){}
    public Request(Long requestID,
                   Long userID,
                   Long bookID,
                   Long reservedCopyID,
                   Date createdAt,
                   RequestStatus status) {

        this.requestID = requestID;
        this.userID = userID;
        this.bookID = bookID;
        this.reservedCopyID = reservedCopyID;
        this.createdAt = createdAt;
        this.status = status;
    }

    public Long getRequestID() { return requestID; }
    public void setRequestID(Long requestID) { this.requestID = requestID; }

    public Long getUserID() { return userID; }
    public void setUserID(Long userID) { this.userID = userID; }

    public Long getBookID() { return bookID; }
    public void setBookID(Long bookID) { this.bookID = bookID; }

    public Long getReservedCopyID() { return reservedCopyID; }
    public void setReservedCopyID(Long reservedCopyID) { this.reservedCopyID = reservedCopyID; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }
}
