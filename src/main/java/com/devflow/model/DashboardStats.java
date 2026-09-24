package com.devflow.model;

import java.io.Serializable;
import java.util.Map;

public class DashboardStats implements Serializable {
    private static final long serialVersionUID = 1L;

    // Counts
    private int totalProjects;
    private int activeProjects;
    private int completedProjects;

    private int totalTasks;
    private int myTasks;
    private int todoTasks;
    private int inProgressTasks;
    private int inReviewTasks;
    private int completedTasks;

    private int totalBugs;
    private int myBugs;
    private int openBugs;
    private int resolvedBugs;
    private int criticalBugs;

    private int upcomingMeetings;
    private int pendingIdeas;
    private int approvedIdeas;
    private int totalUsers;

    // Chart Data Maps
    private Map<String, Integer> taskStatusDistribution;
    private Map<String, Integer> bugSeverityDistribution;
    private Map<String, Integer> ideaStatusDistribution;

    public DashboardStats() {}

    public int getTotalProjects() { return totalProjects; }
    public void setTotalProjects(int totalProjects) { this.totalProjects = totalProjects; }

    public int getActiveProjects() { return activeProjects; }
    public void setActiveProjects(int activeProjects) { this.activeProjects = activeProjects; }

    public int getCompletedProjects() { return completedProjects; }
    public void setCompletedProjects(int completedProjects) { this.completedProjects = completedProjects; }

    public int getTotalTasks() { return totalTasks; }
    public void setTotalTasks(int totalTasks) { this.totalTasks = totalTasks; }

    public int getMyTasks() { return myTasks; }
    public void setMyTasks(int myTasks) { this.myTasks = myTasks; }

    public int getTodoTasks() { return todoTasks; }
    public void setTodoTasks(int todoTasks) { this.todoTasks = todoTasks; }

    public int getInProgressTasks() { return inProgressTasks; }
    public void setInProgressTasks(int inProgressTasks) { this.inProgressTasks = inProgressTasks; }

    public int getInReviewTasks() { return inReviewTasks; }
    public void setInReviewTasks(int inReviewTasks) { this.inReviewTasks = inReviewTasks; }

    public int getCompletedTasks() { return completedTasks; }
    public void setCompletedTasks(int completedTasks) { this.completedTasks = completedTasks; }

    public int getTotalBugs() { return totalBugs; }
    public void setTotalBugs(int totalBugs) { this.totalBugs = totalBugs; }

    public int getMyBugs() { return myBugs; }
    public void setMyBugs(int myBugs) { this.myBugs = myBugs; }

    public int getOpenBugs() { return openBugs; }
    public void setOpenBugs(int openBugs) { this.openBugs = openBugs; }

    public int getResolvedBugs() { return resolvedBugs; }
    public void setResolvedBugs(int resolvedBugs) { this.resolvedBugs = resolvedBugs; }

    public int getCriticalBugs() { return criticalBugs; }
    public void setCriticalBugs(int criticalBugs) { this.criticalBugs = criticalBugs; }

    public int getUpcomingMeetings() { return upcomingMeetings; }
    public void setUpcomingMeetings(int upcomingMeetings) { this.upcomingMeetings = upcomingMeetings; }

    public int getPendingIdeas() { return pendingIdeas; }
    public void setPendingIdeas(int pendingIdeas) { this.pendingIdeas = pendingIdeas; }

    public int getApprovedIdeas() { return approvedIdeas; }
    public void setApprovedIdeas(int approvedIdeas) { this.approvedIdeas = approvedIdeas; }

    public int getTotalUsers() { return totalUsers; }
    public void setTotalUsers(int totalUsers) { this.totalUsers = totalUsers; }

    public Map<String, Integer> getTaskStatusDistribution() { return taskStatusDistribution; }
    public void setTaskStatusDistribution(Map<String, Integer> taskStatusDistribution) { this.taskStatusDistribution = taskStatusDistribution; }

    public Map<String, Integer> getBugSeverityDistribution() { return bugSeverityDistribution; }
    public void setBugSeverityDistribution(Map<String, Integer> bugSeverityDistribution) { this.bugSeverityDistribution = bugSeverityDistribution; }

    public Map<String, Integer> getIdeaStatusDistribution() { return ideaStatusDistribution; }
    public void setIdeaStatusDistribution(Map<String, Integer> ideaStatusDistribution) { this.ideaStatusDistribution = ideaStatusDistribution; }
}
