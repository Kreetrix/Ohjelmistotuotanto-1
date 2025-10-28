package util;

import controller.Session;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.awt.*;

/**
 * Utility singleton responsible for loading JavaFX pages and pop-up windows from FXML resources.
 *
 * <p>This class provides convenience methods to load an FXML file into a reusable primary
 * stage ({@link #loadPage(String, String)}) or into a fresh pop-up stage
 * ({@link #loadPopUp(String, String)}). An overloaded variant of {@link #loadPage(String, String)}
 * is available that accepts a custom error-prefix string to be included in exception messages
 * ({@link #loadPage(String, String, String)}). The class keeps track of the last loaded
 * path and title so the current page can be reloaded using {@link #reloadCurrentPage()}.
 */
public class PageLoader {
    /**
     * Singleton instance.
     */
     private static PageLoader instance = null;

    /**
     * The last loaded FXML resource path (as passed to {@link #loadPage}).
     */
     private String currentPath = null;

    /**
     * The last set window title (as passed to {@link #loadPage}).
     */
     private String currentTitle = null;

    /**
     * The reusable stage used by {@link #loadPage}. If null, a new Stage will be created
     * when first loading a page.
     */
     private Stage currentStage = null;

     private double height = getScreenSize().getHeight() / 1.2;
     private double width = getScreenSize().getWidth() / 3;


     private PageLoader() {
     }

    /**
     * Returns the singleton instance of PageLoader, creating it if necessary.
     *
     * @return the singleton PageLoader
     */
    public static  PageLoader getInstance() {
        if (instance == null) {
            instance = new PageLoader();
        }
        return instance;
    }

    /**
     * Loads an FXML page into a reusable stage. The stage is created on first use and
     * subsequently reused for future calls to this method. The method computes a default
     * scene size based on the current screen dimensions.
     *
     * @param path the classpath resource path to the FXML file (e.g. "/fxml/main.fxml")
     * @param StageTitle the title to display on the Stage window
     */
    public void loadPage(String path, String StageTitle) {
        try {
            currentPath = path;
            currentTitle = StageTitle;


            javafx.fxml.FXMLLoader loader = new FXMLLoader(getClass().getResource(path));

            Parent root = loader.load();

            Scene scene = new Scene(root, width, height);

            if (currentStage == null) {
                currentStage = new Stage();
            }
            currentStage.setScene(scene);
            currentStage.setTitle(StageTitle);
            currentStage.show();

        } catch (Exception ex) {
            System.err.println("Error opening creation window: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Loads an FXML page into a reusable stage with a custom error-prefix string.
     * This behaves like {@link #loadPage(String, String)} but, when an exception occurs,
     * the provided error prefix is printed before the exception message. This allows
     * callers to supply contextual text that will appear in the error log.
     *
     * @param path the classpath resource path to the FXML file
     * @param StageTitle the title to display on the Stage window
     * @param er a custom error-prefix string that will be prepended to any exception message
     */
    public void loadPage(String path, String StageTitle,String er) {
        try {
            currentPath = path;
            currentTitle = StageTitle;

            Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
            double width = screenSize.getWidth() / 3;
            double height = screenSize.getHeight() / 1.2;

            javafx.fxml.FXMLLoader loader = new FXMLLoader(getClass().getResource(path));

            Parent root = loader.load();

            Scene scene = new Scene(root, width, height);

            if (currentStage == null) {
                currentStage = new Stage();
            }
            currentStage.setScene(scene);
            currentStage.setTitle(StageTitle);
            currentStage.show();

        } catch (Exception ex) {
            System.err.println(er + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Loads an FXML page into a new pop-up stage. A fresh Stage is created on each call.
     * The method computes a default scene size based on the current screen dimensions.
     *
     * @param path the classpath resource path to the FXML file
     * @param StageTitle the title to display on the pop-up Stage
     */
    public void loadPopUp(String path, String StageTitle){
        try {


            javafx.fxml.FXMLLoader loader = new FXMLLoader(getClass().getResource(path));

            Parent root = loader.load();

            Scene scene = new Scene(root, width, height);

            Stage Stage = new Stage();
            Stage.setScene(scene);
            Stage.setTitle(StageTitle);
            Stage.show();

        } catch (Exception ex) {
            System.err.println("Error opening creation window: " + ex.getMessage());
            ex.printStackTrace();
        }

    }

    /**
     * Reloads the most recently loaded page (if any) by calling {@link #loadPage(String, String)}
     * with the stored path and title.
     */
    public void reloadCurrentPage() {
        if (currentPath != null && currentTitle != null) {
            loadPage(currentPath, currentTitle);
        }
    }
    private Dimension getScreenSize() {
        return java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    }
}
