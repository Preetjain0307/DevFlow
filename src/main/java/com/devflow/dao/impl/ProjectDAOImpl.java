package com.devflow.dao.impl;

import com.devflow.config.DBConnection;
import com.devflow.dao.ProjectDAO;
import com.devflow.exception.DatabaseException;
import com.devflow.model.Project;
import com.devflow.model.ProjectMember;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProjectDAOImpl implements ProjectDAO {
    private static final Logger logger = LoggerFactory.getLogger(ProjectDAOImpl.class);

    private static final String BASE_SELECT = 
            "SELECT p.id, p.project_key, p.name, p.description, p.status, p.priority, " +
            "p.manager_id, u.full_name AS manager_name, p.start_date, p.end_date, " +
            "p.created_at, p.updated_at, " +
            "(SELECT COUNT(*) FROM tasks t WHERE t.project_id = p.id) AS total_tasks, " +
            "(SELECT COUNT(*) FROM tasks t WHERE t.project_id = p.id AND t.status = 'COMPLETED') AS completed_tasks, " +
            "(SELECT COUNT(*) FROM bugs b WHERE b.project_id = p.id AND b.status IN ('OPEN', 'ASSIGNED', 'IN_PROGRESS', 'REOPENED')) AS open_bugs, " +
            "(SELECT COUNT(*) FROM project_members pm WHERE pm.project_id = p.id) AS member_count " +
            "FROM projects p " +
            "JOIN users u ON p.manager_id = u.id ";

    private Project mapRow(ResultSet rs) throws SQLException {
        Project p = new Project();
        p.setId(rs.getInt("id"));
        p.setProjectKey(rs.getString("project_key"));
        p.setName(rs.getString("name"));
        p.setDescription(rs.getString("description"));
        p.setStatus(rs.getString("status"));
        p.setPriority(rs.getString("priority"));
        p.setManagerId(rs.getInt("manager_id"));
        p.setManagerName(rs.getString("manager_name"));
        p.setStartDate(rs.getDate("start_date"));
        p.setEndDate(rs.getDate("end_date"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        p.setUpdatedAt(rs.getTimestamp("updated_at"));
        p.setTotalTasks(rs.getInt("total_tasks"));
        p.setCompletedTasks(rs.getInt("completed_tasks"));
        p.setOpenBugs(rs.getInt("open_bugs"));
        p.setMemberCount(rs.getInt("member_count"));
        return p;
    }

    @Override
    public Project findById(int id) {
        String sql = BASE_SELECT + "WHERE p.id = ?";
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
            logger.error("Error finding project by id {}: {}", id, e.getMessage());
            throw new DatabaseException("Failed to find project", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return null;
    }

    @Override
    public Project findByKey(String key) {
        String sql = BASE_SELECT + "WHERE p.project_key = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, key);
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            logger.error("Error finding project by key {}: {}", key, e.getMessage());
            throw new DatabaseException("Failed to find project by key", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return null;
    }

    @Override
    public List<Project> findAll() {
        String sql = BASE_SELECT + "ORDER BY p.created_at DESC";
        List<Project> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            logger.error("Error finding all projects: {}", e.getMessage());
            throw new DatabaseException("Failed to retrieve projects", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    @Override
    public List<Project> findByUserId(int userId) {
        String sql = BASE_SELECT + 
                "WHERE p.manager_id = ? OR p.id IN (SELECT project_id FROM project_members WHERE user_id = ?) " +
                "ORDER BY p.created_at DESC";
        List<Project> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            logger.error("Error finding projects for user {}: {}", userId, e.getMessage());
            throw new DatabaseException("Failed to retrieve user projects", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    @Override
    public List<Project> findByManagerId(int managerId) {
        String sql = BASE_SELECT + "WHERE p.manager_id = ? ORDER BY p.created_at DESC";
        List<Project> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, managerId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            logger.error("Error finding projects for manager {}: {}", managerId, e.getMessage());
            throw new DatabaseException("Failed to retrieve manager projects", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    @Override
    public List<Project> search(String keyword, String status, String priority) {
        StringBuilder sb = new StringBuilder(BASE_SELECT);
        sb.append("WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sb.append("AND (p.name LIKE ? OR p.project_key LIKE ? OR p.description LIKE ?) ");
            String term = "%" + keyword.trim() + "%";
            params.add(term);
            params.add(term);
            params.add(term);
        }
        if (status != null && !status.trim().isEmpty() && !"ALL".equalsIgnoreCase(status)) {
            sb.append("AND p.status = ? ");
            params.add(status.trim());
        }
        if (priority != null && !priority.trim().isEmpty() && !"ALL".equalsIgnoreCase(priority)) {
            sb.append("AND p.priority = ? ");
            params.add(priority.trim());
        }
        sb.append("ORDER BY p.created_at DESC");

        List<Project> list = new ArrayList<>();
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
            logger.error("Error searching projects: {}", e.getMessage());
            throw new DatabaseException("Failed to search projects", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    @Override
    public boolean create(Project project) {
        String sql = "INSERT INTO projects (project_key, name, description, status, priority, manager_id, start_date, end_date) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, project.getProjectKey().toUpperCase());
            ps.setString(2, project.getName());
            ps.setString(3, project.getDescription());
            ps.setString(4, project.getStatus() != null ? project.getStatus() : "PLANNING");
            ps.setString(5, project.getPriority() != null ? project.getPriority() : "MEDIUM");
            ps.setInt(6, project.getManagerId());
            ps.setDate(7, project.getStartDate());
            ps.setDate(8, project.getEndDate());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    project.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            logger.error("Error creating project: {}", e.getMessage());
            throw new DatabaseException("Failed to create project", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return false;
    }

    @Override
    public boolean update(Project project) {
        String sql = "UPDATE projects SET name = ?, description = ?, status = ?, priority = ?, manager_id = ?, start_date = ?, end_date = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, project.getName());
            ps.setString(2, project.getDescription());
            ps.setString(3, project.getStatus());
            ps.setString(4, project.getPriority());
            ps.setInt(5, project.getManagerId());
            ps.setDate(6, project.getStartDate());
            ps.setDate(7, project.getEndDate());
            ps.setInt(8, project.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating project id {}: {}", project.getId(), e.getMessage());
            throw new DatabaseException("Failed to update project", e);
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM projects WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error deleting project id {}: {}", id, e.getMessage());
            throw new DatabaseException("Failed to delete project", e);
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE projects SET status = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating status for project id {}: {}", id, e.getMessage());
            throw new DatabaseException("Failed to update project status", e);
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public boolean addMember(int projectId, int userId, String projectRole) {
        String sql = "INSERT INTO project_members (project_id, user_id, project_role) VALUES (?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE project_role = VALUES(project_role)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, projectId);
            ps.setInt(2, userId);
            ps.setString(3, projectRole != null ? projectRole : "DEVELOPER");
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error adding member to project {}: {}", projectId, e.getMessage());
            throw new DatabaseException("Failed to add project member", e);
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public boolean removeMember(int projectId, int userId) {
        String sql = "DELETE FROM project_members WHERE project_id = ? AND user_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, projectId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error removing member from project {}: {}", projectId, e.getMessage());
            throw new DatabaseException("Failed to remove project member", e);
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public boolean updateMemberRole(int projectId, int userId, String projectRole) {
        String sql = "UPDATE project_members SET project_role = ? WHERE project_id = ? AND user_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, projectRole);
            ps.setInt(2, projectId);
            ps.setInt(3, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating member role: {}", e.getMessage());
            throw new DatabaseException("Failed to update member role", e);
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public List<ProjectMember> findMembersByProjectId(int projectId) {
        String sql = "SELECT pm.id, pm.project_id, p.name AS project_name, pm.user_id, u.username, " +
                     "u.full_name, u.email, r.name AS user_role_name, pm.project_role, pm.joined_at " +
                     "FROM project_members pm " +
                     "JOIN projects p ON pm.project_id = p.id " +
                     "JOIN users u ON pm.user_id = u.id " +
                     "JOIN roles r ON u.role_id = r.id " +
                     "WHERE pm.project_id = ? " +
                     "ORDER BY pm.joined_at ASC";
        List<ProjectMember> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, projectId);
            rs = ps.executeQuery();
            while (rs.next()) {
                ProjectMember pm = new ProjectMember();
                pm.setId(rs.getInt("id"));
                pm.setProjectId(rs.getInt("project_id"));
                pm.setProjectName(rs.getString("project_name"));
                pm.setUserId(rs.getInt("user_id"));
                pm.setUsername(rs.getString("username"));
                pm.setFullName(rs.getString("full_name"));
                pm.setEmail(rs.getString("email"));
                pm.setUserRoleName(rs.getString("user_role_name"));
                pm.setProjectRole(rs.getString("project_role"));
                pm.setJoinedAt(rs.getTimestamp("joined_at"));
                list.add(pm);
            }
        } catch (SQLException e) {
            logger.error("Error finding members for project {}: {}", projectId, e.getMessage());
            throw new DatabaseException("Failed to retrieve project members", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    @Override
    public ProjectMember findMember(int projectId, int userId) {
        String sql = "SELECT pm.id, pm.project_id, p.name AS project_name, pm.user_id, u.username, " +
                     "u.full_name, u.email, r.name AS user_role_name, pm.project_role, pm.joined_at " +
                     "FROM project_members pm " +
                     "JOIN projects p ON pm.project_id = p.id " +
                     "JOIN users u ON pm.user_id = u.id " +
                     "JOIN roles r ON u.role_id = r.id " +
                     "WHERE pm.project_id = ? AND pm.user_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, projectId);
            ps.setInt(2, userId);
            rs = ps.executeQuery();
            if (rs.next()) {
                ProjectMember pm = new ProjectMember();
                pm.setId(rs.getInt("id"));
                pm.setProjectId(rs.getInt("project_id"));
                pm.setProjectName(rs.getString("project_name"));
                pm.setUserId(rs.getInt("user_id"));
                pm.setUsername(rs.getString("username"));
                pm.setFullName(rs.getString("full_name"));
                pm.setEmail(rs.getString("email"));
                pm.setUserRoleName(rs.getString("user_role_name"));
                pm.setProjectRole(rs.getString("project_role"));
                pm.setJoinedAt(rs.getTimestamp("joined_at"));
                return pm;
            }
        } catch (SQLException e) {
            logger.error("Error finding project member: {}", e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return null;
    }

    @Override
    public boolean isUserInProject(int projectId, int userId) {
        String sql = "SELECT 1 FROM projects p " +
                     "LEFT JOIN project_members pm ON p.id = pm.project_id " +
                     "WHERE p.id = ? AND (p.manager_id = ? OR pm.user_id = ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, projectId);
            ps.setInt(2, userId);
            ps.setInt(3, userId);
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        } finally {
            DBConnection.close(conn, ps, rs);
        }
    }

    @Override
    public int countTotalProjects() {
        String sql = "SELECT COUNT(*) FROM projects";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            logger.error("Error counting projects: {}", e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return 0;
    }

    @Override
    public int countActiveProjects() {
        String sql = "SELECT COUNT(*) FROM projects WHERE status = 'ACTIVE'";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            logger.error("Error counting active projects: {}", e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return 0;
    }
}
