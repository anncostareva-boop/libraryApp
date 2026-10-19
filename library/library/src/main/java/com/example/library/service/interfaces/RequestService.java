package com.example.library.service.interfaces;

import com.example.library.entity.Request;

import java.util.List;

public interface RequestService {
    // показує всі активні запити
    List<Request> getActiveRequests();

    //показує активні запити користувача
    List<Request> getUserActiveRequests(Long userID);

    void createRequest(Long userID, Long bookID) throws Exception;

    void approveRequest(Long requestID) throws Exception;

    void rejectRequest(Long requestID) throws Exception;
}
