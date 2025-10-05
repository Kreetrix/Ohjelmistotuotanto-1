package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

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
        session.getCurrentUser().setIs_active(0);
        session.clear();

        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/fxml/loginView.fxml"));
        Parent loginRoot = loginLoader.load();
        Stage loginStage = new Stage();
        loginStage.setScene(new Scene(loginRoot, width, height));
        loginStage.setTitle("Login");
        loginStage.show();
    }
}