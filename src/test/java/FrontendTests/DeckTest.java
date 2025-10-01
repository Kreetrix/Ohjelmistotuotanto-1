package FrontendTests;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

public class DeckTest extends ApplicationTest {

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
