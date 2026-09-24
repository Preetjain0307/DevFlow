package com.devflow.dao.impl;

import com.devflow.config.DBConnection;
import com.devflow.dao.BugDAO;
import com.devflow.exception.DatabaseException;
import com.devflow.model.Bug;
import com.devflow.model.BugComment;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BugDAOImpl implements BugDAO {
    private static final Logger logger = LoggerFactory.getLogger(BugDAOImpl.class);

    private static final String BASE_SELECT = 
            "SELECT b.id, b.project_id, p.project_key, p.name AS project_name, " +
            "b.task_id, t.title AS task_title, b.title, b.description, " +
            "b.steps_to_reproduce, b.expected_result, b.actual_result, " +
            "b.severity, b.priority, b.status, b.reported_by, u1.full_name AS reporter_name, " +
            "b.assigned_to, u2.full_name AS assignee_name, b.resolution_notes, " +
            "b.created_at, b.updated_at " +
            "FROM bugs b " +
            "JOIN projects p ON b.project_id = p.id " +
            "LEFT JOIN tasks t ON b.task_id = t.id " +
            "JOIN users u1 ON b.reported_by = u1.id " +
            "LEFT JOIN users u2 ON b.assigned_to = u2.id ";

    private Bug mapRow(ResultSet rs) throws SQLException {
        Bug b = new Bug();
        b.setId(rs.getInt("id"));
        b.setProjectId(rs.getInt("project_id"));
        b.setProjectKey(rs.getString("project_key"));
        b.setProjectName(rs.getString("project_name"));

        int taskId = rs.getInt("task_id");
        b.setTaskId(rs.wasNull() ? null : taskId);
        b.setTaskTitle(rs.getString("task_title"));

        b.setTitle(rs.getString("title"));
        b.setDescription(rs.getString("description"));
        b.setStepsToReproduce(rs.getString("steps_to_reproduce"));
        b.setExpectedResult(rs.getString("expected_result"));
        b.setActualResult(rs.getString("actual_result"));
        b.setSeverity(rs.getString("severity"));
        b.setPriority(rs.getString("priority"));
        b.setStatus(rs.getString("status"));
        b.setReportedBy(rs.getInt("reported_by"));
        b.setReporterName(rs.getString("reporter_name"));

        int assignedTo = rs.getInt("assigned_to");
        b.setAssignedTo(rs.wasNull() ? null : assignedTo);
        b.setAssigneeName(rs.getString("assignee_name"));

        b.setResolutionNotes(rs.getString("resolution_notes"));
        b.setCreatedAt(rs.getTimestamp("created_at"));
        b.setUpdatedAt(rs.getTimestamp("updated_at"));
        return b;
    }

    @Override
    public Bug findById(int id) {
        String sql = BASE_SELECT + "WHERE b.id = ?";
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
            logger.error("Error finding bug by id {}: {}", id, e.getMessage());
            throw new DatabaseException("Failed to find bug", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return null;
    }

    @Override
    public List<Bug> findByProjectId(int projectId) {
        String sql = BASE_SELECT + "WHERE b.project_id = ? ORDER BY b.created_at DESC";
        List<Bug> list = new ArrayList<>();
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
            logger.error("Error finding bugs for project {}: {}", projectId, e.getMessage());
            throw new DatabaseException("Failed to retrieve bugs", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    @Override
    public List<Bug> findByAssigneeId(int userId) {
        String sql = BASE_SELECT + "WHERE b.assigned_to = ? ORDER BY b.severity DESC, b.created_at DESC";
        List<Bug> list = new ArrayList<>();
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
            logger.error("Error finding assigned bugs {}: {}", userId, e.getMessage());
            throw new DatabaseException("Failed to retrieve assigned bugs", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    @Override
    public List<Bug> findByReporterId(int userId) {
        String sql = BASE_SELECT + "WHERE b.reported_by = ? ORDER BY b.created_at DESC";
        List<Bug> list = new ArrayList<>();
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
            logger.error("Error finding reported bugs: {}", e.getMessage());
            throw new DatabaseException("Failed to retrieve reported bugs", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    @Override
    public List<Bug> search(Integer projectId, Integer assignedTo, String severity, String status, String keyword) {
        StringBuilder sb = new StringBuilder(BASE_SELECT);
        sb.append("WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        if (projectId != null && projectId > 0) {
            sb.append("AND b.project_id = ? ");
            params.add(projectId);
        }
        if (assignedTo != null && assignedTo > 0) {
            sb.append("AND b.assigned_to = ? ");
            params.add(assignedTo);
        }
        if (severity != null && !severity.trim().isEmpty() && !"ALL".equalsIgnoreCase(severity)) {
            sb.append("AND b.severity = ? ");
            params.add(severity.trim());
        }
        if (status != null && !status.trim().isEmpty() && !"ALL".equalsIgnoreCase(status)) {
            sb.append("AND b.status = ? ");
            params.add(status.trim());
        }
        if (keyword != null && !keyword.trim().isEmpty()) {
            sb.append("AND (b.title LIKE ? OR b.description LIKE ? OR b.steps_to_reproduce LIKE ?) ");
            String term = "%" + keyword.trim() + "%";
            params.add(term);
            params.add(term);
            params.add(term);
        }
        sb.append("ORDER BY b.created_at DESC");

        List<Bug> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sb.toString());
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            logger.error("Error searching bugs: {}", e.getMessage());
            throw new DatabaseException("Failed to search bugs", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    @Override
    public boolean create(Bug bug) {
        String sql = "INSERT INTO bugs (project_id, task_id, title, description, steps_to_reproduce, expected_result, actual_result, severity, priority, status, reported_by, assigned_to) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, bug.getProjectId());
            if (bug.getTaskId() != null) ps.setInt(2, bug.getTaskId()); else ps.setNull(2, Types.INTEGER);
            ps.setString(3, bug.getTitle());
            ps.setString(4, bug.getDescription());
            ps.setString(5, bug.getStepsToReproduce());
            ps.setString(6, bug.getExpectedResult());
            ps.setString(7, bug.getActualResult());
            ps.setString(8, bug.getSeverity() != null ? bug.getSeverity() : "MEDIUM");
            ps.setString(9, bug.getPriority() != null ? bug.getPriority() : "MEDIUM");
            ps.setString(10, bug.getStatus() != null ? bug.getStatus() : "OPEN");
            ps.setInt(11, bug.getReportedBy());
            if (bug.getAssignedTo() != null) ps.setInt(12, bug.getAssignedTo()); else ps.setNull(12, Types.INTEGER);

            int affected = ps.executeUpdate();
            if (affected > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    bug.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            logger.error("Error creating bug: {}", e.getMessage());
            throw new DatabaseException("Failed to create bug", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return false;
    }

    @Override
    public boolean update(Bug bug) {
        String sql = "UPDATE bugs SET task_id = ?, title = ?, description = ?, steps_to_reproduce = ?, expected_result = ?, actual_result = ?, severity = ?, priority = ?, status = ?, assigned_to = ?, resolution_notes = ? " +
                     "WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            if (bug.getTaskId() != null) ps.setInt(1, bug.getTaskId()); else ps.setNull(1, Types.INTEGER);
            ps.setString(2, bug.getTitle());
            ps.setString(3, bug.getDescription());
            ps.setString(4, bug.getStepsToReproduce());
            ps.setString(5, bug.getExpectedResult());
            ps.setString(6, bug.getActualResult());
            ps.setString(7, bug.getSeverity());
            ps.setString(8, bug.getPriority());
            ps.setString(9, bug.getStatus());
            if (bug.getAssignedTo() != null) ps.setInt(10, bug.getAssignedTo()); else ps.setNull(10, Types.INTEGER);
            ps.setString(11, bug.getResolutionNotes());
            ps.setInt(12, bug.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating bug id {}: {}", bug.getId(), e.getMessage());
            throw new DatabaseException("Failed to update bug", e);
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public boolean updateStatus(int bugId, String status, String resolutionNotes) {
        String sql = "UPDATE bugs SET status = ?, resolution_notes = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setString(2, resolutionNotes);
            ps.setInt(3, bugId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating bug status: {}", e.getMessage());
            throw new DatabaseException("Failed to update bug status", e);
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public boolean delete(int bugId) {
        String sql = "DELETE FROM bugs WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, bugId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error deleting bug {}: {}", bugId, e.getMessage());
            throw new DatabaseException("Failed to delete bug", e);
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public boolean addComment(BugComment comment) {
        String sql = "INSERT INTO bug_comments (bug_id, user_id, comment) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, comment.getBugId());
            ps.setInt(2, comment.getUserId());
            ps.setString(3, comment.getComment());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error adding bug comment: {}", e.getMessage());
            throw new DatabaseException("Failed to add bug comment", e);
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public List<BugComment> findCommentsByBugId(int bugId) {
        String sql = "SELECT bc.id, bc.bug_id, bc.user_id, u.username, u.full_name, bc.comment, bc.created_at " +
                     "FROM bug_comments bc " +
                     "JOIN users u ON bc.user_id = u.id " +
                     "WHERE bc.bug_id = ? ORDER BY bc.created_at ASC";
        List<BugComment> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, bugId);
            rs = ps.executeQuery();
            while (rs.next()) {
                BugComment bc = new BugComment();
                bc.setId(rs.getInt("id"));
                bc.setBugId(rs.getInt("bug_id"));
                bc.setUserId(rs.getInt("user_id"));
                bc.setUsername(rs.getString("username"));
                bc.setUserFullName(rs.getString("full_name"));
                bc.setComment(rs.getString("comment"));
                bc.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(bc);
            }
        } catch (SQLException e) {
            logger.error("Error finding bug comments: {}", e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    @Override
    public int countTotalBugs() {
        String sql = "SELECT COUNT(*) FROM bugs";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            logger.error("Error counting bugs: {}", e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return 0;
    }

    @Override
    public int countOpenBugs() {
        String sql = "SELECT COUNT(*) FROM bugs WHERE status IN ('OPEN', 'ASSIGNED', 'IN_PROGRESS', 'REOPENED')";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            logger.error("Error counting open bugs: {}", e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return 0;
    }

    @Override
    public int countBugsByAssignee(int userId) {
        String sql = "SELECT COUNT(*) FROM bugs WHERE assigned_to = ? AND status IN ('OPEN', 'ASSIGNED', 'IN_PROGRESS', 'REOPENED')";
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
            logger.error("Error counting assigned bugs: {}", e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return 0;
    }

    @Override
    public int countCriticalBugs() {
        String sql = "SELECT COUNT(*) FROM bugs WHERE severity = 'CRITICAL' AND status IN ('OPEN', 'ASSIGNED', 'IN_PROGRESS', 'REOPENED')";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            logger.error("Error counting critical bugs: {}", e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return 0;
    }

    @Override
    public Map<String, Integer> getSeverityDistribution() {
        String sql = "SELECT severity, COUNT(*) AS count FROM bugs GROUP BY severity";
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("LOW", 0);
        map.put("MEDIUM", 0);
        map.put("HIGH", 0);
        map.put("CRITICAL", 0);

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("severity"), rs.getInt("count"));
            }
        } catch (SQLException e) {
            logger.error("Error getting bug severity distribution: {}", e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return map;
    }

    @Override
    public Map<String, Integer> getStatusDistribution() {
        String sql = "SELECT status, COUNT(*) AS count FROM bugs GROUP BY status";
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("OPEN", 0);
        map.put("ASSIGNED", 0);
        map.put("IN_PROGRESS", 0);
        map.put("RESOLVED", 0);
        map.put("REOPENED", 0);
        map.put("CLOSED", 0);

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("status"), rs.getInt("count"));
            }
        } catch (SQLException e) {
            logger.error("Error getting bug status distribution: {}", e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return map;
    }

    @Override
    public Map<String, Integer> getSeverityDistributionByProject(int projectId) {
        String sql = "SELECT severity, COUNT(*) AS count FROM bugs WHERE project_id = ? GROUP BY severity";
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("LOW", 0);
        map.put("MEDIUM", 0);
        map.put("HIGH", 0);
        map.put("CRITICAL", 0);

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, projectId);
            rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("severity"), rs.getInt("count"));
            }
        } catch (SQLException e) {
            logger.error("Error getting bug severity distribution for project: {}", e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return map;
    }
}
