package controller;

import datasource.MariaDbJpaConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.dao.AppUsersDao;
import model.entity.AppUsers;
import util.I18n;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import util.PageLoader;


/**
 * Controller for the user registration view.
 * Handles new user registration with role selection and validation.
 */
public class RegisterController {

    @FXML
    public Button backBtn;
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
    private javafx.scene.control.Button registerBtn;

    @FXML
    private javafx.scene.control.Label errorLabel;

    @FXML
    private Label registerTitleLabel;

    AppUsersDao dao = new AppUsersDao();
    PageLoader pageLoader = PageLoader.getInstance();

    // TODO: ADD ADMIN PANEL FOR UPDATING/DELETING USERS

    /**
     * Initializes the registration controller after FXML loading.
     * Sets up the role selection combo box.
     */
    public void initialize(){


        roleComboBox.getItems().clear();
        roleComboBox.getItems().addAll("student", "teacher", "admin");

        roleComboBox.setCellFactory(cb -> new javafx.scene.control.ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(I18n.get("role." + item));
                }
            }
        });
        roleComboBox.setButtonCell(new javafx.scene.control.ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(I18n.get("role." + item));
                }
            }
        });

        roleComboBox.setValue("student");

    try {
        registerTitleLabel.textProperty().bind(
            Bindings.createStringBinding(() -> I18n.get("register.title"), I18n.localeProperty())
        );
        registerBtn.textProperty().bind(
            Bindings.createStringBinding(() -> I18n.get("register.registerBtn"), I18n.localeProperty())
        );
        usernameField.promptTextProperty().bind(
            Bindings.createStringBinding(() -> I18n.get("register.username"), I18n.localeProperty())
        );
        emailField.promptTextProperty().bind(
            Bindings.createStringBinding(() -> I18n.get("register.email"), I18n.localeProperty())
        );
        passwordField.promptTextProperty().bind(
            Bindings.createStringBinding(() -> I18n.get("register.password"), I18n.localeProperty())
        );
        confirmPasswordField.promptTextProperty().bind(
            Bindings.createStringBinding(() -> I18n.get("register.confirmPassword"), I18n.localeProperty())
        );
    } catch (Exception ignored) {}
    }

    /**
     * Handles the user registration process.
     * Validates input and creates new user account.
     */
    public void onRegister(){
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String role = roleComboBox.getValue();
        String email = emailField.getText();

        if (password.equals(confirmPassword)){
            AppUsers user = new AppUsers(username, email, password, role, 1, null);
            try{
                dao.persist(user);

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/loginView.fxml"));
                    Parent root = loader.load();
                    Stage stage = (Stage) registerBtn.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.setTitle(I18n.get("app.title"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                System.out.println(e);
            }

        }
        else{
            errorLabel.setText(I18n.get("register.passwordMismatch"));
        }
    }
    public void onBack() {
        pageLoader.loadPage(("/fxml/loginView.fxml"), I18n.get("login.title"));
    }
}