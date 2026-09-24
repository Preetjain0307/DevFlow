package com.devflow.dao.impl;

import com.devflow.config.DBConnection;
import com.devflow.dao.TaskDAO;
import com.devflow.exception.DatabaseException;
import com.devflow.model.Task;
import com.devflow.model.TaskComment;
import com.devflow.model.TaskHistory;
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

public class TaskDAOImpl implements TaskDAO {
    private static final Logger logger = LoggerFactory.getLogger(TaskDAOImpl.class);

    private static final String BASE_SELECT = 
            "SELECT t.id, t.project_id, p.project_key, p.name AS project_name, " +
            "t.sprint_id, s.sprint_name, t.milestone_id, m.title AS milestone_title, " +
            "t.title, t.description, t.task_type, t.priority, t.status, " +
            "t.created_by, u1.full_name AS creator_name, " +
            "t.assigned_to, u2.full_name AS assignee_name, " +
            "t.estimated_hours, t.logged_hours, t.due_date, t.created_at, t.updated_at " +
            "FROM tasks t " +
            "JOIN projects p ON t.project_id = p.id " +
            "LEFT JOIN sprints s ON t.sprint_id = s.id " +
            "LEFT JOIN milestones m ON t.milestone_id = m.id " +
            "JOIN users u1 ON t.created_by = u1.id " +
            "LEFT JOIN users u2 ON t.assigned_to = u2.id ";

    private Task mapRow(ResultSet rs) throws SQLException {
        Task t = new Task();
        t.setId(rs.getInt("id"));
        t.setProjectId(rs.getInt("project_id"));
        t.setProjectKey(rs.getString("project_key"));
        t.setProjectName(rs.getString("project_name"));
        
        int sprintId = rs.getInt("sprint_id");
        t.setSprintId(rs.wasNull() ? null : sprintId);
        t.setSprintName(rs.getString("sprint_name"));

        int milestoneId = rs.getInt("milestone_id");
        t.setMilestoneId(rs.wasNull() ? null : milestoneId);
        t.setMilestoneTitle(rs.getString("milestone_title"));

        t.setTitle(rs.getString("title"));
        t.setDescription(rs.getString("description"));
        t.setTaskType(rs.getString("task_type"));
        t.setPriority(rs.getString("priority"));
        t.setStatus(rs.getString("status"));
        t.setCreatedBy(rs.getInt("created_by"));
        t.setCreatorName(rs.getString("creator_name"));

        int assignedTo = rs.getInt("assigned_to");
        t.setAssignedTo(rs.wasNull() ? null : assignedTo);
        t.setAssigneeName(rs.getString("assignee_name"));

        t.setEstimatedHours(rs.getBigDecimal("estimated_hours"));
        t.setLoggedHours(rs.getBigDecimal("logged_hours"));
        t.setDueDate(rs.getDate("due_date"));
        t.setCreatedAt(rs.getTimestamp("created_at"));
        t.setUpdatedAt(rs.getTimestamp("updated_at"));
        return t;
    }

