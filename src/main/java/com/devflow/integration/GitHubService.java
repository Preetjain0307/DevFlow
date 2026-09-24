package com.devflow.integration;

import com.devflow.model.GitHubActivity;
import com.devflow.model.GitHubRepo;
import java.util.List;
import java.util.Map;

public interface GitHubService {
    GitHubRepo getRepoDetails(int projectId);
    boolean connectRepository(int projectId, String repoOwner, String repoName, String defaultBranch);
    boolean disconnectRepository(int projectId);
    List<GitHubActivity> getRecentActivity(int projectId, int limit);
    List<Map<String, Object>> getLiveCommits(int projectId, int limit);
    List<Map<String, Object>> getLivePullRequests(int projectId, int limit);
    List<Map<String, Object>> getLiveIssues(int projectId, int limit);
    boolean syncRepository(int projectId);
}
