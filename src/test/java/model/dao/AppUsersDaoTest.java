package model.dao;


import datasource.PasswordUtil;
import model.entity.AppUsers;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AppUsersDaoTest {

    private AppUsersDao dao;
    private Connection conn;
    private AppUsers testUser;

    @BeforeAll
    void initDao() throws SQLException {
        dao = new AppUsersDao();
        conn = datasource.MariaDbJpaConnection.getConnection();

    }
    @BeforeEach
    void initTestUser() throws SQLException {
        testUser = new AppUsers("activeuser", "active@example.com", "hashed_pw", "student", 1, null);
        dao.persist(testUser);
    }

    @AfterEach
    void destroyUser() throws SQLException {

        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM app_users WHERE username = ?")) {
            ps.setString(1, "activeuser");
            ps.executeUpdate();
        }
    }
    @AfterAll
    void destroyConnection() throws SQLException {
        conn.close();
    }

    //creates test user and add it to db checks if test user in db then deletes the test user
    @Test
    void testGetAllUsersReturnsInsertedUser() throws SQLException {


        List<AppUsers> users = dao.getAllUsers();
        assertNotNull(users);
        assertTrue(users.stream().anyMatch(u -> u.getUsername().equals("activeuser")));

    }

    @Test
    void testSetActive() throws SQLException {

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


    }

    @Test
    void testGetUserByName() throws SQLException {

        AppUsers user = dao.getUserByUsername("activeuser");
        assertEquals("activeuser", user.getUsername());
        assertEquals("active@example.com", user.getEmail());
        assertNotNull(user.getPassword_hash());
        assertEquals("student", user.getRole());
        assertEquals(1, user.getIs_active());

        assertNull(dao.getUserByUsername(null));


    }
}
