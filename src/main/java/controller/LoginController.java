package controller;

import datasource.PasswordUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.beans.binding.Bindings;
import util.I18n;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import model.dao.AppUsersDao;
import model.entity.AppUsers;

import java.awt.*;
import java.io.IOException;

/**
 * Controller for the Login view handling user authentication.
 * Manages login process, validation, and navigation to main application.
 */
public class LoginController {
    Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    double width = screenSize.getWidth() / 3;
    double height = screenSize.getHeight() / 1.2;

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

    /**
     * Initializes the controller after FXML loading.
     * Redirects to main view if user is already logged in.
     */
    @FXML
    protected void initialize() {
        try {
            loginTitleLabel.textProperty().bind(
                    Bindings.createStringBinding(() -> I18n.get("login.title"), I18n.localeProperty())
            );
            loginBtn.textProperty().bind(
                    Bindings.createStringBinding(() -> I18n.get("login.loginBtn"), I18n.localeProperty())
            );
        registerBtn.textProperty().bind(
            Bindings.createStringBinding(() -> I18n.get("login.registerBtn"), I18n.localeProperty())
        );
            usernameField.promptTextProperty().bind(
                    Bindings.createStringBinding(() -> I18n.get("login.username"), I18n.localeProperty())
            );
            passwordField.promptTextProperty().bind(
                    Bindings.createStringBinding(() -> I18n.get("login.password"), I18n.localeProperty())
            );
        } catch (Exception ignored) {}

        if (Session.getInstance().getCurrentUser() != null) {
            Platform.runLater(() -> redirectToMain());
        }
    }

    /**
     * Redirects to the main application view.
     */
    private void redirectToMain() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) loginBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Memory Master");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                if(username.equals(user.getUsername()) && PasswordUtil.checkPassword(password,user.getPassword_hash())){
                    try {
                        Session.getInstance().setCurrentUser(user);
                        user.setIs_active(1);
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
                        Parent root = loader.load();
                        Stage stage = (Stage) loginBtn.getScene().getWindow();

                        stage.setScene(new Scene(root,width,height));
                        stage.setTitle("Memory Master");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    errorLabel.setText(I18n.get("login.invalidCredentials"));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Navigates to the registration view.
     */
    public void toRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/registerView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) loginBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Register");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}