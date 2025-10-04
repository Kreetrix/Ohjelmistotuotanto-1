package FrontendTests;

import datasource.MariaDbJpaConnection;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.sql.*;

public class DeckTest extends ApplicationTest {

    @BeforeAll
    static void prepareTestData() throws Exception {
        try (Connection conn = MariaDbJpaConnection.getConnection()) {
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS users (
                        user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        username VARCHAR(255),
                        password VARCHAR(255),
                        email VARCHAR(255),
                        phone VARCHAR(20),
                        role VARCHAR(50),
                        book_count INT,
                        created_at TIMESTAMP,
                        deleted_at TIMESTAMP NULL
                    )
                """);

                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS decks (
                        deck_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        user_id BIGINT,
                        deck_name VARCHAR(255)
                    )
                """);
            }

            try (PreparedStatement check = conn.prepareStatement("SELECT COUNT(*) FROM app_users WHERE username = ?")) {
                check.setString(1, "user");
                ResultSet rs = check.executeQuery();
                rs.next();
                if (rs.getInt(1) == 0) {
                    try (PreparedStatement insert = conn.prepareStatement(
                            "INSERT INTO app_users (username, password, email, phone, role, book_count, created_at) VALUES (?, ?, ?, ?, ?, ?, NOW())")) {
                        insert.setString(1, "user");
                        insert.setString(2, "1234");
                        insert.setString(3, "user@example.com");
                        insert.setString(4, "0000000000");
                        insert.setString(5, "student");
                        insert.setInt(6, 0);
                        insert.executeUpdate();
                    }
                }
            }

            try (PreparedStatement checkDeck = conn.prepareStatement("SELECT COUNT(*) FROM decks WHERE deck_name=?")) {
                checkDeck.setString(1, "Biology Basics");
                ResultSet rs = checkDeck.executeQuery();
                rs.next();
                if (rs.getInt(1) == 0) {
                    try (PreparedStatement insertDeck = conn.prepareStatement(
                            "INSERT INTO decks (user_id, name) VALUES (1, 'Biology Basics')")) {
                        insertDeck.executeUpdate();
                    }
                }
            }
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/loginView.fxml"));
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    @Test
    void ChooseDeck() {
        clickOn("#usernameField").write("user");
        clickOn("#passwordField").write("1234");
        clickOn("#loginBtn");
        clickOn("My Decks");
        clickOn("Biology Basics");
        clickOn("Start Study");
    }
}
