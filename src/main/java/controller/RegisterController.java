package controller;

import datasource.MariaDbJpaConnection;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.dao.AppUsersDao;
import model.entity.AppUsers;

import java.awt.*;
import java.sql.Connection;

public class RegisterController {
    @FXML
    private ComboBox<String> roleComboBox;
    @FXML
    private TextField emailField;
    @FXML
    private javafx.scene.control.TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private javafx.scene.control.Button loginBtn;

    @FXML
    private javafx.scene.control.Label errorLabel;

    AppUsersDao dao = new AppUsersDao();

    public void initialize(){
        roleComboBox.getItems().addAll("Student", "Teacher", "Admin");
        roleComboBox.setValue("Student");
    }
    public void onRegister(){
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String role = roleComboBox.getValue();
        String email = emailField.getText();

        if (password.equals(confirmPassword)){
            AppUsers user = new AppUsers(username, email, password, role, 0, null);
            try{
                dao.persist(user);
            } catch (Exception e) {
                System.out.println(e);
            }

        }
        else{
        errorLabel.setText("Passwords do not match");}
    }

}
