package com.example.library.dao.implementations;

import com.example.library.dao.interfaces.IRequestDAO;
import com.example.library.entity.Request;
import com.example.library.enums.RequestStatus;
import com.example.library.exception.DataAccessException;
import com.example.library.exception.DataNotFoundException;
import com.example.library.dao.interfaces.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RequestDAOImpl implements IRequestDAO {
    private final ConnectionManager cm;
    private Request mapRequest(
            ResultSet rs
    ) throws SQLException {
        return new Request(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getLong("book_id"),
                rs.getObject("copy_id", Long.class),
                rs.getDate("created_at"),
                RequestStatus.valueOf(rs.getString("status"))
        );
    }

    public RequestDAOImpl(ConnectionManager cm) {
        this.cm = cm;
    }

    @Override
    public List<Request> showActiveUserRequests(Long userID) throws DataNotFoundException {
        List<Request> list = new ArrayList<>();
        String sql = """
        SELECT *
        FROM borrow_requests
        WHERE user_id = ?
        AND status IN (?, ?)
        """;

        try (Connection con = cm.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setLong(1, userID);
            ps.setString(2,"PENDING");
            ps.setString(3, "AWAITING_ISSUANCE");

            ResultSet rs =
                    ps.executeQuery();

            while (rs.next()) {
                list.add(mapRequest(rs));
            }
        } catch (SQLException e) {
            throw new DataNotFoundException("Couldn't find active requests.");
        }
        return list;
    }

    @Override
    public List<Request> showActiveRequests() throws DataNotFoundException {
        List<Request> list = new ArrayList<>();
        String sql = """
        SELECT *
        FROM borrow_requests
        WHERE status IN (?, ?)
        """;

        try (Connection con = cm.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setString(1,"PENDING");
            ps.setString(2, "AWAITING_ISSUANCE");

            ResultSet rs =
                    ps.executeQuery();

            while (rs.next()) {
                list.add(mapRequest(rs));
            }
        } catch (SQLException e) {
            throw new DataNotFoundException("Couldn't find active requests.");
        }
        return list;
    }

    @Override
    public Request findByID(Long requestID) throws DataNotFoundException {
        String sql =
                "SELECT * FROM borrow_requests WHERE id=?";

        try (
                Connection con =
                        cm.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setLong(1, requestID);

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {
                return mapRequest(rs);
            }

        } catch (SQLException e) {

            throw new DataNotFoundException(
                    "Couldn't find request by ID.",
                    e
            );
        }
        return null;
    }

    @Override
    public void create(Long userID, Long bookID) throws DataAccessException {
        try(Connection con = cm.getConnection(false, 2)) {

            try (PreparedStatement ps = con.prepareStatement(
                    """
                        INSERT INTO borrow_requests
                        (user_id, book_id, created_at)
                        VALUES (?, ?, ?)
                        """)) {

                long now = System.currentTimeMillis();

                ps.setLong(1, userID);
                ps.setLong(2, bookID);
                ps.setDate(3, new java.sql.Date(now));
                ps.executeUpdate();

                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new DataAccessException("Couldn't create request.", e);
        }
    }

    @Override
    public void setReservedCopy(Long requestID, Long copyID)
            throws DataAccessException {

        String sql = """
        UPDATE borrow_requests
        SET copy_id = ?
        WHERE id = ?
    """;

        try (
                Connection con = cm.getConnection(false);
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setLong(1, copyID);
            ps.setLong(2, requestID);

            ps.executeUpdate();

            con.commit();

        } catch (SQLException e) {

            throw new DataAccessException(
                    "Couldn't reserve copy.",
                    e
            );
        }
    }

    @Override
    public void changeStatus(Long requestID, RequestStatus status) throws DataAccessException {
        String sql = """
            UPDATE borrow_requests
            SET status = ?
            WHERE id = ?
        """;

        try (
                Connection con =
                        cm.getConnection(false);

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setString(1, status.name());
            ps.setLong(2, requestID);
            ps.executeUpdate();

            con.commit();

        } catch (SQLException e) {

            throw new DataAccessException(
                    "Couldn't update request status.",
                    e
            );
        }
    }

}
