package controller;

import javafx.stage.Stage;
import javafx.stage.Window;
import util.I18n;
import util.PageLoader;
import view.AdminPanel;

import java.util.Locale;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for handling user logout functionality.
 * Manages session cleanup and navigation back to login screen.
 */
public class LogoutController {

    Session session = Session.getInstance();

    /**
     * Handles the logout process.
     * Clears user session and navigates to login screen.
     * @throws IOException if login view FXML cannot be loaded
     */
    public void onLogout() throws IOException {

        session.clear();

        List<Window> windows = new ArrayList<>(Window.getWindows());
        for (Window w : windows) {
            try {
                if (w instanceof Stage) {
                    ((Stage) w).close();
                } else {
                    w.hide();
                }
            } catch (Exception ignored) {
            }
        }
        I18n.setLocale(Locale.ENGLISH);
        session.setLanguage("en");
        AdminPanel.setClosed();

        PageLoader.getInstance().loadPage("/fxml/loginView.fxml", "Login");
    }
}