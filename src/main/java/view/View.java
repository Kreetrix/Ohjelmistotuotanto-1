package view;

import java.awt.Dimension;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main application class for the Memory Master flashcard application.
 * Handles application startup and initial window configuration.
 */
public final class View extends Application {

    /**
     * Main entry point for the JavaFX application.
     * @param stage the primary stage for this application
     * @throws Exception if FXML loading fails
     */
    @Override
    public void start(Stage stage) throws Exception {
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth() / 3;
        double height = screenSize.getHeight() / 1.2;

        // Start with login screen
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/fxml/loginView.fxml"));
        Parent loginRoot = loginLoader.load();
        Stage loginStage = new Stage();
        loginStage.setScene(new Scene(loginRoot, width, height));
        loginStage.setTitle("Login");
        loginStage.show();
    }

    /**
     * Main method to launch the application.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}