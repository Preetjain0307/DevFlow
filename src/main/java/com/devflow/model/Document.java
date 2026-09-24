package com.devflow.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Document implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int projectId;
    private String projectKey;
    private String projectName;
    private String title;
    private String description;
    private String category; // Synopsis, SRS, UML, Reports, Presentation, Meeting Documents, Other
    private String fileName;
    private String filePath;
    private long fileSize;
    private String fileType;
    private int version;
    private int uploadedBy;
    private String uploaderName;
    private Timestamp createdAt;

    public Document() {
        this.version = 1;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getDocumentId() { return id; }
    public void setDocumentId(int docId) { this.id = docId; }

    public String getUploadedByName() { return uploaderName != null ? uploaderName : "User"; }
    public void setUploadedByName(String name) { this.uploaderName = name; }

    public int getProjectId() { return projectId; }
    public void setProjectId(int projectId) { this.projectId = projectId; }

    public String getProjectKey() { return projectKey; }
    public void setProjectKey(String projectKey) { this.projectKey = projectKey; }

    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public long getFileSize() { return fileSize; }
    public void setFileSize(long fileSize) { this.fileSize = fileSize; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public int getVersion() { return version; }
    public void setVersion(int version) { this.version = version; }

    public int getUploadedBy() { return uploadedBy; }
    public void setUploadedBy(int uploadedBy) { this.uploadedBy = uploadedBy; }

    public String getUploaderName() { return uploaderName; }
    public void setUploaderName(String uploaderName) { this.uploaderName = uploaderName; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public String getFormattedFileSize() {
        if (fileSize < 1024) return fileSize + " B";
        int exp = (int) (Math.log(fileSize) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.1f %sB", fileSize / Math.pow(1024, exp), pre);
    }
}
