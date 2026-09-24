package com.devflow.dao.impl;

import com.devflow.config.DBConnection;
import com.devflow.dao.DocumentDAO;
import com.devflow.exception.DatabaseException;
import com.devflow.model.Document;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocumentDAOImpl implements DocumentDAO {
    private static final Logger logger = LoggerFactory.getLogger(DocumentDAOImpl.class);

    private static final String BASE_SELECT = 
            "SELECT d.id, d.project_id, p.project_key, p.name AS project_name, " +
            "d.title, d.description, d.category, d.file_name, d.file_path, " +
            "d.file_size, d.file_type, d.version, d.uploaded_by, u.full_name AS uploader_name, " +
            "d.created_at " +
            "FROM documents d " +
            "JOIN projects p ON d.project_id = p.id " +
            "JOIN users u ON d.uploaded_by = u.id ";

    private Document mapRow(ResultSet rs) throws SQLException {
        Document d = new Document();
        d.setId(rs.getInt("id"));
        d.setProjectId(rs.getInt("project_id"));
        d.setProjectKey(rs.getString("project_key"));
        d.setProjectName(rs.getString("project_name"));
        d.setTitle(rs.getString("title"));
        d.setDescription(rs.getString("description"));
        d.setCategory(rs.getString("category"));
        d.setFileName(rs.getString("file_name"));
        d.setFilePath(rs.getString("file_path"));
        d.setFileSize(rs.getLong("file_size"));
        d.setFileType(rs.getString("file_type"));
        d.setVersion(rs.getInt("version"));
        d.setUploadedBy(rs.getInt("uploaded_by"));
        d.setUploaderName(rs.getString("uploader_name"));
        d.setCreatedAt(rs.getTimestamp("created_at"));
        return d;
    }

    @Override
    public Document findById(int id) {
        String sql = BASE_SELECT + "WHERE d.id = ?";
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
            logger.error("Error finding document by id {}: {}", id, e.getMessage());
            throw new DatabaseException("Failed to find document", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return null;
    }

    @Override
    public List<Document> findByProjectId(int projectId) {
        String sql = BASE_SELECT + "WHERE d.project_id = ? ORDER BY d.created_at DESC";
        List<Document> list = new ArrayList<>();
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
            logger.error("Error finding documents for project {}: {}", projectId, e.getMessage());
            throw new DatabaseException("Failed to retrieve documents", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    @Override
    public List<Document> search(Integer projectId, String category, String keyword) {
        StringBuilder sb = new StringBuilder(BASE_SELECT);
        sb.append("WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        if (projectId != null && projectId > 0) {
            sb.append("AND d.project_id = ? ");
            params.add(projectId);
        }
        if (category != null && !category.trim().isEmpty() && !"ALL".equalsIgnoreCase(category)) {
            sb.append("AND d.category = ? ");
            params.add(category.trim());
        }
        if (keyword != null && !keyword.trim().isEmpty()) {
            sb.append("AND (d.title LIKE ? OR d.description LIKE ? OR d.file_name LIKE ?) ");
            String term = "%" + keyword.trim() + "%";
            params.add(term);
            params.add(term);
            params.add(term);
        }
        sb.append("ORDER BY d.created_at DESC");

        List<Document> list = new ArrayList<>();
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
            logger.error("Error searching documents: {}", e.getMessage());
            throw new DatabaseException("Failed to search documents", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    @Override
    public boolean create(Document document) {
        String sql = "INSERT INTO documents (project_id, title, description, category, file_name, file_path, file_size, file_type, version, uploaded_by) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, document.getProjectId());
            ps.setString(2, document.getTitle());
            ps.setString(3, document.getDescription());
            ps.setString(4, document.getCategory() != null ? document.getCategory() : "Other");
            ps.setString(5, document.getFileName());
            ps.setString(6, document.getFilePath());
            ps.setLong(7, document.getFileSize());
            ps.setString(8, document.getFileType());
            ps.setInt(9, document.getVersion() > 0 ? document.getVersion() : 1);
            ps.setInt(10, document.getUploadedBy());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    document.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            logger.error("Error creating document: {}", e.getMessage());
            throw new DatabaseException("Failed to save document metadata", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return false;
    }

    @Override
    public boolean delete(int documentId) {
        String sql = "DELETE FROM documents WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, documentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error deleting document {}: {}", documentId, e.getMessage());
            throw new DatabaseException("Failed to delete document", e);
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public int countTotalDocuments() {
        String sql = "SELECT COUNT(*) FROM documents";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            logger.error("Error counting documents: {}", e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return 0;
    }
}
