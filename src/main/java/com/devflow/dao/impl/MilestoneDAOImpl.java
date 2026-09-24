package com.devflow.dao.impl;

import com.devflow.config.DBConnection;
import com.devflow.dao.MilestoneDAO;
import com.devflow.exception.DatabaseException;
import com.devflow.model.Milestone;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MilestoneDAOImpl implements MilestoneDAO {
    private static final Logger logger = LoggerFactory.getLogger(MilestoneDAOImpl.class);

    private Milestone mapRow(ResultSet rs) throws SQLException {
        Milestone m = new Milestone();
        m.setId(rs.getInt("id"));
        m.setProjectId(rs.getInt("project_id"));
        m.setTitle(rs.getString("title"));
        m.setDescription(rs.getString("description"));
        m.setDueDate(rs.getDate("due_date"));
        m.setStatus(rs.getString("status"));
        m.setCreatedAt(rs.getTimestamp("created_at"));
        return m;
    }

    @Override
    public Milestone findById(int id) {
        String sql = "SELECT id, project_id, title, description, due_date, status, created_at FROM milestones WHERE id = ?";
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
            logger.error("Error finding milestone by id {}: {}", id, e.getMessage());
            throw new DatabaseException("Failed to find milestone", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return null;
    }

    @Override
    public List<Milestone> findByProjectId(int projectId) {
        String sql = "SELECT id, project_id, title, description, due_date, status, created_at FROM milestones WHERE project_id = ? ORDER BY due_date ASC";
        List<Milestone> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, projectId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            logger.error("Error finding milestones for project {}: {}", projectId, e.getMessage());
            throw new DatabaseException("Failed to retrieve milestones", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    @Override
    public boolean create(Milestone milestone) {
        String sql = "INSERT INTO milestones (project_id, title, description, due_date, status) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, milestone.getProjectId());
            ps.setString(2, milestone.getTitle());
            ps.setString(3, milestone.getDescription());
            ps.setDate(4, milestone.getDueDate());
            ps.setString(5, milestone.getStatus() != null ? milestone.getStatus() : "OPEN");

            int affected = ps.executeUpdate();
            if (affected > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    milestone.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            logger.error("Error creating milestone: {}", e.getMessage());
            throw new DatabaseException("Failed to create milestone", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return false;
    }

    @Override
    public boolean update(Milestone milestone) {
        String sql = "UPDATE milestones SET title = ?, description = ?, due_date = ?, status = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, milestone.getTitle());
            ps.setString(2, milestone.getDescription());
            ps.setDate(3, milestone.getDueDate());
            ps.setString(4, milestone.getStatus());
            ps.setInt(5, milestone.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating milestone {}: {}", milestone.getId(), e.getMessage());
            throw new DatabaseException("Failed to update milestone", e);
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public boolean updateStatus(int milestoneId, String status) {
        String sql = "UPDATE milestones SET status = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, milestoneId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating milestone status: {}", e.getMessage());
            throw new DatabaseException("Failed to update milestone status", e);
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public boolean delete(int milestoneId) {
        String sql = "DELETE FROM milestones WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, milestoneId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error deleting milestone: {}", e.getMessage());
            throw new DatabaseException("Failed to delete milestone", e);
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }
}
