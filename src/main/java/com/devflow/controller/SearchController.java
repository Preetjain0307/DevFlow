package com.devflow.controller;

import com.devflow.config.DBConnection;
import com.devflow.util.JsonUtil;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "SearchController", urlPatterns = {"/api/search"})
public class SearchController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String query = request.getParameter("q");
        if (query == null) query = "";
        query = query.trim();

        List<Map<String, Object>> results = new ArrayList<>();
        if (query.length() < 2) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("results", results);
            JsonUtil.sendJsonResponse(response, resp);
            return;
        }

        String searchPattern = "%" + query + "%";

        try (Connection conn = DBConnection.getConnection()) {
            // 1. Search Projects
            String sqlProjects = "SELECT id, project_key, name, status FROM projects WHERE name LIKE ? OR project_key LIKE ? LIMIT 4";
            try (PreparedStatement ps = conn.prepareStatement(sqlProjects)) {
                ps.setString(1, searchPattern);
                ps.setString(2, searchPattern);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Map<String, Object> item = new HashMap<>();
                        item.put("category", "Project");
                        item.put("title", rs.getString("name"));
                        item.put("subtitle", "Key: " + rs.getString("project_key") + " • Status: " + rs.getString("status"));
                        item.put("url", request.getContextPath() + "/projects?action=view&id=" + rs.getInt("id"));
                        item.put("icon", "bi-folder-fill text-primary");
                        results.add(item);
                    }
                }
            }

            // 2. Search Tasks
            String sqlTasks = "SELECT id, title, status, priority FROM tasks WHERE title LIKE ? LIMIT 5";
            try (PreparedStatement ps = conn.prepareStatement(sqlTasks)) {
                ps.setString(1, searchPattern);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Map<String, Object> item = new HashMap<>();
                        item.put("category", "Task");
                        item.put("title", rs.getString("title"));
                        item.put("subtitle", "Priority: " + rs.getString("priority") + " • Status: " + rs.getString("status"));
                        item.put("url", request.getContextPath() + "/tasks?action=view&id=" + rs.getInt("id"));
                        item.put("icon", "bi-check2-square text-success");
                        results.add(item);
                    }
                }
            }

            // 3. Search Bugs
            String sqlBugs = "SELECT id, title, severity, status FROM bugs WHERE title LIKE ? LIMIT 5";
            try (PreparedStatement ps = conn.prepareStatement(sqlBugs)) {
                ps.setString(1, searchPattern);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Map<String, Object> item = new HashMap<>();
                        item.put("category", "Bug");
                        item.put("title", rs.getString("title"));
                        item.put("subtitle", "Severity: " + rs.getString("severity") + " • Status: " + rs.getString("status"));
                        item.put("url", request.getContextPath() + "/bugs?action=view&id=" + rs.getInt("id"));
                        item.put("icon", "bi-bug-fill text-danger");
                        results.add(item);
                    }
                }
            }

            // 4. Search Ideas / Proposals
            String sqlIdeas = "SELECT id, title, status FROM ideas WHERE title LIKE ? LIMIT 4";
            try (PreparedStatement ps = conn.prepareStatement(sqlIdeas)) {
                ps.setString(1, searchPattern);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Map<String, Object> item = new HashMap<>();
                        item.put("category", "Proposal");
                        item.put("title", rs.getString("title"));
                        item.put("subtitle", "Status: " + rs.getString("status"));
                        item.put("url", request.getContextPath() + "/ideas?action=view&id=" + rs.getInt("id"));
                        item.put("icon", "bi-lightbulb-fill text-warning");
                        results.add(item);
                    }
                }
            }
        } catch (Exception e) {
            JsonUtil.sendErrorJson(response, 500, e.getMessage());
            return;
        }

        Map<String, Object> resp = new HashMap<>();
        resp.put("results", results);
        JsonUtil.sendJsonResponse(response, resp);
    }
}
