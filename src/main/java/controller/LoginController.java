package controller;

import datasource.PasswordUtil;
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

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginBtn;

    @FXML
    private Label errorLabel;

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
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
                        Parent root = loader.load();
                        Stage stage = (Stage) loginBtn.getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.setTitle("App");
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

        //just test user TODO: REMOVE WHEN NOT NEEDED
        if (username.equals("user") && password.equals("1234")) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) loginBtn.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            errorLabel.setText("Invalid username or password");
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
