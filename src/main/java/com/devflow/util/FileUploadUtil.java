package com.devflow.util;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.servlet.http.Part;

public class FileUploadUtil {
    private static final Set<String> ALLOWED_EXTENSIONS = new HashSet<>(Arrays.asList(
            "pdf", "doc", "docx", "ppt", "pptx", "xls", "xlsx",
            "txt", "md", "png", "jpg", "jpeg", "gif", "svg", "zip", "tar", "gz"
    ));

    private static final Set<String> DANGEROUS_EXTENSIONS = new HashSet<>(Arrays.asList(
            "exe", "bat", "cmd", "sh", "bin", "jsp", "jspx", "php", "asp", "aspx", "jar", "war", "py", "pl"
    ));

    public static boolean isAllowedExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) return false;
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        return !DANGEROUS_EXTENSIONS.contains(ext) && ALLOWED_EXTENSIONS.contains(ext);
    }

    public static String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        if (contentDisp != null) {
            String[] items = contentDisp.split(";");
            for (String s : items) {
                if (s.trim().startsWith("filename")) {
                    String fileName = s.substring(s.indexOf("=") + 2, s.length() - 1);
                    return new File(fileName).getName();
                }
            }
        }
        return "unnamed_" + System.currentTimeMillis();
    }

    public static String saveFile(Part part, String uploadDir) throws Exception {
        String originalName = extractFileName(part);
        if (!isAllowedExtension(originalName)) {
            throw new IllegalArgumentException("File type not permitted for security reasons.");
        }

        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String safeFileName = UUID.randomUUID().toString() + "_" + originalName.replaceAll("[^a-zA-Z0-9._-]", "_");
        File destFile = new File(dir, safeFileName);

        try (InputStream input = part.getInputStream()) {
            Files.copy(input, destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        return safeFileName;
    }
}
