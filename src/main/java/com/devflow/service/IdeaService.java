package com.devflow.service;

import com.devflow.model.Idea;
import com.devflow.model.IdeaComment;
import com.devflow.model.IdeaHistory;
import com.devflow.model.IdeaVote;
import java.util.List;

public interface IdeaService {
    Idea getIdeaById(int id, Integer currentUserId);
    List<Idea> getIdeasByProjectId(int projectId);
    List<Idea> getIdeasBySubmitter(int userId);
    List<Idea> getPendingFacultyReviewIdeas();
    List<Idea> searchIdeas(Integer projectId, String status, String facultyStatus, String keyword);
    boolean submitIdea(Idea idea, int userId, String username, String ipAddress);
    boolean updateIdea(Idea idea, int userId, String username, String ipAddress);
    boolean castVote(int ideaId, int userId, String username, String vote, String comment, String ipAddress);
    boolean processFacultyDecision(int ideaId, String decision, String rejectionReason, int facultyUserId, String facultyUsername, String ipAddress);
    boolean addComment(int ideaId, int userId, String comment);
    List<IdeaComment> getComments(int ideaId);
    List<IdeaHistory> getHistory(int ideaId);
    boolean deleteIdea(int ideaId, int userId, String username, String ipAddress);
}
