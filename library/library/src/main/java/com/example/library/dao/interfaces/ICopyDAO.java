package com.example.library.dao.interfaces;

import com.example.library.enums.CopyStatus;
import com.example.library.entity.BookCopy;
import com.example.library.exception.DataAccessException;
import com.example.library.exception.DataNotFoundException;

import java.util.List;

public interface ICopyDAO {
    List<BookCopy> showCopies(Long bookID) throws DataNotFoundException;

    BookCopy findByID(Long copyID) throws DataNotFoundException;
    BookCopy findAvailableCopy(Long bookID) throws DataNotFoundException;

    void changeStatus(Long copyID, CopyStatus status) throws DataAccessException;
}
