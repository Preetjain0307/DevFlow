package com.devflow.service;

import com.devflow.model.Bug;
import com.devflow.model.BugComment;
import java.util.List;

public interface BugService {
    Bug getBugById(int id);
    List<Bug> getBugsByProjectId(int projectId);
    List<Bug> getBugsByAssignee(int userId);
    List<Bug> getBugsByReporter(int userId);
    List<Bug> searchBugs(Integer projectId, Integer assignedTo, String severity, String status, String keyword);
    boolean reportBug(Bug bug, int userId, String username, String ipAddress);
    boolean updateBug(Bug bug, int userId, String username, String ipAddress);
    boolean updateBugStatus(int bugId, String status, String resolutionNotes, int userId, String username, String ipAddress);
    boolean deleteBug(int bugId, int userId, String username, String ipAddress);
    boolean addComment(int bugId, int userId, String comment);
    List<BugComment> getComments(int bugId);
}
