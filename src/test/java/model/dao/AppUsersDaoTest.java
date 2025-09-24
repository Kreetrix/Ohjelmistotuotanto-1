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

        //delete test user from db
        try (Connection conn = datasource.MariaDbJpaConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM app_users WHERE username = ?")) {
            ps.setString(1, "txuser");
            ps.executeUpdate();
        }
    }

    @Test
    void testSetActive() throws SQLException {
        // Create test user with is_active = 1
        AppUsers testUser = new AppUsers("activeuser", "active@example.com", "hashed_pw", "student", 1, null);
        dao.persist(testUser);

        // Get the user ID
        int userId = 0;
        List<AppUsers> users = dao.getAllUsers();
        for (AppUsers user : users) {
            if (user.getUsername().equals("activeuser")) {
                userId = user.getUser_id();
                break;
            }
        }

        // Test setting active to false (0)
        dao.setActive(false, userId);
        users = dao.getAllUsers();
        AppUsers updatedUser = users.stream()
                .filter(u -> u.getUsername().equals("activeuser"))
                .findFirst()
                .orElse(null);
        assertNotNull(updatedUser);
        assertEquals(0, updatedUser.getIs_active());

        // Test setting active to true (1)
        dao.setActive(true, userId);
        users = dao.getAllUsers();
        updatedUser = users.stream()
                .filter(u -> u.getUsername().equals("activeuser"))
                .findFirst()
                .orElse(null);
        assertNotNull(updatedUser);
        assertEquals(1, updatedUser.getIs_active());

        // Clean up - delete test user from db
        try (Connection conn = datasource.MariaDbJpaConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM app_users WHERE username = ?")) {
            ps.setString(1, "activeuser");
            ps.executeUpdate();
        }
    }
}
