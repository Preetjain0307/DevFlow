package com.devflow.dao.impl;

import com.devflow.config.DBConnection;
import com.devflow.dao.GitHubDAO;
import com.devflow.exception.DatabaseException;
import com.devflow.model.GitHubActivity;
import com.devflow.model.GitHubRepo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GitHubDAOImpl implements GitHubDAO {
    private static final Logger logger = LoggerFactory.getLogger(GitHubDAOImpl.class);

    @Override
    public GitHubRepo findByProjectId(int projectId) {
        String sql = "SELECT gr.id, gr.project_id, p.project_key, p.name AS project_name, " +
                     "gr.repo_owner, gr.repo_name, gr.repo_url, gr.default_branch, gr.is_active, " +
                     "gr.last_synced_at, gr.created_at " +
                     "FROM github_repositories gr " +
                     "JOIN projects p ON gr.project_id = p.id " +
                     "WHERE gr.project_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, projectId);
            rs = ps.executeQuery();
            if (rs.next()) {
                GitHubRepo repo = new GitHubRepo();
                repo.setId(rs.getInt("id"));
                repo.setProjectId(rs.getInt("project_id"));
                repo.setProjectKey(rs.getString("project_key"));
                repo.setProjectName(rs.getString("project_name"));
                repo.setRepoOwner(rs.getString("repo_owner"));
                repo.setRepoName(rs.getString("repo_name"));
                repo.setRepoUrl(rs.getString("repo_url"));
                repo.setDefaultBranch(rs.getString("default_branch"));
                repo.setActive(rs.getBoolean("is_active"));
                repo.setLastSyncedAt(rs.getTimestamp("last_synced_at"));
                repo.setCreatedAt(rs.getTimestamp("created_at"));
                return repo;
            }
        } catch (SQLException e) {
            logger.error("Error finding GitHub repo for project {}: {}", projectId, e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return null;
    }

    @Override
    public boolean saveOrUpdateRepo(GitHubRepo repo) {
        String sql = "INSERT INTO github_repositories (project_id, repo_owner, repo_name, repo_url, default_branch, is_active, last_synced_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP) " +
                     "ON DUPLICATE KEY UPDATE repo_owner = VALUES(repo_owner), repo_name = VALUES(repo_name), " +
                     "repo_url = VALUES(repo_url), default_branch = VALUES(default_branch), is_active = VALUES(is_active), last_synced_at = CURRENT_TIMESTAMP";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, repo.getProjectId());
            ps.setString(2, repo.getRepoOwner());
            ps.setString(3, repo.getRepoName());
            ps.setString(4, repo.getRepoUrl());
            ps.setString(5, repo.getDefaultBranch() != null ? repo.getDefaultBranch() : "main");
            ps.setBoolean(6, repo.isActive());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error saving GitHub repo: {}", e.getMessage());
            throw new DatabaseException("Failed to save GitHub repo link", e);
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public boolean deleteRepo(int projectId) {
        String sql = "DELETE FROM github_repositories WHERE project_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, projectId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error deleting GitHub repo link: {}", e.getMessage());
            return false;
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public boolean addActivity(GitHubActivity activity) {
        String sql = "INSERT INTO github_activity (project_id, event_type, author_name, author_avatar, message, event_url, event_timestamp) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, activity.getProjectId());
            ps.setString(2, activity.getEventType());
            ps.setString(3, activity.getAuthorName());
            ps.setString(4, activity.getAuthorAvatar());
            ps.setString(5, activity.getMessage());
            ps.setString(6, activity.getEventUrl());
            ps.setTimestamp(7, activity.getEventTimestamp());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error adding GitHub activity: {}", e.getMessage());
            return false;
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public List<GitHubActivity> findActivityByProjectId(int projectId, int limit) {
        String sql = "SELECT id, project_id, event_type, author_name, author_avatar, message, event_url, event_timestamp, created_at " +
                     "FROM github_activity WHERE project_id = ? ORDER BY event_timestamp DESC LIMIT ?";
        List<GitHubActivity> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, projectId);
            ps.setInt(2, limit);
            rs = ps.executeQuery();
            while (rs.next()) {
                GitHubActivity ga = new GitHubActivity();
                ga.setId(rs.getInt("id"));
                ga.setProjectId(rs.getInt("project_id"));
                ga.setEventType(rs.getString("event_type"));
                ga.setAuthorName(rs.getString("author_name"));
                ga.setAuthorAvatar(rs.getString("author_avatar"));
                ga.setMessage(rs.getString("message"));
                ga.setEventUrl(rs.getString("event_url"));
                ga.setEventTimestamp(rs.getTimestamp("event_timestamp"));
                ga.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(ga);
            }
        } catch (SQLException e) {
            logger.error("Error finding GitHub activity: {}", e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    @Override
    public List<GitHubActivity> findRecentActivity(int limit) {
        String sql = "SELECT id, project_id, event_type, author_name, author_avatar, message, event_url, event_timestamp, created_at " +
                     "FROM github_activity ORDER BY event_timestamp DESC LIMIT ?";
        List<GitHubActivity> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, limit);
            rs = ps.executeQuery();
            while (rs.next()) {
                GitHubActivity ga = new GitHubActivity();
                ga.setId(rs.getInt("id"));
                ga.setProjectId(rs.getInt("project_id"));
                ga.setEventType(rs.getString("event_type"));
                ga.setAuthorName(rs.getString("author_name"));
                ga.setAuthorAvatar(rs.getString("author_avatar"));
                ga.setMessage(rs.getString("message"));
                ga.setEventUrl(rs.getString("event_url"));
                ga.setEventTimestamp(rs.getTimestamp("event_timestamp"));
                ga.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(ga);
            }
        } catch (SQLException e) {
            logger.error("Error finding recent GitHub activity: {}", e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }
}
