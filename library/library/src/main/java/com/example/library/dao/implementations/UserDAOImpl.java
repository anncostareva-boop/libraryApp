package com.example.library.dao.implementations;

import com.example.library.dao.interfaces.IUserDAO;
import com.example.library.entity.User;
import com.example.library.enums.Role;
import com.example.library.exception.DataAccessException;
import com.example.library.exception.DataNotFoundException;
import com.example.library.dao.interfaces.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements IUserDAO {
    private final ConnectionManager cm;
    private User mapUser(
            ResultSet rs
    ) throws SQLException {

        return new User(
                rs.getLong("id"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("password"),
                Role.valueOf(rs.getString("role"))
        );
    }

    public UserDAOImpl(ConnectionManager cm) {
        this.cm = cm;
    }

    @Override
    public void addUser(User user) throws DataAccessException {
        try(Connection con = cm.getConnection(false, 2)) {

            try (PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO users (username, email, password, role) VALUES (?, ?, ?, ?)")) {

                ps.setString(1, user.getUserName());
                ps.setString(2, user.getEmail());
                ps.setString(3, user.getPassword());
                ps.setString(4, user.getRole().name());
                ps.executeUpdate();

                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }

        } catch (SQLException e) {
            e.printStackTrace();

            throw new DataAccessException(
                    e.getMessage(),
                    e
            );
        }
    }

    @Override
    public List<User> findAdmins() throws DataNotFoundException {
        List<User> admins = new ArrayList<>();

        String sql = """
        SELECT *
        FROM users
        WHERE role = 'ADMIN'
        """;
        try (
                Connection con = cm.getConnection();
                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                admins.add(mapUser(rs));
            }

        } catch (SQLException e) {

            throw new DataAccessException(
                    "Couldn't load admins.",
                    e
            );
        }
        return admins;
    }

    @Override
    public User findByID(Long userID) throws DataNotFoundException {
        String sql =
                "SELECT * FROM users WHERE id=?";

        try (
                Connection con =
                        cm.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setLong(1, userID);

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {

                return mapUser(rs);
            }

        } catch (SQLException e) {

            throw new DataNotFoundException(
                    "Couldn't find user by ID.",
                    e
            );
        }
        return null;
    }

    @Override
    public User findByUsername(String username) throws DataNotFoundException {
        String sql =
                "SELECT * FROM users WHERE username=?";

        try (
                Connection con =
                        cm.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setString(1, username);

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {

                return mapUser(rs);
            }

        } catch (SQLException e) {

            throw new DataNotFoundException(
                    "Couldn't find user by username.",
                    e
            );
        }
        return null;
    }

    @Override
    public User findByEmail(String email) {
        String sql =
                "SELECT * FROM users WHERE email=?";

        try (
                Connection con =
                        cm.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setString(1, email);

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {

                return mapUser(rs);
            }

        } catch (SQLException e) {

            throw new DataNotFoundException(
                    "Couldn't find user by gmail.",
                    e
            );
        }
        return null;
    }

    @Override
    public void editProfile(User user) throws DataAccessException {
        try (Connection con = cm.getConnection(false, 2)) {
            try (PreparedStatement ps = con.prepareStatement(
                    "UPDATE users SET username=?, email=? WHERE id=?")) {

                ps.setString(1, user.getUserName());
                ps.setString(2, user.getEmail());
                ps.setLong(3, user.getUserID());
                ps.executeUpdate();

                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Couldn't edit profile.", e);
        }
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();

        String sql = """
        SELECT *
        FROM users
        ORDER BY id
    """;

        try (
                Connection con = cm.getConnection();
                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                users.add(mapUser(rs));
            }

        } catch (SQLException e) {

            throw new DataAccessException(
                    "Couldn't load users.",
                    e
            );
        }

        return users;
    }

}
