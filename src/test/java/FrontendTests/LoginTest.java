package FrontendTests;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;

import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.testfx.assertions.api.Assertions.assertThat;


public class LoginTest extends ApplicationTest {

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
