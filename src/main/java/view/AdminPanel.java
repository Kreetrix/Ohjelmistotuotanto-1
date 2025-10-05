package view;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.dao.AppUsersDao;
import model.dao.DecksDao;
import model.entity.AppUsers;
import model.entity.Decks;

import java.sql.SQLException;
import java.sql.Timestamp;

public class AdminPanel extends Application {

    private static Stage currentStage = null;
    private static boolean isOpen = false;

    private AppUsersDao usersDao = new AppUsersDao();
    private DecksDao decksDao = new DecksDao();
    
    private TableView<AppUsers> usersTable = new TableView<>();
    private TableView<Decks> decksTable = new TableView<>();
    
    private ObservableList<AppUsers> usersList = FXCollections.observableArrayList();
    private ObservableList<Decks> decksList = FXCollections.observableArrayList();

    public static boolean isAdminPanelOpen() {
        return isOpen;
    }

    @Override
    public void start(Stage primaryStage) {
        if (isOpen && currentStage != null) {
            currentStage.toFront();
            currentStage.requestFocus();
            return;
        }

        currentStage = primaryStage;
        isOpen = true;

        primaryStage.setTitle("Admin Panel");

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab studentsTab = new Tab("Students");
        studentsTab.setContent(createStudentsPanel());

        Tab decksTab = new Tab("Decks");
        decksTab.setContent(createDecksPanel());

        tabPane.getTabs().addAll(studentsTab, decksTab);

        Scene scene = new Scene(tabPane, 1000, 600);
        primaryStage.setScene(scene);
        
        primaryStage.setOnCloseRequest(e -> {
            isOpen = false;
            currentStage = null;
        });
        
        primaryStage.show();

        loadData();
    }

    private VBox createStudentsPanel() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        Label title = new Label("Student Management");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        setupUsersTable();

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button refreshBtn = new Button("Refresh");
        refreshBtn.setOnAction(e -> loadUsers());

        Button toggleActiveBtn = new Button("Toggle Active/Inactive");
        toggleActiveBtn.setOnAction(e -> toggleUserActive());

        Button updateBtn = new Button("Update Student");
        updateBtn.setOnAction(e -> updateStudent());

        Button deleteBtn = new Button("Delete Student");
        deleteBtn.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white;");
        deleteBtn.setOnAction(e -> deleteStudent());

        buttonBox.getChildren().addAll(refreshBtn, toggleActiveBtn, updateBtn, deleteBtn);

        vbox.getChildren().addAll(title, usersTable, buttonBox);
        VBox.setVgrow(usersTable, Priority.ALWAYS);

