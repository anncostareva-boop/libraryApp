package com.example.library.dao.implementations;

import com.example.library.dao.interfaces.ConnectionManager;
import com.example.library.dao.interfaces.IIssuanceDAO;
import com.example.library.entity.Issuance;
import com.example.library.entity.IssuedBookInfo;
import com.example.library.enums.BookStatus;
import com.example.library.exception.DataAccessException;
import com.example.library.exception.DataNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IssuanceDAOImpl implements IIssuanceDAO {
    private final ConnectionManager cm;
    private Issuance mapIssuance(
            ResultSet rs
    ) throws SQLException {
        return new Issuance(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getLong("copy_id"),
                rs.getDate("issuance_date"),
                rs.getDate("due_date"),
                rs.getDate("returning_date")
        );
    }

    public IssuanceDAOImpl(ConnectionManager cm) {
        this.cm = cm;
    }

    @Override
    public void registerIssuance(Issuance issuance) throws DataAccessException {
        try(Connection con = cm.getConnection(false, 2)) {
            String sql = """
                        INSERT INTO issuance_history
                        (user_id, copy_id, issuance_date, due_date)
                        VALUES (?, ?, ?, ?)
                        """;

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setLong(1, issuance.getUserID());
                ps.setLong(2, issuance.getCopyID());
                ps.setDate(3, issuance.getIssuanceDate());
                ps.setDate(4, issuance.getDueDate());
                ps.executeUpdate();

                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new DataAccessException("Couldn't register new issuance.", e);
        }
    }

    @Override
    public void registerReturning(Long issuanceID) throws DataAccessException {
        try (Connection con = cm.getConnection(false, 2)) {
            String sql = """
            UPDATE issuance_history
            SET returning_date = NOW()
            WHERE id = ?
        """;
            try {
                PreparedStatement ps =
                        con.prepareStatement(sql);

                ps.setLong(1, issuanceID);
                ps.executeUpdate();

                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new DataAccessException("Couldn't register returning.", e);
        }
    }


    @Override
    public Issuance findIssuance(Long userID, Long copyID) throws DataNotFoundException {
        String sql = """
        SELECT *
        FROM issuance_history
        WHERE user_id = ?
        AND copy_id = ?
        AND returning_date IS NULL
        """;

        try (Connection con = cm.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setLong(1, userID);
            ps.setLong(2, copyID);

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {
                return mapIssuance(rs);
            }
        } catch (SQLException e) {
            throw new DataNotFoundException("Couldn't find issuance.");
        }
        return null;
    }

    @Override
    public Issuance findByID(Long issuanceID) throws DataNotFoundException {
        String sql =
                "SELECT * FROM issuance_history WHERE id=?";

        try (
                Connection con =
                        cm.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setLong(1, issuanceID);

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {
                return mapIssuance(rs);
            }

        } catch (SQLException e) {

            throw new DataNotFoundException(
                    "Couldn't find issuance by ID.",
                    e
            );
        }
        return null;
    }

    @Override
    public Boolean isOverdue(Long issuanceID) throws DataAccessException {
        String sql = """
        SELECT due_date
        FROM issuance_history
        WHERE id = ?
        """;

        try (Connection con = cm.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, issuanceID);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Date dueDate = rs.getDate("due_date");

                if (dueDate == null) {
                    return false;
                }

                return dueDate.before(new java.util.Date());
            }

            return false;

        } catch (SQLException e) {
            throw new DataAccessException("Couldn't check overdue.", e);
        }
    }
    @Override
    public List<IssuedBookInfo> showIssuedBookInfo(Long userID) throws DataNotFoundException {

        List<IssuedBookInfo> result =
                new ArrayList<>();

        String sql = """
        SELECT
            ih.id AS issuance_id,
            ih.copy_id,
            b.title,
            b.author,
            ih.issuance_date,
            ih.due_date,
            bc.status
        FROM issuance_history ih
        JOIN book_copies bc ON ih.copy_id = bc.id
        JOIN books b ON bc.book_id = b.id
        WHERE ih.user_id = ?
          AND ih.returning_date IS NULL
        """;

        try (
                Connection con =
                        cm.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setLong(1, userID);

            ResultSet rs =
                    ps.executeQuery();

            while (rs.next()) {

                Date dueDate = rs.getDate("due_date");

                boolean overdue =
                        dueDate.before(new java.util.Date());

                result.add(
                        new IssuedBookInfo(
                                rs.getLong("issuance_id"),
                                rs.getLong("copy_id"),
                                rs.getString("title"),
                                rs.getString("author"),
                                rs.getDate("issuance_date"),
                                dueDate,
                                overdue,
                                BookStatus.valueOf(rs.getString("status"))
                        )
                );
            }

        } catch (SQLException e) {

            throw new DataNotFoundException(
                    "Couldn't show issued books.",
                    e
            );
        }

        return result;
    }

    @Override
    public List<Issuance> findAllActive() {
        List<Issuance> issuances = new ArrayList<>();

        // Робимо JOIN з таблицею копій, щоб дізнатися ID книги
        String sql = """
    SELECT ih.*, bc.book_id 
    FROM issuance_history ih
    JOIN book_copies bc ON ih.copy_id = bc.id
    WHERE ih.returning_date IS NULL
    ORDER BY ih.issuance_date DESC
    """;

        try (Connection con = cm.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Issuance issuance = mapIssuance(rs);
                // Записуємо знайдений book_id в об'єкт
                issuance.setBookID(rs.getLong("book_id"));
                issuances.add(issuance);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return issuances;
    }

}
