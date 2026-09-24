package com.devflow.dao;

import com.devflow.model.Document;
import java.util.List;

public interface DocumentDAO {
    Document findById(int id);
    List<Document> findByProjectId(int projectId);
    List<Document> search(Integer projectId, String category, String keyword);
    boolean create(Document document);
    boolean delete(int documentId);
    int countTotalDocuments();
}
