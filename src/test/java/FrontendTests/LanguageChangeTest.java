package FrontendTests;

import controller.Session;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.NodeOrientation;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import util.I18n;

import java.util.Locale;

import static org.testfx.assertions.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests that the login view updates its texts when the application locale
 * changes
 * and that selecting Hebrew switches layout to right-to-left.
 */
class LanguageChangeTest extends ApplicationTest {

    private Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/loginView.fxml"));
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    private void reloadLogin() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/loginView.fxml"));
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    @AfterAll
    static void cleanUp(){
        Session.getInstance().clear();
        I18n.setLocale(Locale.ENGLISH);
    }

    @Test
    void englishTextsAreShown() {
        interact(() -> I18n.setLocale(Locale.ENGLISH));
        interact(() -> {
            try {
                reloadLogin();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        assertThat(lookup("#loginBtn").queryButton()).hasText("Log In");
        assertEquals("Username", lookup("#usernameField").queryTextInputControl().getPromptText());
    }

    @Test
    void russianTextsAreShown()  {
        interact(() -> I18n.setLocale(new Locale("ru")));
        interact(() -> {
            try {
                reloadLogin();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        assertThat(lookup("#loginBtn").queryButton()).hasText("Войти");
        assertThat(lookup("#registerBtn").queryButton()).hasText("Регистрация");
    }

    @Test
    void japaneseTextsAreShown() {
        interact(() -> I18n.setLocale(new Locale("ja")));
        interact(() -> {
            try {
                reloadLogin();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        assertThat(lookup("#loginBtn").queryButton()).hasText("ログイン");
        assertEquals("ユーザー名", lookup("#usernameField").queryTextInputControl().getPromptText());
    }

    @Test
    void hebrewButtonSwitchesToHebrewAndRtl() {
        interact(() -> I18n.setLocale(Locale.ENGLISH));
        interact(() -> {
            try {
                reloadLogin();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        clickOn("#hebrewBtn");
        assertThat(lookup("#loginTitleLabel").queryLabeled()).hasText("התחברות!");
        javafx.scene.Node root = lookup("#rootBox").query();
        assertEquals(NodeOrientation.RIGHT_TO_LEFT, root.getNodeOrientation());
    }

}
