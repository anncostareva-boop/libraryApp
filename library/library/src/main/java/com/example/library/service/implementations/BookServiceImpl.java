package com.example.library.service.implementations;

import com.example.library.dao.interfaces.IBookDAO;
import com.example.library.dao.interfaces.ICopyDAO;
import com.example.library.entity.Book;
import com.example.library.enums.BookStatus;
import com.example.library.exception.DataNotFoundException;
import com.example.library.service.interfaces.BookService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private final IBookDAO bookDAO;
    private final ICopyDAO copyDAO;

    public BookServiceImpl(IBookDAO bookDAO, ICopyDAO copyDAO) {
        this.bookDAO = bookDAO;
        this.copyDAO = copyDAO;
    }

    @Override
    public List<Book> getAllBooks() {
        return bookDAO.showAllBooks();
    }

    @Override
    public Book getBookByID(Long id) {
        return bookDAO.findByID(id);
    }

    @Override
    public List<Book> searchBook(String title, String author, String genre) {
        return bookDAO.searchBook(title, author, genre);
    }

    @Override
    public void updateBookStatus(Long bookID) {

        try {

            boolean hasAvailableCopies =
                    copyDAO.findAvailableCopy(bookID) != null;

            if (hasAvailableCopies) {

                bookDAO.changeBookStatus(
                        bookID,
                        BookStatus.IN_STOCK
                );

            } else {

                bookDAO.changeBookStatus(
                        bookID,
                        BookStatus.OUT_OF_STOCK
                );
            }

        } catch (DataNotFoundException e) {

            bookDAO.changeBookStatus(
                    bookID,
                    BookStatus.OUT_OF_STOCK
            );
        }
    }

}
