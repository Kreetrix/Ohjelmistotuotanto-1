package util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;


import static org.junit.jupiter.api.Assertions.*;

class PageLoaderTest extends ApplicationTest {

    static PageLoader pageLoader;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    @BeforeAll
    static void beforeAll() {
        pageLoader = PageLoader.getInstance();
    }

    @Test
    void getInstance() {
        assertSame(pageLoader,PageLoader.getInstance());
    }

    @Test
    void loadPage() {
       interact(() -> assertNotNull(pageLoader.loadPage("/fxml/loginView.fxml", "test")));
    }

    @Test
    void loadPopUp() {
        interact(() -> {
            Stage popup = pageLoader.loadPopUp("/fxml/main.fxml", "test").stage();
            assertNotNull(popup);

        });
    }
    @Test
    void loadpageWithWrongPath() {

        interact(() -> {
            assertThrows(Exception.class, () ->
                    pageLoader.loadPage("/fxml/not_found.fxml", "test")
            );
        });
    }

    @Test
    void loadPopupWithWrongPath() {

        interact(() -> {
            assertThrows(Exception.class, () ->
                    pageLoader.loadPopUp("/fxml/not_found.fxml", "test")
            );
        });
    }
    @Test
    void initialize() {
        interact(() -> {
            assertThrows(Exception.class, () -> pageLoader.initialize(null));
            assertDoesNotThrow( () -> pageLoader.initialize(new Stage()));
        });

    }
}