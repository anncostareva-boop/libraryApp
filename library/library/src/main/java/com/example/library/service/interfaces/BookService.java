package com.example.library.service.interfaces;

import com.example.library.entity.Book;
import com.example.library.enums.BookStatus;

import java.util.List;

public interface BookService {
    // метод для відображення всіх книг
    List<Book> getAllBooks();

    // метод для пошуку книги за айді
    Book getBookByID(Long id);

    // метод для пошуку одразу за трьома параметрами, кожен з яких може бути null
    List<Book> searchBook(String title, String author, String genre);

    // перевірка наявності доступних копій
    void updateBookStatus(Long bookID);

}
