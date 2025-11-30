package FrontendTests;

import controller.Session;
import datasource.MariaDbJpaConnection;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.testfx.assertions.api.Assertions.assertThat;

class LoginTest extends ApplicationTest {

    @BeforeAll
    static void ensureTestUserExists() throws Exception {
        try (Connection conn = MariaDbJpaConnection.getConnection()) {
            try (PreparedStatement check = conn.prepareStatement("SELECT COUNT(*) FROM app_users WHERE username = ?")) {
                check.setString(1, "user");
                ResultSet rs = check.executeQuery();
                rs.next();

                if (rs.getInt(1) == 0) {
                    try (PreparedStatement insert = conn.prepareStatement(
                            "INSERT INTO app_users (username, password, email, phone, role, book_count, created_at) " +
                                    "VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                        insert.setString(1, "user");
                        insert.setString(2, "1234");
                        insert.setString(3, "user@example.com");
                        insert.setString(4, "0000000000");
                        insert.setString(5, "student");
                        insert.setInt(6, 0);
                        insert.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
                        insert.executeUpdate();
                    }
                }
            }
        }
    }

    @AfterAll
    static void cleanUp(){
        Session.getInstance().clear();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/loginView.fxml"));
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    @Test
    void wrongPasswordShowsError() {
        clickOn("#usernameField").write("user");
        clickOn("#passwordField").write("wrongpass");
        clickOn("#loginBtn");

        assertThat(lookup("#errorLabel").queryLabeled())
                .hasText("Invalid username or password");
    }

    @Test
    void correctLoginSwitchesScene() {
        clickOn("#usernameField").write("user");
        clickOn("#passwordField").write("1234");
        clickOn("#loginBtn");


    }
}
