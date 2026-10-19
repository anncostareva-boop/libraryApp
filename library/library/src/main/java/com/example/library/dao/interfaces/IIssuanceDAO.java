package com.example.library.dao.interfaces;

import com.example.library.entity.Issuance;
import com.example.library.entity.IssuedBookInfo;
import com.example.library.exception.DataAccessException;
import com.example.library.exception.DataNotFoundException;

import java.sql.Date;
import java.util.List;

public interface IIssuanceDAO {
    void registerIssuance(Issuance issuance) throws DataAccessException;
    void registerReturning(Long issuanceID) throws DataAccessException;

    Issuance findIssuance(Long userID, Long copyID) throws DataNotFoundException;
    Issuance findByID(Long issuanceID) throws DataNotFoundException;

    Boolean isOverdue(Long issuanceID) throws DataAccessException;
    List<IssuedBookInfo> showIssuedBookInfo(Long userID) throws DataNotFoundException;

    List<Issuance> findAllActive();
}
