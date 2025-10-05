package model.dao;

import model.entity.GameSessions;
import datasource.MariaDbJpaConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for GameSessions entity.
 * Handles database operations for study session tracking.
 */
public class GameSessionsDao {

    /**
     * Retrieves all game sessions from the database.
     * @return List of all GameSessions entities
     * @throws SQLException if database access error occurs
     */
    public List<GameSessions> getAllGameSessions() throws SQLException {
        List<GameSessions> sessions = new ArrayList<>();
        String sql = "SELECT * FROM gamesessions";

        try (Connection conn = MariaDbJpaConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int session_id = rs.getInt("session_id");
                int user_id = rs.getInt("user_id");
                int deck_id = rs.getInt("deck_id");
                Timestamp start_time = rs.getTimestamp("start_time");
                Timestamp end_time = rs.getTimestamp("end_time");

                GameSessions session = new GameSessions(user_id, deck_id, start_time, end_time);
                session.setSession_id(session_id);
                sessions.add(session);
            }
        }

        return sessions;
    }

    /**
     * Saves a new game session to the database and returns the generated session ID.
     * @param session the GameSessions entity to persist
     * @return the generated session ID
     * @throws SQLException if database access error occurs
     */
    public int persist(GameSessions session) throws SQLException {
        String sql = "INSERT INTO gamesessions (user_id, deck_id, start_time, end_time) VALUES (?, ?, ?, ?)";
        try (Connection conn = MariaDbJpaConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, session.getUser_id());
            ps.setInt(2, session.getDeck_id());
            ps.setTimestamp(3, session.getStart_time());
            ps.setTimestamp(4, session.getEnd_time());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int sessionId = rs.getInt(1);
                    session.setSession_id(sessionId);
                    return sessionId;
                } else {
                    throw new SQLException("Failed to get session_id after insert");
                }
            }
        }
    }
}