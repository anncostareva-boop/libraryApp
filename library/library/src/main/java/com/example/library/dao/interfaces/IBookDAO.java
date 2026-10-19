package com.example.library.dao.interfaces;

import com.example.library.entity.Book;
import com.example.library.enums.BookStatus;
import com.example.library.exception.DataAccessException;
import com.example.library.exception.DataNotFoundException;

import java.util.List;

public interface IBookDAO {

    List<Book> showAllBooks() throws DataNotFoundException;

    Book findByID(Long id) throws DataNotFoundException;
    List<Book> searchBook(String title, String author, String genre) throws DataNotFoundException;

    void changeBookStatus(Long bookID, BookStatus status) throws DataAccessException;
}
