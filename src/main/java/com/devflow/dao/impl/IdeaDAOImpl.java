package com.devflow.dao.impl;

import com.devflow.config.DBConnection;
import com.devflow.dao.IdeaDAO;
import com.devflow.exception.DatabaseException;
import com.devflow.model.Idea;
import com.devflow.model.IdeaComment;
import com.devflow.model.IdeaHistory;
import com.devflow.model.IdeaVote;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IdeaDAOImpl implements IdeaDAO {
    private static final Logger logger = LoggerFactory.getLogger(IdeaDAOImpl.class);

    private static final String BASE_SELECT = 
            "SELECT i.id, i.project_id, p.project_key, p.name AS project_name, " +
            "i.title, i.description, i.problem_statement, i.proposed_solution, " +
            "i.expected_benefit, i.priority, i.estimated_effort_days, " +
            "i.status, i.faculty_status, i.submitted_by, u1.full_name AS submitter_name, " +
            "i.faculty_reviewed_by, u2.full_name AS faculty_reviewer_name, " +
            "i.faculty_rejection_reason, i.faculty_review_date, i.created_at, i.updated_at, " +
            "(SELECT COUNT(*) FROM idea_votes iv WHERE iv.idea_id = i.id AND iv.vote = 'YES') AS yes_votes, " +
            "(SELECT COUNT(*) FROM idea_votes iv WHERE iv.idea_id = i.id AND iv.vote = 'NO') AS no_votes, " +
            "(SELECT COUNT(*) FROM project_members pm WHERE pm.project_id = i.project_id) AS total_eligible_voters " +
            "FROM ideas i " +
            "JOIN projects p ON i.project_id = p.id " +
            "JOIN users u1 ON i.submitted_by = u1.id " +
            "LEFT JOIN users u2 ON i.faculty_reviewed_by = u2.id ";

    private Idea mapRow(ResultSet rs) throws SQLException {
        Idea i = new Idea();
        i.setId(rs.getInt("id"));
        i.setProjectId(rs.getInt("project_id"));
        i.setProjectKey(rs.getString("project_key"));
        i.setProjectName(rs.getString("project_name"));
        i.setTitle(rs.getString("title"));
        i.setDescription(rs.getString("description"));
        i.setProblemStatement(rs.getString("problem_statement"));
        i.setProposedSolution(rs.getString("proposed_solution"));
        i.setExpectedBenefit(rs.getString("expected_benefit"));
        i.setPriority(rs.getString("priority"));
        i.setEstimatedEffortDays(rs.getInt("estimated_effort_days"));
        i.setStatus(rs.getString("status"));
        i.setFacultyStatus(rs.getString("faculty_status"));
        i.setSubmittedBy(rs.getInt("submitted_by"));
        i.setSubmitterName(rs.getString("submitter_name"));

        int reviewerId = rs.getInt("faculty_reviewed_by");
        i.setFacultyReviewedBy(rs.wasNull() ? null : reviewerId);
        i.setFacultyReviewerName(rs.getString("faculty_reviewer_name"));

        i.setFacultyRejectionReason(rs.getString("faculty_rejection_reason"));
        i.setFacultyReviewDate(rs.getTimestamp("faculty_review_date"));
        i.setCreatedAt(rs.getTimestamp("created_at"));
        i.setUpdatedAt(rs.getTimestamp("updated_at"));

        i.setYesVotes(rs.getInt("yes_votes"));
        i.setNoVotes(rs.getInt("no_votes"));
        i.setTotalEligibleVoters(rs.getInt("total_eligible_voters"));
        return i;
    }

    @Override
    public Idea findById(int id) {
        String sql = BASE_SELECT + "WHERE i.id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                Idea idea = mapRow(rs);
                idea.setVotes(findVotesByIdeaId(id));
                idea.setComments(findCommentsByIdeaId(id));
                idea.setHistory(findHistoryByIdeaId(id));
                return idea;
            }
        } catch (SQLException e) {
            logger.error("Error finding idea by id {}: {}", id, e.getMessage());
            throw new DatabaseException("Failed to find idea proposal", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return null;
    }

    @Override
    public List<Idea> findByProjectId(int projectId) {
        String sql = BASE_SELECT + "WHERE i.project_id = ? ORDER BY i.created_at DESC";
        List<Idea> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, projectId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            logger.error("Error finding ideas for project {}: {}", projectId, e.getMessage());
            throw new DatabaseException("Failed to retrieve project ideas", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    @Override
    public List<Idea> findBySubmitterId(int userId) {
        String sql = BASE_SELECT + "WHERE i.submitted_by = ? ORDER BY i.created_at DESC";
        List<Idea> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            logger.error("Error finding submitted ideas: {}", e.getMessage());
            throw new DatabaseException("Failed to retrieve submitted ideas", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    @Override
    public List<Idea> findPendingFacultyReview() {
        String sql = BASE_SELECT + "WHERE i.status = 'PENDING_FACULTY' OR (i.status = 'VOTING_PASSED' AND i.faculty_status = 'PENDING') ORDER BY i.created_at ASC";
        List<Idea> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            logger.error("Error finding pending faculty review ideas: {}", e.getMessage());
            throw new DatabaseException("Failed to retrieve proposals for faculty review", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    @Override
    public List<Idea> search(Integer projectId, String status, String facultyStatus, String keyword) {
        StringBuilder sb = new StringBuilder(BASE_SELECT);
        sb.append("WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        if (projectId != null && projectId > 0) {
            sb.append("AND i.project_id = ? ");
            params.add(projectId);
        }
        if (status != null && !status.trim().isEmpty() && !"ALL".equalsIgnoreCase(status)) {
            sb.append("AND i.status = ? ");
            params.add(status.trim());
        }
        if (facultyStatus != null && !facultyStatus.trim().isEmpty() && !"ALL".equalsIgnoreCase(facultyStatus)) {
            sb.append("AND i.faculty_status = ? ");
            params.add(facultyStatus.trim());
        }
        if (keyword != null && !keyword.trim().isEmpty()) {
            sb.append("AND (i.title LIKE ? OR i.description LIKE ? OR i.problem_statement LIKE ? OR i.proposed_solution LIKE ?) ");
            String term = "%" + keyword.trim() + "%";
            params.add(term);
            params.add(term);
            params.add(term);
            params.add(term);
        }
        sb.append("ORDER BY i.created_at DESC");

        List<Idea> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sb.toString());
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            logger.error("Error searching ideas: {}", e.getMessage());
            throw new DatabaseException("Failed to search ideas", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    @Override
    public boolean create(Idea idea) {
        String sql = "INSERT INTO ideas (project_id, title, description, problem_statement, proposed_solution, expected_benefit, priority, estimated_effort_days, status, faculty_status, submitted_by) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, idea.getProjectId());
            ps.setString(2, idea.getTitle());
            ps.setString(3, idea.getDescription());
            ps.setString(4, idea.getProblemStatement());
            ps.setString(5, idea.getProposedSolution());
            ps.setString(6, idea.getExpectedBenefit());
            ps.setString(7, idea.getPriority() != null ? idea.getPriority() : "MEDIUM");
            ps.setInt(8, idea.getEstimatedEffortDays());
            ps.setString(9, idea.getStatus() != null ? idea.getStatus() : "IN_VOTING");
            ps.setString(10, idea.getFacultyStatus() != null ? idea.getFacultyStatus() : "PENDING");
            ps.setInt(11, idea.getSubmittedBy());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    idea.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            logger.error("Error creating idea proposal: {}", e.getMessage());
            throw new DatabaseException("Failed to create idea proposal", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return false;
    }

    @Override
    public boolean update(Idea idea) {
        String sql = "UPDATE ideas SET title = ?, description = ?, problem_statement = ?, proposed_solution = ?, expected_benefit = ?, priority = ?, estimated_effort_days = ?, status = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, idea.getTitle());
            ps.setString(2, idea.getDescription());
            ps.setString(3, idea.getProblemStatement());
            ps.setString(4, idea.getProposedSolution());
            ps.setString(5, idea.getExpectedBenefit());
            ps.setString(6, idea.getPriority());
            ps.setInt(7, idea.getEstimatedEffortDays());
            ps.setString(8, idea.getStatus());
            ps.setInt(9, idea.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating idea id {}: {}", idea.getId(), e.getMessage());
            throw new DatabaseException("Failed to update idea", e);
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public boolean updateStatus(int ideaId, String status) {
        String sql = "UPDATE ideas SET status = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, ideaId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating idea status: {}", e.getMessage());
            throw new DatabaseException("Failed to update status", e);
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public boolean updateFacultyDecision(int ideaId, String decision, String rejectionReason, int facultyUserId) {
        String overallStatus = decision; // APPROVED, REJECTED, CHANGES_REQUESTED
        String sql = "UPDATE ideas SET status = ?, faculty_status = ?, faculty_reviewed_by = ?, faculty_rejection_reason = ?, faculty_review_date = CURRENT_TIMESTAMP WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, overallStatus);
            ps.setString(2, decision);
            ps.setInt(3, facultyUserId);
            if (rejectionReason != null && !rejectionReason.trim().isEmpty()) {
                ps.setString(4, rejectionReason.trim());
            } else {
                ps.setNull(4, Types.VARCHAR);
            }
            ps.setInt(5, ideaId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating faculty decision: {}", e.getMessage());
            throw new DatabaseException("Failed to record faculty decision", e);
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public boolean delete(int ideaId) {
        String sql = "DELETE FROM ideas WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, ideaId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error deleting idea {}: {}", ideaId, e.getMessage());
            throw new DatabaseException("Failed to delete idea", e);
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public boolean castVote(IdeaVote vote) {
        String sql = "INSERT INTO idea_votes (idea_id, user_id, vote, comments) VALUES (?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE vote = VALUES(vote), comments = VALUES(comments), voted_at = CURRENT_TIMESTAMP";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, vote.getIdeaId());
            ps.setInt(2, vote.getUserId());
            ps.setString(3, vote.getVote());
            ps.setString(4, vote.getComments());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error casting vote: {}", e.getMessage());
            throw new DatabaseException("Failed to record vote", e);
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public IdeaVote findUserVote(int ideaId, int userId) {
        String sql = "SELECT id, idea_id, user_id, vote, comments, voted_at FROM idea_votes WHERE idea_id = ? AND user_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, ideaId);
            ps.setInt(2, userId);
            rs = ps.executeQuery();
            if (rs.next()) {
                IdeaVote iv = new IdeaVote();
                iv.setId(rs.getInt("id"));
                iv.setIdeaId(rs.getInt("idea_id"));
                iv.setUserId(rs.getInt("user_id"));
                iv.setVote(rs.getString("vote"));
                iv.setComments(rs.getString("comments"));
                iv.setVotedAt(rs.getTimestamp("voted_at"));
                return iv;
            }
        } catch (SQLException e) {
            logger.error("Error finding user vote: {}", e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return null;
    }

    @Override
    public List<IdeaVote> findVotesByIdeaId(int ideaId) {
        String sql = "SELECT iv.id, iv.idea_id, iv.user_id, u.username, u.full_name, iv.vote, iv.comments, iv.voted_at " +
                     "FROM idea_votes iv " +
                     "JOIN users u ON iv.user_id = u.id " +
                     "WHERE iv.idea_id = ? ORDER BY iv.voted_at ASC";
        List<IdeaVote> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, ideaId);
            rs = ps.executeQuery();
            while (rs.next()) {
                IdeaVote iv = new IdeaVote();
                iv.setId(rs.getInt("id"));
                iv.setIdeaId(rs.getInt("idea_id"));
                iv.setUserId(rs.getInt("user_id"));
                iv.setUsername(rs.getString("username"));
                iv.setUserFullName(rs.getString("full_name"));
                iv.setVote(rs.getString("vote"));
                iv.setComments(rs.getString("comments"));
                iv.setVotedAt(rs.getTimestamp("voted_at"));
                list.add(iv);
            }
        } catch (SQLException e) {
            logger.error("Error finding votes for idea {}: {}", ideaId, e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    @Override
    public int countYesVotes(int ideaId) {
        String sql = "SELECT COUNT(*) FROM idea_votes WHERE idea_id = ? AND vote = 'YES'";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, ideaId);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            logger.error("Error counting yes votes: {}", e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return 0;
    }

    @Override
    public int countNoVotes(int ideaId) {
        String sql = "SELECT COUNT(*) FROM idea_votes WHERE idea_id = ? AND vote = 'NO'";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, ideaId);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            logger.error("Error counting no votes: {}", e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return 0;
    }

    @Override
    public boolean addComment(IdeaComment comment) {
        String sql = "INSERT INTO idea_comments (idea_id, user_id, comment) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, comment.getIdeaId());
            ps.setInt(2, comment.getUserId());
            ps.setString(3, comment.getComment());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error adding idea comment: {}", e.getMessage());
            return false;
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public List<IdeaComment> findCommentsByIdeaId(int ideaId) {
        String sql = "SELECT ic.id, ic.idea_id, ic.user_id, u.username, u.full_name, ic.comment, ic.created_at " +
                     "FROM idea_comments ic " +
                     "JOIN users u ON ic.user_id = u.id " +
                     "WHERE ic.idea_id = ? ORDER BY ic.created_at ASC";
        List<IdeaComment> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, ideaId);
            rs = ps.executeQuery();
            while (rs.next()) {
                IdeaComment ic = new IdeaComment();
                ic.setId(rs.getInt("id"));
                ic.setIdeaId(rs.getInt("idea_id"));
                ic.setUserId(rs.getInt("user_id"));
                ic.setUsername(rs.getString("username"));
                ic.setUserFullName(rs.getString("full_name"));
                ic.setComment(rs.getString("comment"));
                ic.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(ic);
            }
        } catch (SQLException e) {
            logger.error("Error finding idea comments: {}", e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    @Override
    public boolean addHistory(IdeaHistory history) {
        String sql = "INSERT INTO idea_history (idea_id, user_id, action, notes) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, history.getIdeaId());
            ps.setInt(2, history.getUserId());
            ps.setString(3, history.getAction());
            ps.setString(4, history.getNotes());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error adding idea history: {}", e.getMessage());
            return false;
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public List<IdeaHistory> findHistoryByIdeaId(int ideaId) {
        String sql = "SELECT ih.id, ih.idea_id, ih.user_id, u.username, u.full_name, ih.action, ih.notes, ih.created_at " +
                     "FROM idea_history ih " +
                     "JOIN users u ON ih.user_id = u.id " +
                     "WHERE ih.idea_id = ? ORDER BY ih.created_at DESC";
        List<IdeaHistory> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, ideaId);
            rs = ps.executeQuery();
            while (rs.next()) {
                IdeaHistory ih = new IdeaHistory();
                ih.setId(rs.getInt("id"));
                ih.setIdeaId(rs.getInt("idea_id"));
                ih.setUserId(rs.getInt("user_id"));
                ih.setUsername(rs.getString("username"));
                ih.setUserFullName(rs.getString("full_name"));
                ih.setAction(rs.getString("action"));
                ih.setNotes(rs.getString("notes"));
                ih.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(ih);
            }
        } catch (SQLException e) {
            logger.error("Error finding idea history: {}", e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    @Override
    public int countTotalIdeas() {
        String sql = "SELECT COUNT(*) FROM ideas";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            logger.error("Error counting ideas: {}", e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return 0;
    }

    @Override
    public int countPendingIdeas() {
        String sql = "SELECT COUNT(*) FROM ideas WHERE status IN ('IN_VOTING', 'PENDING_FACULTY', 'VOTING_PASSED')";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            logger.error("Error counting pending ideas: {}", e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return 0;
    }

    @Override
    public int countApprovedIdeas() {
        String sql = "SELECT COUNT(*) FROM ideas WHERE status = 'APPROVED'";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            logger.error("Error counting approved ideas: {}", e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return 0;
    }

    @Override
    public Map<String, Integer> getStatusDistribution() {
        String sql = "SELECT status, COUNT(*) AS count FROM ideas GROUP BY status";
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("IN_VOTING", 0);
        map.put("PENDING_FACULTY", 0);
        map.put("APPROVED", 0);
        map.put("REJECTED", 0);
        map.put("CHANGES_REQUESTED", 0);

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("status"), rs.getInt("count"));
            }
        } catch (SQLException e) {
            logger.error("Error getting idea status distribution: {}", e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return map;
    }

    @Override
    public Map<String, Integer> getStatusDistributionByProject(int projectId) {
        String sql = "SELECT status, COUNT(*) AS count FROM ideas WHERE project_id = ? GROUP BY status";
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("IN_VOTING", 0);
        map.put("PENDING_FACULTY", 0);
        map.put("APPROVED", 0);
        map.put("REJECTED", 0);
        map.put("CHANGES_REQUESTED", 0);

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, projectId);
            rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("status"), rs.getInt("count"));
            }
        } catch (SQLException e) {
            logger.error("Error getting idea status distribution for project: {}", e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return map;
    }
}
