package com.devflow.dao;

import com.devflow.model.GitHubActivity;
import com.devflow.model.GitHubRepo;
import java.util.List;

public interface GitHubDAO {
    GitHubRepo findByProjectId(int projectId);
    boolean saveOrUpdateRepo(GitHubRepo repo);
    boolean deleteRepo(int projectId);
    boolean addActivity(GitHubActivity activity);
    List<GitHubActivity> findActivityByProjectId(int projectId, int limit);
    List<GitHubActivity> findRecentActivity(int limit);
}
