package com.example.library.service.interfaces;

import com.example.library.entity.Issuance;
import com.example.library.entity.IssuedBookInfo;

import java.util.List;

public interface IssuanceService {

    void issueBook(Long requestID);


    void returnBook(Long userID, Long copyID,  boolean isDamaged);


    List<IssuedBookInfo> getUserIssuedBooks(Long userID);

    Boolean hasOverdueBooks(Long userID);

    List<Issuance> getAllActiveIssuances();
}
