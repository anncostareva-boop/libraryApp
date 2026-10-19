package com.example.library.dao.implementations;

import com.example.library.dao.interfaces.ConnectionManager;
import com.example.library.dao.interfaces.IFineDAO;
import com.example.library.entity.Fine;
import com.example.library.enums.FineReason;
import com.example.library.exception.DataAccessException;
import com.example.library.exception.DataNotFoundException;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FineDAOImpl implements IFineDAO {

    private final ConnectionManager cm;

    public FineDAOImpl(ConnectionManager cm) {
        this.cm = cm;
    }

    private Fine mapFine(ResultSet rs) throws SQLException {

        return new Fine(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getBigDecimal("amount"),
                rs.getBoolean("paid"),
                FineReason.valueOf(rs.getString("reason"))
        );
    }

    @Override
    public List<Fine> showUserFines(Long userID)
            throws DataNotFoundException {

        List<Fine> list =
                new ArrayList<>();

        String sql = """
            SELECT *
            FROM fines
            WHERE user_id = ?
            AND paid = FALSE
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

                list.add(
                        mapFine(rs)
                );
            }

        } catch (SQLException e) {

            throw new DataNotFoundException(
                    "Couldn't find fines.",
                    e
            );
        }

        return list;
    }

    @Override
    public void addFine(Fine fine)
            throws DataAccessException {

        String sql = """
            INSERT INTO fines
            (user_id, amount, reason)
            VALUES (?, ?, ?)
        """;

        try (
                Connection con =
                        cm.getConnection(false);

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setLong(1, fine.getUserID());

            ps.setBigDecimal(
                    2,
                    fine.getAmount()
            );

            ps.setString(
                    3,
                    fine.getReason().name()
            );

            ps.executeUpdate();

            con.commit();

        } catch (SQLException e) {

            throw new DataAccessException(
                    "Couldn't add fine.",
                    e
            );
        }
    }

    @Override
    public void payFine(Long fineID)
            throws DataAccessException {

        String sql = """
            UPDATE fines
            SET paid = TRUE
            WHERE id = ?
        """;

        try (
                Connection con =
                        cm.getConnection(false);

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setLong(1, fineID);

            ps.executeUpdate();

            con.commit();

        } catch (SQLException e) {

            throw new DataAccessException(
                    "Couldn't update fine status.",
                    e
            );
        }
    }

    @Override
    public List<Fine> findAll() {

        List<Fine> fines =
                new ArrayList<>();

        String sql = """
            SELECT *
            FROM fines
            WHERE paid IS FALSE
        """;

        try (
                Connection con =
                        cm.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ResultSet rs =
                    ps.executeQuery();

            while (rs.next()) {

                fines.add(
                        mapFine(rs)
                );
            }

        } catch (SQLException e) {

            throw new DataAccessException(
                    "Couldn't load fines.",
                    e
            );
        }

        return fines;
    }
}