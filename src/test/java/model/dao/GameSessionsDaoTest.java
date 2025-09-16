package model.dao;

import model.entity.GameSessions;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GameSessionsDaoTest {

    private GameSessionsDao dao;

    @BeforeAll
    void initDao() {
        dao = new GameSessionsDao();
    }

    @Test
    void testGetAllGameSessionsReturnsInsertedSession() throws SQLException {
        GameSessions testSession = new GameSessions(1, 1, new Timestamp(System.currentTimeMillis()), null);
        dao.persist(testSession);

        List<GameSessions> sessions = dao.getAllGameSessions();
        assertNotNull(sessions);
        assertTrue(sessions.stream().anyMatch(s ->
                s.getUser_id() == 1 &&
                s.getDeck_id() == 1
        ));

        try (Connection conn = datasource.MariaDbJpaConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM gamesessions WHERE user_id = ? AND deck_id = ?")) {
            ps.setInt(1, 1);
            ps.setInt(2, 1);
            ps.executeUpdate();
        }
    }
}