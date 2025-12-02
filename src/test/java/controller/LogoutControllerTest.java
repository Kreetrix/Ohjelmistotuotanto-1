package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.entity.AppUsers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import util.Page;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LogoutControllerTest extends ApplicationTest {

    private static final AppUsers TestUser = new AppUsers("testUser","test@email.com", "test1","student",1,new Timestamp(System.currentTimeMillis()));

    @BeforeEach
    void setUp() {
        Session.getInstance().setCurrentUser(TestUser);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(Page.MAIN.getPath()));
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }
    @Test
    void onLogout() {
        LogoutController controller = new LogoutController();
        interact( () ->{
            controller.onLogout();
            List<Window> windows = new ArrayList<>(Window.getWindows());
            assertNull(Session.getInstance().getCurrentUser());
            assertEquals(1, windows.size());
            assertEquals("en",Session.getInstance().getLanguage());
        });
    }
}