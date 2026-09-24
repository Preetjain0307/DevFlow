package com.devflow.service;

import com.devflow.model.Document;
import java.util.List;
import javax.servlet.http.Part;

public interface DocumentService {
    Document getDocumentById(int id);
    List<Document> getDocumentsByProjectId(int projectId);
    List<Document> searchDocuments(Integer projectId, String category, String keyword);
    boolean uploadDocument(Document document, Part filePart, String uploadRootPath, int userId, String username, String ipAddress);
    boolean deleteDocument(int documentId, String uploadRootPath, int userId, String username, String ipAddress);
}
