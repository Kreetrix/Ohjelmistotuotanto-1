package view;

import javafx.application.Application;
import javafx.stage.Stage;
import util.Page;
import util.PageLoader;

/**
 * Main application class for the Memory Master flashcard application.
 * Handles application startup and initial window configuration.
 */
public final class View extends Application {
    PageLoader pageLoader = PageLoader.getInstance();

    /**
     * Main entry point for the JavaFX application.
     * @param stage the primary stage for this application
     * @throws Exception if FXML loading fails
     */
    @Override
    public void start(Stage stage) throws Exception {
        // Start with login screen
        pageLoader.initialize(stage);
        pageLoader.loadPage(Page.LOGIN);
    }

    /**
     * Main method to launch the application.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}