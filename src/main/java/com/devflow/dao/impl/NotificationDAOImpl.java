package com.devflow.dao.impl;

import com.devflow.config.DBConnection;
import com.devflow.dao.NotificationDAO;
import com.devflow.exception.DatabaseException;
import com.devflow.model.Notification;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotificationDAOImpl implements NotificationDAO {
    private static final Logger logger = LoggerFactory.getLogger(NotificationDAOImpl.class);

    private static final String BASE_SELECT = 
            "SELECT n.id, n.user_id, n.project_id, p.project_key, " +
            "n.title, n.message, n.link_url, n.notification_type, n.is_read, n.created_at " +
            "FROM notifications n " +
            "LEFT JOIN projects p ON n.project_id = p.id ";

    private Notification mapRow(ResultSet rs) throws SQLException {
        Notification n = new Notification();
        n.setId(rs.getInt("id"));
        n.setUserId(rs.getInt("user_id"));
        int pid = rs.getInt("project_id");
        n.setProjectId(rs.wasNull() ? null : pid);
        n.setProjectKey(rs.getString("project_key"));
        n.setTitle(rs.getString("title"));
        n.setMessage(rs.getString("message"));
        n.setLinkUrl(rs.getString("link_url"));
        n.setNotificationType(rs.getString("notification_type"));
        n.setRead(rs.getBoolean("is_read"));
        n.setCreatedAt(rs.getTimestamp("created_at"));
        return n;
    }

    @Override
    public Notification findById(int id) {
        String sql = BASE_SELECT + "WHERE n.id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            logger.error("Error finding notification by id: {}", e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return null;
    }

    @Override
    public List<Notification> findByUserId(int userId, int limit) {
        String sql = BASE_SELECT + "WHERE n.user_id = ? ORDER BY n.created_at DESC LIMIT ?";
        List<Notification> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, limit);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            logger.error("Error finding notifications for user {}: {}", userId, e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    @Override
    public List<Notification> findUnreadByUserId(int userId) {
        String sql = BASE_SELECT + "WHERE n.user_id = ? AND n.is_read = FALSE ORDER BY n.created_at DESC";
        List<Notification> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            logger.error("Error finding unread notifications: {}", e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    @Override
    public int countUnreadByUserId(int userId) {
        String sql = "SELECT COUNT(*) FROM notifications WHERE user_id = ? AND is_read = FALSE";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            logger.error("Error counting unread notifications: {}", e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return 0;
    }

    @Override
    public boolean create(Notification notification) {
        String sql = "INSERT INTO notifications (user_id, project_id, title, message, link_url, notification_type, is_read) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, notification.getUserId());
            if (notification.getProjectId() != null) ps.setInt(2, notification.getProjectId()); else ps.setNull(2, Types.INTEGER);
            ps.setString(3, notification.getTitle());
            ps.setString(4, notification.getMessage());
            ps.setString(5, notification.getLinkUrl());
            ps.setString(6, notification.getNotificationType() != null ? notification.getNotificationType() : "GENERAL");
            ps.setBoolean(7, notification.isRead());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    notification.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            logger.error("Error creating notification: {}", e.getMessage());
            throw new DatabaseException("Failed to save notification", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return false;
    }

    @Override
    public boolean markAsRead(int notificationId, int userId) {
        String sql = "UPDATE notifications SET is_read = TRUE WHERE id = ? AND user_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, notificationId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public boolean markAllAsRead(int userId) {
        String sql = "UPDATE notifications SET is_read = TRUE WHERE user_id = ? AND is_read = FALSE";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public boolean delete(int notificationId, int userId) {
        String sql = "DELETE FROM notifications WHERE id = ? AND user_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, notificationId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public boolean notifyProjectMembers(int projectId, int excludeUserId, String title, String message, String linkUrl, String type) {
        String memberSql = "SELECT user_id FROM project_members WHERE project_id = ? " +
                           "UNION SELECT manager_id FROM projects WHERE id = ?";
        Connection conn = null;
        PreparedStatement psMembers = null;
        PreparedStatement psInsert = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            psMembers = conn.prepareStatement(memberSql);
            psMembers.setInt(1, projectId);
            psMembers.setInt(2, projectId);
            rs = psMembers.executeQuery();

            String insertSql = "INSERT INTO notifications (user_id, project_id, title, message, link_url, notification_type, is_read) VALUES (?, ?, ?, ?, ?, ?, FALSE)";
            psInsert = conn.prepareStatement(insertSql);

            while (rs.next()) {
                int uid = rs.getInt(1);
                if (uid != excludeUserId) {
                    psInsert.setInt(1, uid);
                    psInsert.setInt(2, projectId);
                    psInsert.setString(3, title);
                    psInsert.setString(4, message);
                    psInsert.setString(5, linkUrl);
                    psInsert.setString(6, type != null ? type : "GENERAL");
                    psInsert.addBatch();
                }
            }
            psInsert.executeBatch();
            return true;
        } catch (SQLException e) {
            logger.error("Error notifying project members: {}", e.getMessage());
            return false;
        } finally {
            DBConnection.close(null, psInsert, null);
            DBConnection.close(conn, psMembers, rs);
        }
    }
}
