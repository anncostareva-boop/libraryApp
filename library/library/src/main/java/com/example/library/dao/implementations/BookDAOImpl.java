package com.example.library.dao.implementations;


import com.example.library.dao.interfaces.ConnectionManager;
import com.example.library.dao.interfaces.IBookDAO;
import com.example.library.entity.Book;
import com.example.library.enums.BookStatus;
import com.example.library.exception.DataAccessException;
import com.example.library.exception.DataNotFoundException;
import org.springframework.stereotype.Repository;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;

public class BookDAOImpl implements IBookDAO {

    private final ConnectionManager cm;
    private Book mapBook(
            ResultSet rs
    ) throws SQLException {

        return new Book(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("description"),
                rs.getString("genre"),
                BookStatus.valueOf(rs.getString("status"))
        );
    }

    public BookDAOImpl(ConnectionManager cm) {
        this.cm = cm;
    }

    @Override
    public List<Book> showAllBooks() throws DataNotFoundException {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT * FROM books";

        try (Connection con = cm.getConnection();
            Statement st = con.createStatement()) {

            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                list.add(mapBook(rs));
            }
        } catch (SQLException e) {
            throw new DataNotFoundException("Couldn't find all books");
        }
        return list;
    }
    @Override
    public Book findByID(Long id) throws DataNotFoundException {
        String sql =
                "SELECT * FROM books WHERE id=?";

        try (Connection con = cm.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {

                return mapBook(rs);
            }

        } catch (SQLException e) {

            throw new DataNotFoundException(
                    "Couldn't find book by ID.",
                    e
            );
        }

        return null;
    }

    @Override
    public List<Book> searchBook(String title, String author, String genre)
            throws DataNotFoundException {

        List<Book> books = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        StringBuilder sql =
                new StringBuilder("SELECT DISTINCT * FROM books WHERE 1 = 1");

        if(title != null && !title.isBlank()) {

            sql.append(" AND title LIKE ? ");
            params.add("%" + title + "%");
        }

        if(author != null && !author.isBlank()) {

            sql.append(" AND author LIKE ? ");
            params.add("%" + author + "%");
        }

        if(genre != null && !genre.isBlank()) {

            sql.append(" AND genre LIKE ? ");
            params.add("%" + genre + "%");
        }

        try (
                Connection con = cm.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql.toString())
        ) {

            for(int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                books.add(mapBook(rs));
            }

        } catch (SQLException e) {

            throw new DataNotFoundException(
                    "Couldn't search book.",
                    e
            );
        }

        return books;
    }

    @Override
    public void changeBookStatus(Long bookID, BookStatus status) throws DataAccessException {
        try (Connection con = cm.getConnection(false, 2)) {
            String sql = """
            UPDATE books
            SET status = ?
            WHERE id = ?
        """;
            try {
                    PreparedStatement ps =
                            con.prepareStatement(sql);


                ps.setString(1, status.name());
                ps.setLong(2, bookID);
                ps.executeUpdate();

                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Couldn't change book status.", e);
        }
    }

}
