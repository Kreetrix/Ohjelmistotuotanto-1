package controller;

import datasource.PasswordUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private Label errorLabel;

    @FXML
    protected void initialize() {
        if (Session.getInstance().getCurrentUser() != null) {
            Platform.runLater(() -> redirectToMain());
        }
    }

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

    @FXML
    private void onLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        AppUsersDao appUsersDao = new AppUsersDao();
        try {
            AppUsers user = appUsersDao.getUserByUsername(username);

            if (user == null) {
                errorLabel.setText("User not found");
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
                    errorLabel.setText("Invalid username or password");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void toRegister( ) {
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