    @Override
    public Task findById(int id) {
        String sql = BASE_SELECT + "WHERE t.id = ?";
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
            logger.error("Error finding task by id {}: {}", id, e.getMessage());
            throw new DatabaseException("Failed to find task", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return null;
    }

    @Override
    public List<Task> findByProjectId(int projectId) {
        String sql = BASE_SELECT + "WHERE t.project_id = ? ORDER BY t.created_at DESC";
        List<Task> list = new ArrayList<>();
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
            logger.error("Error finding tasks for project {}: {}", projectId, e.getMessage());
            throw new DatabaseException("Failed to retrieve project tasks", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    @Override
    public List<Task> findBySprintId(int sprintId) {
        String sql = BASE_SELECT + "WHERE t.sprint_id = ? ORDER BY t.status ASC, t.priority DESC";
        List<Task> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, sprintId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            logger.error("Error finding tasks for sprint {}: {}", sprintId, e.getMessage());
            throw new DatabaseException("Failed to retrieve sprint tasks", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    @Override
    public List<Task> findByAssigneeId(int userId) {
        String sql = BASE_SELECT + "WHERE t.assigned_to = ? ORDER BY t.due_date ASC, t.priority DESC";
        List<Task> list = new ArrayList<>();
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
            logger.error("Error finding tasks for assignee {}: {}", userId, e.getMessage());
            throw new DatabaseException("Failed to retrieve assigned tasks", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    @Override
    public List<Task> search(Integer projectId, Integer sprintId, Integer assignedTo, String status, String priority, String keyword) {
        StringBuilder sb = new StringBuilder(BASE_SELECT);
        sb.append("WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        if (projectId != null && projectId > 0) {
            sb.append("AND t.project_id = ? ");
            params.add(projectId);
        }
        if (sprintId != null && sprintId > 0) {
            sb.append("AND t.sprint_id = ? ");
            params.add(sprintId);
        }
        if (assignedTo != null && assignedTo > 0) {
            sb.append("AND t.assigned_to = ? ");
            params.add(assignedTo);
        }
        if (status != null && !status.trim().isEmpty() && !"ALL".equalsIgnoreCase(status)) {
            sb.append("AND t.status = ? ");
            params.add(status.trim());
        }
        if (priority != null && !priority.trim().isEmpty() && !"ALL".equalsIgnoreCase(priority)) {
            sb.append("AND t.priority = ? ");
            params.add(priority.trim());
        }
        if (keyword != null && !keyword.trim().isEmpty()) {
            sb.append("AND (t.title LIKE ? OR t.description LIKE ?) ");
            String term = "%" + keyword.trim() + "%";
            params.add(term);
            params.add(term);
        }
        sb.append("ORDER BY t.created_at DESC");

        List<Task> list = new ArrayList<>();
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
            logger.error("Error searching tasks: {}", e.getMessage());
            throw new DatabaseException("Failed to search tasks", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    @Override
    public boolean create(Task task) {
        String sql = "INSERT INTO tasks (project_id, sprint_id, milestone_id, title, description, task_type, priority, status, created_by, assigned_to, estimated_hours, due_date) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, task.getProjectId());
            if (task.getSprintId() != null) ps.setInt(2, task.getSprintId()); else ps.setNull(2, Types.INTEGER);
            if (task.getMilestoneId() != null) ps.setInt(3, task.getMilestoneId()); else ps.setNull(3, Types.INTEGER);
            ps.setString(4, task.getTitle());
            ps.setString(5, task.getDescription());
            ps.setString(6, task.getTaskType() != null ? task.getTaskType() : "FEATURE");
            ps.setString(7, task.getPriority() != null ? task.getPriority() : "MEDIUM");
            ps.setString(8, task.getStatus() != null ? task.getStatus() : "TODO");
            ps.setInt(9, task.getCreatedBy());
            if (task.getAssignedTo() != null) ps.setInt(10, task.getAssignedTo()); else ps.setNull(10, Types.INTEGER);
            ps.setBigDecimal(11, task.getEstimatedHours());
            ps.setDate(12, task.getDueDate());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    task.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            logger.error("Error creating task: {}", e.getMessage());
            throw new DatabaseException("Failed to create task", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return false;
    }

    @Override
    public boolean update(Task task) {
        String sql = "UPDATE tasks SET sprint_id = ?, milestone_id = ?, title = ?, description = ?, task_type = ?, priority = ?, status = ?, assigned_to = ?, estimated_hours = ?, logged_hours = ?, due_date = ? " +
                     "WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            if (task.getSprintId() != null) ps.setInt(1, task.getSprintId()); else ps.setNull(1, Types.INTEGER);
            if (task.getMilestoneId() != null) ps.setInt(2, task.getMilestoneId()); else ps.setNull(2, Types.INTEGER);
            ps.setString(3, task.getTitle());
            ps.setString(4, task.getDescription());
            ps.setString(5, task.getTaskType());
            ps.setString(6, task.getPriority());
            ps.setString(7, task.getStatus());
            if (task.getAssignedTo() != null) ps.setInt(8, task.getAssignedTo()); else ps.setNull(8, Types.INTEGER);
            ps.setBigDecimal(9, task.getEstimatedHours());
            ps.setBigDecimal(10, task.getLoggedHours());
            ps.setDate(11, task.getDueDate());
            ps.setInt(12, task.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating task id {}: {}", task.getId(), e.getMessage());
            throw new DatabaseException("Failed to update task", e);
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public boolean updateStatus(int taskId, String status, int userId) {
        // Record status change in task and task_history
        Task existing = findById(taskId);
        if (existing == null) return false;
        String oldStatus = existing.getStatus();

        String sql = "UPDATE tasks SET status = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, taskId);
            ps.executeUpdate();

            // Insert history
            String histSql = "INSERT INTO task_history (task_id, user_id, field_changed, old_value, new_value) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement psHist = conn.prepareStatement(histSql)) {
                psHist.setInt(1, taskId);
                psHist.setInt(2, userId);
                psHist.setString(3, "status");
                psHist.setString(4, oldStatus);
                psHist.setString(5, status);
                psHist.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            DBConnection.rollback(conn);
            logger.error("Error updating task status {}: {}", taskId, e.getMessage());
            throw new DatabaseException("Failed to update task status", e);
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (Exception ignored) {}
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public boolean delete(int taskId) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, taskId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error deleting task {}: {}", taskId, e.getMessage());
            throw new DatabaseException("Failed to delete task", e);
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public boolean addComment(TaskComment comment) {
        String sql = "INSERT INTO task_comments (task_id, user_id, comment) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, comment.getTaskId());
            ps.setInt(2, comment.getUserId());
            ps.setString(3, comment.getComment());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error adding task comment: {}", e.getMessage());
            throw new DatabaseException("Failed to add comment", e);
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public List<TaskComment> findCommentsByTaskId(int taskId) {
        String sql = "SELECT tc.id, tc.task_id, tc.user_id, u.username, u.full_name, tc.comment, tc.created_at " +
                     "FROM task_comments tc " +
                     "JOIN users u ON tc.user_id = u.id " +
                     "WHERE tc.task_id = ? ORDER BY tc.created_at ASC";
        List<TaskComment> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, taskId);
            rs = ps.executeQuery();
            while (rs.next()) {
                TaskComment tc = new TaskComment();
                tc.setId(rs.getInt("id"));
                tc.setTaskId(rs.getInt("task_id"));
                tc.setUserId(rs.getInt("user_id"));
                tc.setUsername(rs.getString("username"));
                tc.setUserFullName(rs.getString("full_name"));
                tc.setComment(rs.getString("comment"));
                tc.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(tc);
            }
        } catch (SQLException e) {
            logger.error("Error finding comments for task {}: {}", taskId, e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    @Override
    public boolean addHistory(TaskHistory history) {
        String sql = "INSERT INTO task_history (task_id, user_id, field_changed, old_value, new_value) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, history.getTaskId());
            ps.setInt(2, history.getUserId());
            ps.setString(3, history.getFieldChanged());
            ps.setString(4, history.getOldValue());
            ps.setString(5, history.getNewValue());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public List<TaskHistory> findHistoryByTaskId(int taskId) {
        String sql = "SELECT th.id, th.task_id, th.user_id, u.username, u.full_name, th.field_changed, th.old_value, th.new_value, th.changed_at " +
                     "FROM task_history th " +
                     "JOIN users u ON th.user_id = u.id " +
                     "WHERE th.task_id = ? ORDER BY th.changed_at DESC";
        List<TaskHistory> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, taskId);
            rs = ps.executeQuery();
            while (rs.next()) {
                TaskHistory th = new TaskHistory();
                th.setId(rs.getInt("id"));
                th.setTaskId(rs.getInt("task_id"));
                th.setUserId(rs.getInt("user_id"));
                th.setUsername(rs.getString("username"));
                th.setUserFullName(rs.getString("full_name"));
                th.setFieldChanged(rs.getString("field_changed"));
                th.setOldValue(rs.getString("old_value"));
                th.setNewValue(rs.getString("new_value"));
                th.setChangedAt(rs.getTimestamp("changed_at"));
                list.add(th);
            }
        } catch (SQLException e) {
            logger.error("Error finding task history: {}", e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    @Override
    public int countTotalTasks() {
        String sql = "SELECT COUNT(*) FROM tasks";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            logger.error("Error counting tasks: {}", e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return 0;
    }

    @Override
    public int countTasksByAssignee(int userId) {
        String sql = "SELECT COUNT(*) FROM tasks WHERE assigned_to = ?";
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
            logger.error("Error counting assigned tasks: {}", e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return 0;
    }

    @Override
    public int countTasksByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM tasks WHERE status = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            logger.error("Error counting tasks by status: {}", e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return 0;
    }

    @Override
    public int countCompletedTasks() {
        return countTasksByStatus("COMPLETED");
    }

    @Override
    public Map<String, Integer> getStatusDistribution() {
        String sql = "SELECT status, COUNT(*) AS count FROM tasks GROUP BY status";
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("TODO", 0);
        map.put("IN_PROGRESS", 0);
        map.put("IN_REVIEW", 0);
        map.put("COMPLETED", 0);

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
            logger.error("Error getting status distribution: {}", e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return map;
    }

    @Override
    public Map<String, Integer> getStatusDistributionByProject(int projectId) {
        String sql = "SELECT status, COUNT(*) AS count FROM tasks WHERE project_id = ? GROUP BY status";
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("TODO", 0);
        map.put("IN_PROGRESS", 0);
        map.put("IN_REVIEW", 0);
        map.put("COMPLETED", 0);

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, projectId);
            rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("status"), rs.getInt("count"));
            }
        } catch (SQLException e) {
            logger.error("Error getting status distribution for project: {}", e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return map;
    }
}
