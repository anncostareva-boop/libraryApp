package com.example.library.entity;

import com.example.library.enums.BookStatus;

public class Book {
    protected Long bookID;
    protected String title;
    protected String author;
    protected String description;
    private BookStatus status;
    private String genre;

    public Book(
            Long bookID,
            String title,
            String author,
            String description,
            String genre,
            BookStatus status
    ) {
        this.bookID = bookID;
        this.title = title;
        this.author = author;
        this.description = description;
        this.genre = genre;
        this.status = status;
    }

    public Long getBookID() { return bookID; }
    public void setBookID(Long bookID) { this.bookID = bookID; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BookStatus getStatus() { return status; }
    public void setStatus(BookStatus status) { this.status = status; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
}
