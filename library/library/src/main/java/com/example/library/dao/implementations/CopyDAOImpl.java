package com.example.library.dao.implementations;

import com.example.library.dao.interfaces.ICopyDAO;
import com.example.library.entity.BookCopy;
import com.example.library.enums.CopyStatus;
import com.example.library.exception.DataAccessException;
import com.example.library.exception.DataNotFoundException;
import com.example.library.dao.interfaces.ConnectionManager;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CopyDAOImpl implements ICopyDAO {
    private final ConnectionManager cm;

    public CopyDAOImpl(ConnectionManager cm) {
        this.cm = cm;
    }

    private BookCopy mapBookCopy(
            ResultSet rs
    ) throws SQLException {

        return new BookCopy(
                rs.getLong("id"),
                rs.getLong("book_id"),
                CopyStatus.valueOf(
                        rs.getString("status")
                )
        );
    }

    @Override
    public List<BookCopy> showCopies(Long bookID) throws DataNotFoundException {
        List<BookCopy> list = new ArrayList<>();
        String sql = "SELECT * FROM book_copies WHERE book_id = ?";

        try (Connection con = cm.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setLong(1, bookID);

            ResultSet rs =
                    ps.executeQuery();

            while (rs.next()) {
                list.add(mapBookCopy(rs));
            }
        } catch (SQLException e) {
            throw new DataNotFoundException("Couldn't find book by genre");
        }
        return list;
    }

    @Override
    public BookCopy findByID(Long id)
            throws DataNotFoundException {

        String sql =
                "SELECT * FROM book_copies WHERE id=?";

        try (
                Connection con =
                        cm.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setLong(1, id);

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {

                return mapBookCopy(rs);
            }

        } catch (SQLException e) {

            throw new DataNotFoundException(
                    "Couldn't find copy by ID.",
                    e
            );
        }

        return null;
    }

    @Override
    public BookCopy findAvailableCopy(Long bookID)
            throws DataNotFoundException {

        String sql =
                """
                 SELECT * FROM book_copies
                 WHERE book_id=?
                 AND status = 'IN_STOCK'
                 LIMIT 1""";

        try (
                Connection con =
                        cm.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setLong(1, bookID);

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {
                return mapBookCopy(rs);
            }

        } catch (SQLException e) {

            throw new DataNotFoundException(
                    "Couldn't find book by book ID.",
                    e
            );
        }
        return null;
    }

    @Override
    public void changeStatus(Long copyID, CopyStatus status) throws DataAccessException {
        String sql = """
            UPDATE book_copies
            SET status = ?
            WHERE id = ?
        """;

        try (Connection con = cm.getConnection(false)) {

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, status.name());
                ps.setLong(2, copyID);
                ps.executeUpdate();

                con.commit(); // Фіксуємо зміни в базі даних
            } catch (SQLException e) {
                con.rollback(); // Якщо виникла помилка в запиті — відкочуємо назад
                throw e;
            }

        } catch (SQLException e) {
            throw new DataAccessException("Couldn't update copy status.", e);
        }
    }

}
