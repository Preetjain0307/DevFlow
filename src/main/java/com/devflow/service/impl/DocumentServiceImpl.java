package com.devflow.service.impl;

import com.devflow.config.Constants;
import com.devflow.dao.AuditLogDAO;
import com.devflow.dao.DocumentDAO;
import com.devflow.dao.NotificationDAO;
import com.devflow.dao.impl.AuditLogDAOImpl;
import com.devflow.dao.impl.DocumentDAOImpl;
import com.devflow.dao.impl.NotificationDAOImpl;
import com.devflow.exception.ValidationException;
import com.devflow.model.AuditLog;
import com.devflow.model.Document;
import com.devflow.service.DocumentService;
import com.devflow.util.FileUploadUtil;
import com.devflow.util.ValidationUtil;
import java.io.File;
import java.util.List;
import javax.servlet.http.Part;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocumentServiceImpl implements DocumentService {
    private static final Logger logger = LoggerFactory.getLogger(DocumentServiceImpl.class);

    private final DocumentDAO documentDAO;
    private final NotificationDAO notificationDAO;
    private final AuditLogDAO auditLogDAO;

    public DocumentServiceImpl() {
        this.documentDAO = new DocumentDAOImpl();
        this.notificationDAO = new NotificationDAOImpl();
        this.auditLogDAO = new AuditLogDAOImpl();
    }

    public DocumentServiceImpl(DocumentDAO documentDAO, NotificationDAO notificationDAO, AuditLogDAO auditLogDAO) {
        this.documentDAO = documentDAO;
        this.notificationDAO = notificationDAO;
        this.auditLogDAO = auditLogDAO;
    }

    @Override
    public Document getDocumentById(int id) {
        return documentDAO.findById(id);
    }

    @Override
    public List<Document> getDocumentsByProjectId(int projectId) {
        return documentDAO.findByProjectId(projectId);
    }

    @Override
    public List<Document> searchDocuments(Integer projectId, String category, String keyword) {
        return documentDAO.search(projectId, category, keyword);
    }

    @Override
    public boolean uploadDocument(Document document, Part filePart, String uploadRootPath, int userId, String username, String ipAddress) {
        if (!ValidationUtil.isNotEmpty(document.getTitle())) {
            throw new ValidationException("Document title is required.");
        }
        if (document.getProjectId() <= 0) {
            throw new ValidationException("Project is required.");
        }
        if (filePart == null || filePart.getSize() <= 0) {
            throw new ValidationException("A valid file must be selected for upload.");
        }

        try {
            String originalFileName = FileUploadUtil.extractFileName(filePart);
            if (!FileUploadUtil.isAllowedExtension(originalFileName)) {
                throw new ValidationException("File extension is not allowed for security reasons.");
            }

            // Save file to disk
            String savedFileName = FileUploadUtil.saveFile(filePart, uploadRootPath);
            document.setFileName(originalFileName);
            document.setFilePath(savedFileName);
            document.setFileSize(filePart.getSize());
            document.setFileType(filePart.getContentType());
            document.setUploadedBy(userId);

            boolean created = documentDAO.create(document);
            if (created) {
                auditLogDAO.log(new AuditLog(userId, username, Constants.AUDIT_UPLOAD_DOCUMENT, "DOCUMENT", document.getId(), "Uploaded document: " + document.getTitle() + " (" + originalFileName + ")", ipAddress));
                
                // Notify team members
                notificationDAO.notifyProjectMembers(document.getProjectId(), userId, "New Document Uploaded", username + " uploaded a new document: " + document.getTitle(), "/documents?action=list&projectId=" + document.getProjectId(), "DOCUMENT");
            }
            return created;
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error saving uploaded file: {}", e.getMessage(), e);
            throw new ValidationException("Failed to upload document: " + e.getMessage());
        }
    }

    @Override
    public boolean deleteDocument(int documentId, String uploadRootPath, int userId, String username, String ipAddress) {
        Document doc = documentDAO.findById(documentId);
        if (doc == null) return false;

        // Attempt to remove file from disk
        try {
            File diskFile = new File(uploadRootPath, doc.getFilePath());
            if (diskFile.exists()) {
                diskFile.delete();
            }
        } catch (Exception e) {
            logger.warn("Could not delete physical file: {}", e.getMessage());
        }

        boolean deleted = documentDAO.delete(documentId);
        if (deleted) {
            auditLogDAO.log(new AuditLog(userId, username, Constants.AUDIT_DELETE_DOCUMENT, "DOCUMENT", documentId, "Deleted document: " + doc.getTitle(), ipAddress));
        }
        return deleted;
    }
}
