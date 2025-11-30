package FrontendTests;

import controller.Session;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import static org.testfx.assertions.api.Assertions.assertThat;

class MainTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }
    @AfterAll
    static void cleanUp(){
        Session.getInstance().clear();
    }

    @Test
    void navbarButtonsAreVisible() {
        WaitForAsyncUtils.waitForFxEvents();
        assertThat((Button) lookup("#searchBtn").query()).isVisible();
        assertThat((Button) lookup("#listBtn").query()).isVisible();
        assertThat((Button) lookup("#studyBtn").query()).isVisible();
        assertThat((Button) lookup("#logoutBtn").query()).isVisible();
    }

}
