package com.example.library.service.interfaces;

import com.example.library.entity.BookCopy;

import java.util.List;

public interface CopyService {
    // показує копії конкретної книги
    List<BookCopy> showCopies(Long bookID);

}
