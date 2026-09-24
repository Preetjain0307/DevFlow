package com.devflow.dao;

import com.devflow.model.Idea;
import com.devflow.model.IdeaComment;
import com.devflow.model.IdeaHistory;
import com.devflow.model.IdeaVote;
import java.util.List;
import java.util.Map;

public interface IdeaDAO {
    Idea findById(int id);
    List<Idea> findByProjectId(int projectId);
    List<Idea> findBySubmitterId(int userId);
    List<Idea> findPendingFacultyReview();
    List<Idea> search(Integer projectId, String status, String facultyStatus, String keyword);
    boolean create(Idea idea);
    boolean update(Idea idea);
    boolean updateStatus(int ideaId, String status);
    boolean updateFacultyDecision(int ideaId, String decision, String rejectionReason, int facultyUserId);
    boolean delete(int ideaId);

    // Votes
    boolean castVote(IdeaVote vote);
    IdeaVote findUserVote(int ideaId, int userId);
    List<IdeaVote> findVotesByIdeaId(int ideaId);
    int countYesVotes(int ideaId);
    int countNoVotes(int ideaId);

    // Comments & History
    boolean addComment(IdeaComment comment);
    List<IdeaComment> findCommentsByIdeaId(int ideaId);
    boolean addHistory(IdeaHistory history);
    List<IdeaHistory> findHistoryByIdeaId(int ideaId);

    // Metrics
    int countTotalIdeas();
    int countPendingIdeas();
    int countApprovedIdeas();
    Map<String, Integer> getStatusDistribution();
    Map<String, Integer> getStatusDistributionByProject(int projectId);
}
