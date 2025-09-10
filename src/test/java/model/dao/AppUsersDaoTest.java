package model.dao;

import model.entity.AppUsers;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AppUsersDaoTest {

    private AppUsersDao dao;

    @BeforeAll
    void initDao() {
        dao = new AppUsersDao();
    }

    //creates test user and add it to db checks if test user in db then deletes the test user
    @Test
    void testGetAllUsersReturnsInsertedUser() throws SQLException {
        AppUsers testUser = new AppUsers("txuser", "tx@example.com", "hashed_pw", "student", 1, null);
        dao.persist(testUser);

        List<AppUsers> users = dao.getAllUsers();
        assertNotNull(users);
        assertTrue(users.stream().anyMatch(u -> u.getUsername().equals("txuser")));

        try (Connection conn = datasource.MariaDbJpaConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM app_users WHERE username = ?")) {
            ps.setString(1, "txuser");
            ps.executeUpdate();
        }
    }
}
