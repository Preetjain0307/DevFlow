package com.devflow.controller;

import com.devflow.config.Constants;
import com.devflow.model.Document;
import com.devflow.model.User;
import com.devflow.service.DocumentService;
import com.devflow.service.ProjectService;
import com.devflow.service.impl.DocumentServiceImpl;
import com.devflow.service.impl.ProjectServiceImpl;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

@WebServlet(name = "DocumentController", urlPatterns = {"/documents", "/document/download", "/document/delete"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
        maxFileSize = 1024 * 1024 * 25,       // 25MB
        maxRequestSize = 1024 * 1024 * 50     // 50MB
)
public class DocumentController extends HttpServlet {
    private DocumentService documentService;
    private ProjectService projectService;

    @Override
    public void init() throws ServletException {
        this.documentService = new DocumentServiceImpl();
        this.projectService = new ProjectServiceImpl();
    }

    private String getUploadRootPath() {
        String appPath = getServletContext().getRealPath("");
        String uploadDir = appPath + File.separator + "uploads";
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return uploadDir;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();

        if ("/document/download".equals(servletPath)) {
            handleDownload(request, response);
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "upload":
                showUploadForm(request, response);
                break;
            case "list":
            default:
                listDocuments(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();

        if ("/document/delete".equals(servletPath)) {
            handleDelete(request, response);
            return;
        }

        String action = request.getParameter("action");
        if ("upload".equals(action)) {
            handleUpload(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/documents");
        }
    }

    private void listDocuments(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);

        String projectIdStr = request.getParameter("projectId");
        String category = request.getParameter("category");
        String keyword = request.getParameter("keyword");

        Integer projectId = (projectIdStr != null && !projectIdStr.isEmpty()) ? Integer.parseInt(projectIdStr) : null;

        List<Document> docs = documentService.searchDocuments(projectId, category, keyword);

        request.setAttribute("documents", docs);
        request.setAttribute("projects", projectService.getUserProjects(currentUser.getId(), currentUser.getRoleName()));
        request.setAttribute("selectedProjectId", projectId);
        request.setAttribute("selectedCategory", category);
        request.setAttribute("keyword", keyword);

        request.getRequestDispatcher("/WEB-INF/views/document/list.jsp").forward(request, response);
    }

    private void showUploadForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);

        String projectIdStr = request.getParameter("projectId");
        int projectId = (projectIdStr != null && !projectIdStr.isEmpty()) ? Integer.parseInt(projectIdStr) : 1;

        request.setAttribute("projects", projectService.getUserProjects(currentUser.getId(), currentUser.getRoleName()));
        request.setAttribute("selectedProjectId", projectId);

        request.getRequestDispatcher("/WEB-INF/views/document/upload.jsp").forward(request, response);
    }

    private void handleUpload(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);

        String projectIdStr = request.getParameter("projectId");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String category = request.getParameter("category");
        Part filePart = request.getPart("file");

        Document doc = new Document();
        doc.setProjectId(Integer.parseInt(projectIdStr));
        doc.setTitle(title);
        doc.setDescription(description);
        doc.setCategory(category != null ? category : "Other");

        try {
            boolean uploaded = documentService.uploadDocument(doc, filePart, getUploadRootPath(), currentUser.getId(), currentUser.getUsername(), request.getRemoteAddr());
            if (uploaded) {
                response.sendRedirect(request.getContextPath() + "/documents?projectId=" + doc.getProjectId());
                return;
            }
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
        }

        showUploadForm(request, response);
    }

    private void handleDownload(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Document doc = documentService.getDocumentById(id);
        if (doc == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Document not found.");
            return;
        }

        File file = new File(getUploadRootPath(), doc.getFilePath());
        if (!file.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Physical file not found on server storage.");
            return;
        }

        String mimeType = doc.getFileType();
        if (mimeType == null || mimeType.isEmpty()) {
            mimeType = "application/octet-stream";
        }

        response.setContentType(mimeType);
        response.setContentLengthLong(file.length());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + doc.getFileName() + "\"");

        try (FileInputStream in = new FileInputStream(file);
             OutputStream out = response.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();
        }
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);

        int id = Integer.parseInt(request.getParameter("id"));
        int projectId = Integer.parseInt(request.getParameter("projectId"));

        documentService.deleteDocument(id, getUploadRootPath(), currentUser.getId(), currentUser.getUsername(), request.getRemoteAddr());
        response.sendRedirect(request.getContextPath() + "/documents?projectId=" + projectId);
    }
}
