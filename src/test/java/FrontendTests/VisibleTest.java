package FrontendTests;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.testfx.assertions.api.Assertions.assertThat;

public class VisibleTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/loginView.fxml"));
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }


    @Test
    void loginFieldsAreVisible() {
        TextField username = lookup("#usernameField").queryAs(TextField.class);
        PasswordField password = lookup("#passwordField").queryAs(PasswordField.class);
        Button loginBtn = lookup("#loginBtn").queryAs(Button.class);
        Button registerBtn = lookup("#registerBtn").queryAs(Button.class);
        assertThat(username).isVisible();
        assertThat(password).isVisible();
        assertThat(loginBtn).isVisible();
        assertThat(registerBtn).isVisible();


    }


}
