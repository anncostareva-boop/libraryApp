package com.example.library.form;

public class BookSearchForm {
    //книги можна шукати одночасно за всіма параметрами, кожен з яких може бути null;
    //якщо не заповнювати нічого, метод з DAO просто поверне список всіх книг

    private String title;
    private String author;
    private String genre;

    public BookSearchForm() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

}
