package util;

import java.awt.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


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
public final class PageLoader {

    /**
     * Singleton instance.
     */
    private static PageLoader instance;
    //default sizes for window
    private final double height = getScreenSize().getHeight() / 1.2;
    private final double width = getScreenSize().getWidth() / 3;
    /**
     * The last loaded FXML resource path (as passed to {@link #loadPage}).
     */
    private String currentPath;
    /**
     * The last set window title (as passed to {@link #loadPage}).
     */
    private String currentTitle;
    /**
     * The reusable stage used by {@link #loadPage}. If null, a new Stage will be created
     * when first loading a page.
     */
    private Stage mainStage;


    private PageLoader() {
    }

    public void initialize(Stage stage) {
        if(mainStage != null) {
            return;
        }
        this.mainStage = stage;
        mainStage.setResizable(true);
        mainStage.setTitle("Memory Master - Login");
    }

    /**
     * Returns the singleton instance of PageLoader, creating it if necessary.
     *
     * @return the singleton PageLoader
     */
    public static PageLoader getInstance() {
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
     * @param path       the classpath resource path to the FXML file (e.g. "/fxml/main.fxml")
     * @param stageTitle the title to display on the Stage window
     */
    public void loadPage(String path, String stageTitle) {
        loadPage(path, stageTitle, "Error loading page: ");
    }

    /**
     * Loads an FXML page into a reusable stage with a custom error-prefix string.
     * This behaves like {@link #loadPage(String, String)} but, when an exception occurs,
     * the provided error prefix is printed before the exception message. This allows
     * callers to supply contextual text that will appear in the error log.
     *
     * @param path       the classpath resource path to the FXML file
     * @param stageTitle the title to display on the Stage window
     * @param er         a custom error-prefix string that will be prepended to any exception message
     */
    public Stage loadPage(String path, String stageTitle, String er) {
        try {
            currentPath = path;
            currentTitle = stageTitle;

            javafx.fxml.FXMLLoader loader = new FXMLLoader(getClass().getResource(path));

            Parent root = loader.load();

            Scene scene = new Scene(root, width, height);


            mainStage.setScene(scene);
            mainStage.setTitle(stageTitle);
            mainStage.show();

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(er);
        }
        return mainStage;
    }

    public Stage loadPopUp(String path, String stageTitle) {
        return loadPopUp(path, stageTitle, this.width, this.height);
    }

    /**
     * Loads an FXML page into a new pop-up stage. A fresh Stage is created on each call.
     * The method computes a default scene size based on the current screen dimensions.
     *
     * @param path       the classpath resource path to the FXML file
     * @param stageTitle the title to display on the pop-up Stage
     * @param width      the width of the popup
     * @param height     the height of the popup
     **/


    public Stage loadPopUp(String path, String stageTitle, double width, double height) {
        try {

            javafx.fxml.FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Parent root = loader.load();
            Scene scene = new Scene(root, width, height);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initOwner(mainStage);
            stage.setTitle(stageTitle);
            stage.showAndWait();
            return stage;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
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
