package com.devflow.dao.impl;

import com.devflow.config.DBConnection;
import com.devflow.dao.SprintDAO;
import com.devflow.exception.DatabaseException;
import com.devflow.model.Sprint;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SprintDAOImpl implements SprintDAO {
    private static final Logger logger = LoggerFactory.getLogger(SprintDAOImpl.class);

    private static final String BASE_SELECT = 
            "SELECT s.id, s.project_id, s.sprint_name, s.goal, s.start_date, s.end_date, s.status, s.created_at, " +
            "(SELECT COUNT(*) FROM tasks t WHERE t.sprint_id = s.id) AS total_tasks, " +
            "(SELECT COUNT(*) FROM tasks t WHERE t.sprint_id = s.id AND t.status = 'COMPLETED') AS completed_tasks " +
            "FROM sprints s ";

    private Sprint mapRow(ResultSet rs) throws SQLException {
        Sprint s = new Sprint();
        s.setId(rs.getInt("id"));
        s.setProjectId(rs.getInt("project_id"));
        s.setSprintName(rs.getString("sprint_name"));
        s.setGoal(rs.getString("goal"));
        s.setStartDate(rs.getDate("start_date"));
        s.setEndDate(rs.getDate("end_date"));
        s.setStatus(rs.getString("status"));
        s.setCreatedAt(rs.getTimestamp("created_at"));
        s.setTotalTasks(rs.getInt("total_tasks"));
        s.setCompletedTasks(rs.getInt("completed_tasks"));
        return s;
    }

    @Override
    public Sprint findById(int id) {
        String sql = BASE_SELECT + "WHERE s.id = ?";
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
            logger.error("Error finding sprint by id {}: {}", id, e.getMessage());
            throw new DatabaseException("Failed to find sprint", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return null;
    }

    @Override
    public List<Sprint> findByProjectId(int projectId) {
        String sql = BASE_SELECT + "WHERE s.project_id = ? ORDER BY s.start_date DESC";
        List<Sprint> list = new ArrayList<>();
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
            logger.error("Error finding sprints for project {}: {}", projectId, e.getMessage());
            throw new DatabaseException("Failed to retrieve sprints", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    @Override
    public Sprint findActiveSprintByProjectId(int projectId) {
        String sql = BASE_SELECT + "WHERE s.project_id = ? AND s.status = 'ACTIVE' LIMIT 1";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, projectId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            logger.error("Error finding active sprint: {}", e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return null;
    }

    @Override
    public boolean create(Sprint sprint) {
        String sql = "INSERT INTO sprints (project_id, sprint_name, goal, start_date, end_date, status) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, sprint.getProjectId());
            ps.setString(2, sprint.getSprintName());
            ps.setString(3, sprint.getGoal());
            ps.setDate(4, sprint.getStartDate());
            ps.setDate(5, sprint.getEndDate());
            ps.setString(6, sprint.getStatus() != null ? sprint.getStatus() : "PLANNING");

            int affected = ps.executeUpdate();
            if (affected > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    sprint.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            logger.error("Error creating sprint: {}", e.getMessage());
            throw new DatabaseException("Failed to create sprint", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return false;
    }

    @Override
    public boolean update(Sprint sprint) {
        String sql = "UPDATE sprints SET sprint_name = ?, goal = ?, start_date = ?, end_date = ?, status = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, sprint.getSprintName());
            ps.setString(2, sprint.getGoal());
            ps.setDate(3, sprint.getStartDate());
            ps.setDate(4, sprint.getEndDate());
            ps.setString(5, sprint.getStatus());
            ps.setInt(6, sprint.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating sprint {}: {}", sprint.getId(), e.getMessage());
            throw new DatabaseException("Failed to update sprint", e);
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public boolean updateStatus(int sprintId, String status) {
        String sql = "UPDATE sprints SET status = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, sprintId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating sprint status: {}", e.getMessage());
            throw new DatabaseException("Failed to update sprint status", e);
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public boolean delete(int sprintId) {
        String sql = "DELETE FROM sprints WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, sprintId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error deleting sprint {}: {}", sprintId, e.getMessage());
            throw new DatabaseException("Failed to delete sprint", e);
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }
}
