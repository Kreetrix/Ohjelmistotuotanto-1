package controller;

import components.CustomButton;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import util.PageLoader;
import view.AdminPanel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for the navigation bar component.
 * Handles navigation actions and user logout.
 */
public class NavbarController {
    private final LogoutController logout = new LogoutController();
    private final Session sessionManager = Session.getInstance();
    private static final Logger logger = Logger.getLogger(StudyController.class.getName());

    @FXML
    private CustomButton logoutBtn;

    @FXML
    private CustomButton admin;

    @FXML
    private CustomButton listBtn;

    @FXML
    private CustomButton homeButton;

    /**
     * Initializes the navbar controller after FXML loading.
     * Sets up button icons, tooltips, and event handlers.
     */
    @FXML
    private void initialize() {

        listBtn.setIcon("/icons/list.png");
        listBtn.setTooltipText("List");
        listBtn.setOnAction(e -> System.out.println("List clicked"));
        listBtn.setVisible(false);
        // Back button
        setupBackButton();

        // Only for admin!!!
        setupAdminButton();

        logoutBtn.setIcon("/icons/logout.png");
        logoutBtn.setOnAction(e -> {

            logout.onLogout();

        });

        admin.setSvgIcon("admin", Color.LIGHTBLUE.toString(), 2);
        admin.setTooltipText("Admin");

    }

    private void setupAdminButton() {
        System.out.println(sessionManager.toString());
        if (sessionManager.isAdmin()) {

            admin.setVisible(true);
            admin.setManaged(true);
            admin.setOnAction(e -> openAdminPanel());
        } else {
            admin.setVisible(false);
            admin.setManaged(false);
        }
    }

    private void setupBackButton() {
        homeButton.setIcon("/icons/home.png");
        homeButton.setOnAction(e -> goHomepage());
    }

    private void goHomepage() {
        PageLoader.getInstance().goToHomePage();
    }

    private void openAdminPanel() {
        if (AdminPanel.isAdminPanelOpen()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Admin Panel");
            alert.setHeaderText(null);
            alert.setContentText("Admin panel is already open.");
            alert.showAndWait();
            return;
        }

        admin.setDisable(true);

        try {
            AdminPanel adminPanel = new AdminPanel();
            Stage adminStage = new Stage();

            adminStage.setOnHidden(e -> {
                admin.setDisable(false);
                System.out.println("Admin panel closed, button re-enabled");
            });

            adminPanel.start(adminStage);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in openAdminPanel");
            admin.setDisable(false);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Failed to open admin panel: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
