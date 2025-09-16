package model.dao;

import model.entity.GameSessions;
import model.entity.SessionResults;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SessionResultsDaoTest {

    private SessionResultsDao dao;

    @BeforeAll
    void initDao() {
        dao = new SessionResultsDao();
    }

    @Test
    void testGetAllSessionResultsReturnsInsertedResult() throws SQLException {
        // Insert a valid GameSessions record
        GameSessionsDao gameSessionsDao = new GameSessionsDao();
        GameSessions testSession = new GameSessions(1, 1, new Timestamp(System.currentTimeMillis()), null);
        gameSessionsDao.persist(testSession);

        // Retrieve the session_id of the inserted GameSessions record
        int sessionId;
        try (Connection conn = datasource.MariaDbJpaConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT session_id FROM gamesessions WHERE user_id = ? AND deck_id = ?")) {
            ps.setInt(1, 1);
            ps.setInt(2, 1);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "GameSessions record not found");
                sessionId = rs.getInt("session_id");
            }
        }

        // Insert a SessionResults record using the retrieved session_id
        SessionResults testResult = new SessionResults(sessionId, 1, true, 100);
        dao.persist(testResult);

        // Verify the inserted SessionResults record
        List<SessionResults> results = dao.getAllSessionResults();
        assertNotNull(results);
        assertTrue(results.stream().anyMatch(r ->
                r.getSession_id() == sessionId &&
                r.getCard_id() == 1 &&
                r.isIs_correct()
        ));

        // Clean up the inserted records
        try (Connection conn = datasource.MariaDbJpaConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM sessionresults WHERE session_id = ? AND card_id = ?")) {
            ps.setInt(1, sessionId);
            ps.setInt(2, 1);
            ps.executeUpdate();
        }

        try (Connection conn = datasource.MariaDbJpaConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM gamesessions WHERE session_id = ?")) {
            ps.setInt(1, sessionId);
            ps.executeUpdate();
        }
    }
}