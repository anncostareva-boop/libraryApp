package com.example.library.dao.implementations;

import com.example.library.dao.interfaces.INotificationDAO;
import com.example.library.entity.Notification;
import com.example.library.exception.DataAccessException;
import com.example.library.exception.DataNotFoundException;
import com.example.library.dao.interfaces.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAOImpl implements INotificationDAO {
    private final ConnectionManager cm;
    private Notification mapNotification(
            ResultSet rs
    ) throws SQLException {
        Notification n = new Notification(
                rs.getLong("id"),
                rs.getLong("receiver_id"),
                rs.getString("title"),
                rs.getString("message"),
                rs.getDate("time")
        );
        // Насильно записуємо статус прочитано/непрочитано з бази в об'єкт
        n.setRead(rs.getBoolean("is_read"));

        return n;
    }

    public NotificationDAOImpl(ConnectionManager cm) {
        this.cm = cm;
    }

    @Override
    public List<Notification> showNotifications(Long userID) throws DataNotFoundException {

        List<Notification> list = new ArrayList<>();

        String sql = "SELECT * FROM notifications WHERE receiver_id = ? ORDER BY time DESC, id DESC";

        try (Connection con = cm.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, userID);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapNotification(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataNotFoundException("Database error while loading notifications");
        }

        return list;
    }

    @Override
    public void createNotification(Notification n) throws DataAccessException {
        try(Connection con = cm.getConnection(false)) {

            try (PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO notifications (receiver_id, title, message, time) VALUES (?, ?, ?, ?)")) {

                ps.setLong(1, n.getReceiverID());
                ps.setString(2, n.getTitle());
                ps.setString(3, n.getMessage());
                ps.setDate(4, n.getCreatedAt());
                ps.executeUpdate();

                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new DataAccessException("Couldn't create notification.", e);
        }
    }

    @Override
    public void markAsRead(Long notificationID) {
        String sql = """
            UPDATE notifications
            SET is_read = 1
            WHERE id = ?
        """;

        try (
                Connection con =
                        cm.getConnection(false);

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {
            ps.setLong(1, notificationID);
            ps.executeUpdate();

            con.commit();
        } catch (SQLException e) {

            throw new DataAccessException(
                    "Couldn't mark notification as read.",
                    e
            );
        }
    }
}
