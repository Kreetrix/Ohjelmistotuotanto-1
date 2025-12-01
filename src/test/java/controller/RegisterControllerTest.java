package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.dao.AppUsersDao;
import model.entity.AppUsers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import util.I18n;
import util.Page;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RegisterControllerTest extends ApplicationTest {
    private final AppUsersDao appUsersDao = new AppUsersDao();
    private Connection conn = datasource.MariaDbJpaConnection.getConnection();

    RegisterControllerTest() throws SQLException {
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(Page.REGISTER.getPath()));
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    @AfterEach
    void tearDown() throws SQLException {

        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM app_users WHERE username = ?")) {
            ps.setString(1, "NewUser");
            ps.executeUpdate();
        }
    }

    @Test
    void onRegister() {

        clickOn("#usernameField").write("NewUser");
        clickOn("#emailField").write("T@T.com");
        clickOn("#passwordField").write("password");
        clickOn("#confirmPasswordField").write("password");
        clickOn("#registerBtn");

        AppUsers testUser;
        try {
            testUser = appUsersDao.getUserByUsername("NewUser");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        assertNotNull(testUser);
        assertEquals("NewUser", testUser.getUsername());
        assertEquals("T@T.com", testUser.getEmail());
    }
    @Test
    void onRegisterWithEmptyFields() {

        clickOn("#registerBtn");
        assertEquals(I18n.get("register.error.emptyfields"), lookup("#errorLabel").queryLabeled().getText() );

        clickOn("#passwordField").write("password");
        clickOn("#registerBtn");
        assertEquals(I18n.get("register.error.emptyfields"), lookup("#errorLabel").queryLabeled().getText() );


        clickOn("#usernameField").write("NewUser");
        clickOn("#registerBtn");
        assertEquals(I18n.get("register.error.emptyfields"), lookup("#errorLabel").queryLabeled().getText() );


        clickOn("#confirmPasswordField").write("password");
        clickOn("#registerBtn");
        assertEquals(I18n.get("register.error.emptyfields"), lookup("#errorLabel").queryLabeled().getText() );
    }
    @Test
    void onRegisterWithMismatchedPasswords() {

        clickOn("#usernameField").write("NewUser");
        clickOn("#emailField").write("T@T.com");
        clickOn("#passwordField").write("password");
        clickOn("#confirmPasswordField").write("pasw");
        clickOn("#registerBtn");
        assertEquals(I18n.get("register.passwordMismatch"), lookup("#errorLabel").queryLabeled().getText());

    }
    @Test
    void onBack() {
        clickOn("#backBtn");
        assertNotNull(lookup("#loginBtn").queryButton());
    }
}