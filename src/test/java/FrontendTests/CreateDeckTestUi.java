package FrontendTests;

import controller.Session;
import datasource.MariaDbJpaConnection;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.entity.AppUsers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.sql.*;

import static org.testfx.assertions.api.Assertions.assertThat;

public class CreateDeckTestUi extends ApplicationTest {

    @BeforeAll
    static void prepareTestData() throws Exception {
        try (Connection conn = MariaDbJpaConnection.getConnection()) {
            try (Statement stmt = conn.createStatement()) {
stmt.execute(
                    "CREATE TABLE IF NOT EXISTS decks (" +
                    "deck_id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id BIGINT, " +
                    "deck_name VARCHAR(255), " +
                    "description VARCHAR(255)" +
                    ")"
                );            }
        }
    }

    @BeforeAll
    static void loginTestUser() throws Exception {
        try (Connection conn = MariaDbJpaConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM app_users WHERE username=?")) {
            ps.setString(1, "user");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                AppUsers user = new AppUsers(
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        rs.getString("role"),
                        rs.getInt("is_active"),
                        rs.getTimestamp("created_at")
                );
                user.setUser_id(rs.getInt("user_id"));
                Session.getInstance().setCurrentUser(user);
            }
        }
    }


    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/creation.fxml"));
        stage.setScene(new Scene(root, 600, 800));
        stage.show();
    }

    @Test
    void userCanCreateNewDeck() throws Exception {
        clickOn("#createDeckButton");
        clickOn("#deckNameField").write("English");
        clickOn("#descriptionField").write("Basic English vocabulary");
        clickOn("#saveButton");
        assertThat(lookup(".menu-item-button").lookup("English")).isNotNull();
        try (Connection conn = MariaDbJpaConnection.getConnection(); PreparedStatement ps = conn.prepareStatement("SELECT * FROM decks WHERE deck_name=?")) {
            ps.setString(1, "English");
            ResultSet rs = ps.executeQuery();
            assertThat(rs.next()).isTrue();
        }
    }

    @AfterEach
    void cleanUp() throws Exception {
        try (Connection conn = MariaDbJpaConnection.getConnection()) {
            try (PreparedStatement ps1 = conn.prepareStatement(
                    "DELETE FROM gamesessions WHERE deck_id IN (SELECT deck_id FROM decks WHERE deck_name = ?)")) {
                ps1.setString(1, "English");
                ps1.executeUpdate();
            }
        }

    }
}