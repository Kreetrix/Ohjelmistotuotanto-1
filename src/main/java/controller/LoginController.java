package controller;

import util.PageLoader;
import util.PasswordUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.beans.binding.Bindings;
import util.I18n;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.dao.AppUsersDao;
import model.entity.AppUsers;
import util.AudioPlayer;
import java.awt.*;
import java.util.Locale;

/**
 * Controller for the Login view handling user authentication.
 * Manages login process, validation, and navigation to main application.
 */
public class LoginController {


    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginBtn;

    @FXML
    private Button registerBtn;

    @FXML
    private Label errorLabel;

    @FXML
    private Label loginTitleLabel;

    @FXML
    private Button hebrewBtn;

    @FXML
    private VBox rootBox;

    /**
     * Initializes the controller after FXML loading.
     * Redirects to main view if user is already logged in.
     */
    @FXML
    protected void initialize() {
        try {
            loginTitleLabel.textProperty().bind(
                    Bindings.createStringBinding(() -> I18n.get("login.title"), I18n.localeProperty()));
            loginBtn.textProperty().bind(
                    Bindings.createStringBinding(() -> I18n.get("login.loginBtn"), I18n.localeProperty()));
            registerBtn.textProperty().bind(
                    Bindings.createStringBinding(() -> I18n.get("login.registerBtn"), I18n.localeProperty()));
            usernameField.promptTextProperty().bind(
                    Bindings.createStringBinding(() -> I18n.get("login.username"), I18n.localeProperty()));
            passwordField.promptTextProperty().bind(
                    Bindings.createStringBinding(() -> I18n.get("login.password"), I18n.localeProperty()));
        } catch (Exception ignored) {
        }

        if (Session.getInstance().getCurrentUser() != null) {
            Platform.runLater(() -> redirectToMain());
        }
    }

    /**
     * Redirects to the main application view.
     */
    private void redirectToMain() {
        PageLoader.getInstance().loadPage("/fxml/main.fxml", I18n.get("app.title"));
    }

    /**
     * Handles the login process when login button is clicked.
     * Validates credentials and redirects to main view on success.
     */
    @FXML
    private void onLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        AppUsersDao appUsersDao = new AppUsersDao();
        try {
            AppUsers user = appUsersDao.getUserByUsername(username);

            if (user == null) {
                errorLabel.setText(I18n.get("login.userNotFound"));
            } else {
                if (username.equals(user.getUsername())
                        && PasswordUtil.checkPassword(password, user.getPassword_hash())) {

                    Session.getInstance().setCurrentUser(user);

                    PageLoader.getInstance().loadPage("/fxml/main.fxml", I18n.get("app.title"));
                    user.setIs_active(1);

                } else {
                    errorLabel.setText(I18n.get("login.invalidCredentials"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Navigates to the registration view.
     */
    public void toRegister() {

        PageLoader.getInstance().loadPage(("/fxml/registerView.fxml"), I18n.get("register.title"));
    }

    /**
     * Changes the login language to Hebrew and sets the node orientation to
     * right-to-left.
     */
    public void toHebrew() {
        AudioPlayer.play("/music/Hebrew.mp3", 6000);
        I18n.setLocale(new Locale("he"));
        rootBox.setNodeOrientation(javafx.geometry.NodeOrientation.RIGHT_TO_LEFT);
        usernameField.setNodeOrientation(javafx.geometry.NodeOrientation.RIGHT_TO_LEFT);
        passwordField.setNodeOrientation(javafx.geometry.NodeOrientation.RIGHT_TO_LEFT);
        errorLabel.setNodeOrientation(javafx.geometry.NodeOrientation.RIGHT_TO_LEFT);
        loginTitleLabel.setNodeOrientation(javafx.geometry.NodeOrientation.RIGHT_TO_LEFT);
        loginBtn.setNodeOrientation(javafx.geometry.NodeOrientation.RIGHT_TO_LEFT);
        registerBtn.setNodeOrientation(javafx.geometry.NodeOrientation.RIGHT_TO_LEFT);
    }
}