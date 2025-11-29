package util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Loads and shows JavaFX pages and pop-up windows.
 */
public final class PageLoader {

    private static Logger logger =  Logger.getLogger(PageLoader.class.getName());

    /**
     * Singleton instance of the page loader.
     */
    private static PageLoader instance;
    //default sizes for window
    private final double height = getScreenSize().getHeight() / 1.2;
    private final double width = getScreenSize().getWidth() / 3;
    private Page currentPage;
    /**
     * Main application stage used for standard pages.
     */
    private Stage mainStage;

    private PageLoader() {
    }

    /**
     * Returns the singleton instance of PageLoader.
     *
     * @return shared PageLoader instance
     */
    public static PageLoader getInstance() {
        if (instance == null) {
            instance = new PageLoader();
        }
        return instance;
    }

    /**
     * Initializes the main application stage.
     *
     * @param stage primary JavaFX stage
     */
    public void initialize(Stage stage) {
        if (mainStage != null) {
            return;
        }
        this.mainStage = stage;
        mainStage.setResizable(true);
        mainStage.setTitle("Memory Master - Login");
    }

    /**
     * Loads the given {@link Page} into the main stage.
     *
     * @param page page enum to load
     * @return the main stage showing the page
     */
    public Stage loadPage(Page page) {
        return loadPage(page.getPath(), page.getTitle());
    }

    /**
     * Loads an FXML page into the main stage.
     *
     * @param path       FXML resource path
     * @param stageTitle window title
     * @return the main stage showing the page
     */
    public Stage loadPage(String path, String stageTitle) {
        return loadPage(path, stageTitle, "Error loading page: " + path);
    }

    /**
     * Loads an FXML page with a custom error message prefix.
     *
     * @param path       FXML resource path
     * @param stageTitle window title
     * @param er         error message prefix
     * @return the main stage showing the page
     */
    public Stage loadPage(String path, String stageTitle, String er) {
        try {

            currentPage = Page.pageByPath(path);


            Parent root = loadFXML(path);

            Scene scene = new Scene(root, width, height);

            if (mainStage == null) {
                mainStage = setupStage(stageTitle, scene);
            }


            mainStage.setScene(scene);
            mainStage.setTitle(I18n.get(stageTitle));
            mainStage.show();

        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), er);
            loadPage(Page.MAIN);
            throw ex;
        }
        return mainStage;
    }

    /**
     * Opens a popup window with default size.
     *
     * @param path       FXML resource path
     * @param stageTitle window title
     * @param <T>        controller type
     * @return popup wrapper containing stage and controller
     */
    public <T> PopUp<T> loadPopUp(String path, String stageTitle) {
        return loadPopUp(path, stageTitle, this.width, this.height);
    }

    /**
     * Opens a popup window with custom size.
     *
     * @param path       FXML resource path
     * @param stageTitle window title
     * @param width      popup width
     * @param height     popup height
     * @param <T>        controller type
     * @return popup wrapper containing stage and controller
     */
    public <T> PopUp<T> loadPopUp(String path, String stageTitle, double width, double height) {
        return loadPopUp(path, stageTitle, width, height, mainStage);
    }

    /**
     * Opens a popup window with custom size and owner stage.
     *
     * @param path       FXML resource path
     * @param stageTitle window title
     * @param width      popup width
     * @param height     popup height
     * @param owner      owner stage for the popup
     * @param <T>        controller type
     * @return popup wrapper containing stage and controller
     */
    public <T> PopUp<T> loadPopUp(String path, String stageTitle, double width, double height, Stage owner) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Parent root = loader.load();

            T controller = loader.getController();

            Scene scene = new Scene(root, width, height);
            Stage stage = setupStageWithOwner(stageTitle, scene, owner);

            return new PopUp<>(stage, controller);
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new RuntimeException("Failed to load popup: " + path, e);
        }
    }

    /**
     * Loads an FXML file and returns its root node.
     *
     * @param path FXML resource path
     * @return loaded root node
     */
    private Parent loadFXML(String path) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            return loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a new stage with the given scene and title.
     *
     * @param stageTitle window title
     * @param scene      JavaFX scene
     * @return configured stage
     */
    private Stage setupStage(String stageTitle, Scene scene) {
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(stageTitle);
        return stage;
    }

    /**
     * Creates a new stage with an owner.
     *
     * @param stageTitle window title
     * @param scene      JavaFX scene
     * @param owner      owner stage
     * @return configured stage
     */
    private Stage setupStageWithOwner(String stageTitle, Scene scene, Stage owner) {
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(stageTitle);
        stage.initOwner(owner);
        return stage;
    }

    /**
     * Reloads the most recently shown page.
     */
    public void reloadCurrentPage() {
        if (currentPage != null) {
            loadPage(currentPage);
        }
    }

    /**
     * Navigates to the main application page.
     */
    public void goToHomePage() {
        loadPage(Page.MAIN);

    }

    /**
     * Returns the primary screen size.
     *
     * @return screen dimensions
     */
    private Dimension getScreenSize() {
        return java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    }

    /**
     * Simple holder for a popup stage and its controller.
     */
    public record PopUp<T>(Stage stage, T controller) {
    }
}
