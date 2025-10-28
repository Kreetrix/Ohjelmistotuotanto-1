package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import util.I18n;
import util.PageLoader;

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
    Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    double width = screenSize.getWidth() / 3;
    double height = screenSize.getHeight() / 1.2;

    /**
     * Handles the logout process.
     * Clears user session and navigates to login screen.
     * @throws IOException if login view FXML cannot be loaded
     */
    public void onLogout() throws IOException {
        if (session.getCurrentUser() != null) {
            session.getCurrentUser().setIs_active(0);
        }
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

        PageLoader.getInstance().loadPage("/fxml/loginView.fxml", "Login");
    }
}