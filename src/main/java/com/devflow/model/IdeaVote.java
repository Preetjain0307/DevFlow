package com.devflow.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class IdeaVote implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int ideaId;
    private int userId;
    private String username;
    private String userFullName;
    private String vote; // YES, NO
    private String comments;
    private Timestamp votedAt;

    public IdeaVote() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdeaId() { return ideaId; }
    public void setIdeaId(int ideaId) { this.ideaId = ideaId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getUserFullName() { return userFullName; }
    public void setUserFullName(String userFullName) { this.userFullName = userFullName; }

    public String getVote() { return vote; }
    public void setVote(String vote) { this.vote = vote; }

    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }

    public Timestamp getVotedAt() { return votedAt; }
    public void setVotedAt(Timestamp votedAt) { this.votedAt = votedAt; }
}
