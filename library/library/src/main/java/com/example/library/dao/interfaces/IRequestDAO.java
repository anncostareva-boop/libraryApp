package com.example.library.dao.interfaces;

import com.example.library.entity.Request;
import com.example.library.enums.RequestStatus;
import com.example.library.exception.DataAccessException;
import com.example.library.exception.DataNotFoundException;

import java.util.List;

public interface IRequestDAO {
    List<Request> showActiveUserRequests(Long userID) throws DataNotFoundException;
    List<Request> showActiveRequests() throws DataNotFoundException;

    Request findByID(Long requestID) throws DataNotFoundException;

    void create(Long userID, Long bookID) throws DataAccessException;

    void changeStatus(Long requestID, RequestStatus status) throws DataAccessException;

    void setReservedCopy(Long requestID, Long copyID) throws DataAccessException;
}