        return vbox;
    }

    private VBox createDecksPanel() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        Label title = new Label("Deck Management");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        setupDecksTable();

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button refreshBtn = new Button("Refresh");
        refreshBtn.setOnAction(e -> loadDecks());

        Button updateBtn = new Button("Update Deck");
        updateBtn.setOnAction(e -> updateDeck());

        Button deleteBtn = new Button("Delete Deck");
        deleteBtn.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white;");
        deleteBtn.setOnAction(e -> deleteDeck());

        buttonBox.getChildren().addAll(refreshBtn, updateBtn, deleteBtn);

        vbox.getChildren().addAll(title, decksTable, buttonBox);
        VBox.setVgrow(decksTable, Priority.ALWAYS);

        return vbox;
    }

    private void setupUsersTable() {
        TableColumn<AppUsers, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("user_id"));
        idCol.setPrefWidth(50);

        TableColumn<AppUsers, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        usernameCol.setPrefWidth(150);

        TableColumn<AppUsers, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailCol.setPrefWidth(200);

        TableColumn<AppUsers, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        roleCol.setPrefWidth(100);

        TableColumn<AppUsers, String> activeCol = new TableColumn<>("Active");
        activeCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getIs_active() == 1 ? "Yes" : "No"));
        activeCol.setPrefWidth(80);

        TableColumn<AppUsers, Timestamp> createdCol = new TableColumn<>("Created At");
        createdCol.setCellValueFactory(new PropertyValueFactory<>("created_at"));
        createdCol.setPrefWidth(180);

        usersTable.getColumns().addAll(idCol, usernameCol, emailCol, roleCol, activeCol, createdCol);
        usersTable.setItems(usersList);
    }

    private void setupDecksTable() {
        TableColumn<Decks, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("deck_id"));
        idCol.setPrefWidth(50);

        TableColumn<Decks, String> nameCol = new TableColumn<>("Deck Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("deck_name"));
        nameCol.setPrefWidth(200);

        TableColumn<Decks, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        descCol.setPrefWidth(250);

        TableColumn<Decks, Integer> userIdCol = new TableColumn<>("User ID");
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("user_id"));
        userIdCol.setPrefWidth(80);

        TableColumn<Decks, String> visibilityCol = new TableColumn<>("Visibility");
        visibilityCol.setCellValueFactory(new PropertyValueFactory<>("visibility"));
        visibilityCol.setPrefWidth(100);

        TableColumn<Decks, Integer> versionCol = new TableColumn<>("Version");
        versionCol.setCellValueFactory(new PropertyValueFactory<>("version"));
        versionCol.setPrefWidth(80);

        TableColumn<Decks, String> deletedCol = new TableColumn<>("Deleted");
        deletedCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().isIs_deleted() ? "Yes" : "No"));
        deletedCol.setPrefWidth(80);

        decksTable.getColumns().addAll(idCol, nameCol, descCol, userIdCol, visibilityCol, versionCol, deletedCol);
        decksTable.setItems(decksList);
    }

    private void loadData() {
        loadUsers();
        loadDecks();
    }

    private void loadUsers() {
        try {
            usersList.clear();
            usersList.addAll(usersDao.getAllUsers());
        } catch (SQLException e) {
            showError("Failed to load users", e.getMessage());
        }
    }

    private void loadDecks() {
        try {
            decksList.clear();
            decksList.addAll(decksDao.getAllDecks());
        } catch (SQLException e) {
            showError("Failed to load decks", e.getMessage());
        }
    }

    private void toggleUserActive() {
        AppUsers selected = usersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("No Selection", "Please select a student to toggle active status.");
            return;
        }

        try {
            boolean newStatus = selected.getIs_active() == 0;
            usersDao.setActive(newStatus, selected.getUser_id());
            showInfo("Success", "Student status updated successfully.");
            loadUsers();
        } catch (SQLException e) {
            showError("Update Failed", e.getMessage());
        }
    }

    private void updateStudent() {
        AppUsers selected = usersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("No Selection", "Please select a student to update.");
            return;
        }

        Dialog<AppUsers> dialog = new Dialog<>();
        dialog.setTitle("Update Student");
        dialog.setHeaderText("Update student information");

        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField usernameField = new TextField(selected.getUsername());
        TextField emailField = new TextField(selected.getEmail());
        ComboBox<String> roleCombo = new ComboBox<>();
        roleCombo.getItems().addAll("student", "teacher", "admin");
        roleCombo.setValue(selected.getRole());

        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(new Label("Role:"), 0, 2);
        grid.add(roleCombo, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                selected.setUsername(usernameField.getText());
                selected.setEmail(emailField.getText());
                selected.setRole(roleCombo.getValue());
                return selected;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(user -> {
            try {
                usersDao.updateUser(user);
                showInfo("Success", "Student updated successfully.");
                loadUsers();
            } catch (SQLException e) {
                showError("Update Failed", e.getMessage());
            }
        });
    }

    private void deleteStudent() {
        AppUsers selected = usersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("No Selection", "Please select a student to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Deletion");
        confirm.setHeaderText("Delete Student");
        confirm.setContentText("Are you sure you want to deactivate this student?\n" +
                "Username: " + selected.getUsername());

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    usersDao.setActive(false, selected.getUser_id());
                    showInfo("Success", "Student deactivated successfully.");
                    loadUsers();
                } catch (SQLException e) {
                    showError("Deletion Failed", e.getMessage());
                }
            }
        });
    }

    private void updateDeck() {
        Decks selected = decksTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("No Selection", "Please select a deck to update.");
            return;
        }

        Dialog<Decks> dialog = new Dialog<>();
        dialog.setTitle("Update Deck");
        dialog.setHeaderText("Update deck information");

        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField nameField = new TextField(selected.getDeck_name());
        TextArea descArea = new TextArea(selected.getDescription());
        descArea.setPrefRowCount(3);
        ComboBox<String> visibilityCombo = new ComboBox<>();
        visibilityCombo.getItems().addAll("public", "private", "shared");
        visibilityCombo.setValue(selected.getVisibility());

        grid.add(new Label("Deck Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descArea, 1, 1);
        grid.add(new Label("Visibility:"), 0, 2);
        grid.add(visibilityCombo, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                selected.setDeck_name(nameField.getText());
                selected.setDescription(descArea.getText());
                selected.setVisibility(visibilityCombo.getValue());
                return selected;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(deck -> {
            try {
                decksDao.updateDeck(deck);
                showInfo("Success", "Deck updated successfully.");
                loadDecks();
            } catch (SQLException e) {
                showError("Update Failed", e.getMessage());
            }
        });
    }

    private void deleteDeck() {
        Decks selected = decksTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("No Selection", "Please select a deck to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Deletion");
        confirm.setHeaderText("Delete Deck");
        confirm.setContentText("Are you sure you want to delete this deck?\n" +
                "Deck Name: " + selected.getDeck_name());

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    decksDao.isDeleted(true, selected.getDeck_id());
                    showInfo("Success", "Deck deleted successfully.");
                    loadDecks();
                } catch (SQLException e) {
                    showError("Deletion Failed", e.getMessage());
                }
            }
        });
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
