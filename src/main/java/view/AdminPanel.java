package view;

import controller.Session;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
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
import util.I18n;
import util.PageLoader;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;

public class AdminPanel extends Application {

    private static Stage currentStage = null;
    private static boolean isOpen = false;

    private final AppUsersDao usersDao = new AppUsersDao();
    private final DecksDao decksDao = new DecksDao();

    private final TableView<AppUsers> usersTable = new TableView<>();
    private final TableView<Decks> decksTable = new TableView<>();

    private final ObservableList<AppUsers> usersList = FXCollections.observableArrayList();
    private final ObservableList<Decks> decksList = FXCollections.observableArrayList();

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

        HBox langBox = new HBox(10);
        langBox.setAlignment(Pos.CENTER_RIGHT);
        langBox.setPadding(new Insets(10));

        Label langLabel = new Label();
        langLabel.textProperty()
                .bind(Bindings.createStringBinding(() -> I18n.get("label.lang"), I18n.localeProperty()));

        ComboBox<String> langCombo = new ComboBox<>();
        List<String> availableLanguages = List.of("en", "ru", "ja");
        langCombo.getItems().addAll(availableLanguages);

        String currentLang = Session.getInstance().getLanguage();
        if (currentLang == null || currentLang.isEmpty()) {
            currentLang = I18n.getLocale().getLanguage();
        }
        langCombo.setValue(currentLang);

        langCombo.setOnAction(e -> {
            String selected = langCombo.getValue();
            if (selected != null) {
                I18n.setLocale(new Locale(selected));
                Session.getInstance().setLanguage(selected);
                PageLoader.getInstance().reloadCurrentPage();
            }
        });

        langBox.getChildren().addAll(langLabel, langCombo);

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab studentsTab = new Tab();
        studentsTab.textProperty()
                .bind(Bindings.createStringBinding(() -> I18n.get("admin.studentsTab"), I18n.localeProperty()));
        studentsTab.setContent(createStudentsPanel());

        Tab decksTab = new Tab();
        decksTab.textProperty()
                .bind(Bindings.createStringBinding(() -> I18n.get("admin.decksTab"), I18n.localeProperty()));
        decksTab.setContent(createDecksPanel());

        tabPane.getTabs().addAll(studentsTab, decksTab);

        VBox mainLayout = new VBox(langBox, tabPane);
        VBox.setVgrow(tabPane, Priority.ALWAYS);

        Scene scene = new Scene(mainLayout, 1000, 600);
        primaryStage.titleProperty()
                .bind(Bindings.createStringBinding(() -> I18n.get("admin.title"), I18n.localeProperty()));
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

        Label title = new Label();
        title.textProperty()
                .bind(Bindings.createStringBinding(() -> I18n.get("admin.studentManagement"), I18n.localeProperty()));
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        setupUsersTable();

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button refreshBtn = new Button();
        refreshBtn.textProperty()
                .bind(Bindings.createStringBinding(() -> I18n.get("admin.refresh"), I18n.localeProperty()));
        refreshBtn.setOnAction(e -> loadUsers());

        Button toggleActiveBtn = new Button();
        toggleActiveBtn.textProperty()
                .bind(Bindings.createStringBinding(() -> I18n.get("admin.toggleActive"), I18n.localeProperty()));
        toggleActiveBtn.setOnAction(e -> toggleUserActive());

        Button updateBtn = new Button();
        updateBtn.textProperty()
                .bind(Bindings.createStringBinding(() -> I18n.get("admin.updateStudent"), I18n.localeProperty()));
        updateBtn.setOnAction(e -> updateStudent());

        Button deleteBtn = new Button();
        deleteBtn.textProperty()
                .bind(Bindings.createStringBinding(() -> I18n.get("admin.deleteStudent"), I18n.localeProperty()));
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

        Label title = new Label();
        title.textProperty()
                .bind(Bindings.createStringBinding(() -> I18n.get("admin.deckManagement"), I18n.localeProperty()));
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        setupDecksTable();

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button refreshBtn = new Button();
        refreshBtn.textProperty()
                .bind(Bindings.createStringBinding(() -> I18n.get("admin.refresh"), I18n.localeProperty()));
        refreshBtn.setOnAction(e -> loadDecks());

        Button updateBtn = new Button();
        updateBtn.textProperty()
                .bind(Bindings.createStringBinding(() -> I18n.get("admin.updateDeck"), I18n.localeProperty()));
        updateBtn.setOnAction(e -> updateDeck());

        Button deleteBtn = new Button();
        deleteBtn.textProperty()
                .bind(Bindings.createStringBinding(() -> I18n.get("admin.deleteDeck"), I18n.localeProperty()));
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
        activeCol.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getIs_active() == 1 ? "Yes" : "No"));
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
        deletedCol.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().isIs_deleted() ? "Yes" : "No"));
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
            showError(I18n.get("admin.failed"), e.getMessage());
        }
    }

    private void loadDecks() {
        try {
            decksList.clear();
            decksList.addAll(decksDao.getAllDecks());
        } catch (SQLException e) {
            showError(I18n.get("admin.failed"), e.getMessage());
        }
    }

    private void toggleUserActive() {
        AppUsers selected = usersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning(I18n.get("admin.noSelection"), I18n.get("admin.selectStudent"));
            return;
        }

        try {
            boolean newStatus = selected.getIs_active() == 0;
            usersDao.setActive(newStatus, selected.getUser_id());
            showInfo(I18n.get("admin.success"), I18n.get("admin.toggleActive"));
            loadUsers();
        } catch (SQLException e) {
            showError(I18n.get("admin.failed"), e.getMessage());
        }
    }

    private void updateStudent() {
        AppUsers selected = usersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning(I18n.get("admin.noSelection"), I18n.get("admin.selectStudent"));
            return;
        }

        Dialog<AppUsers> dialog = new Dialog<>();
        dialog.setTitle(I18n.get("admin.updateStudent"));
        dialog.setHeaderText(I18n.get("admin.updateStudent"));

        ButtonType updateButtonType = new ButtonType(I18n.get("admin.updateStudent"), ButtonBar.ButtonData.OK_DONE);
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
                showInfo(I18n.get("admin.success"), I18n.get("admin.updateStudent"));
                loadUsers();
            } catch (SQLException e) {
                showError(I18n.get("admin.failed"), e.getMessage());
            }
        });
    }

    private void deleteStudent() {
        AppUsers selected = usersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning(I18n.get("admin.noSelection"), I18n.get("admin.selectStudent"));
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle(I18n.get("admin.deleteStudent"));
        confirm.setHeaderText(I18n.get("admin.deleteStudent"));
        confirm.setContentText(selected.getUsername());

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    usersDao.setActive(false, selected.getUser_id());
                    showInfo(I18n.get("admin.success"), I18n.get("admin.deleteStudent"));
                    loadUsers();
                } catch (SQLException e) {
                    showError(I18n.get("admin.failed"), e.getMessage());
                }
            }
        });
    }

    private void updateDeck() {
        Decks selected = decksTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning(I18n.get("admin.noSelection"), I18n.get("admin.selectDeck"));
            return;
        }

        Dialog<Decks> dialog = new Dialog<>();
        dialog.setTitle(I18n.get("admin.updateDeck"));
        dialog.setHeaderText(I18n.get("admin.updateDeck"));

        ButtonType updateButtonType = new ButtonType(I18n.get("admin.updateDeck"), ButtonBar.ButtonData.OK_DONE);
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
                showInfo(I18n.get("admin.success"), I18n.get("admin.updateDeck"));
                loadDecks();
            } catch (SQLException e) {
                showError(I18n.get("admin.failed"), e.getMessage());
            }
        });
    }

    private void deleteDeck() {
        Decks selected = decksTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning(I18n.get("admin.noSelection"), I18n.get("admin.selectDeck"));
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle(I18n.get("admin.deleteDeck"));
        confirm.setHeaderText(I18n.get("admin.deleteDeck"));
        confirm.setContentText(selected.getDeck_name());

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    decksDao.isDeleted(true, selected.getDeck_id());
                    showInfo(I18n.get("admin.success"), I18n.get("admin.deleteDeck"));
                    loadDecks();
                } catch (SQLException e) {
                    showError(I18n.get("admin.failed"), e.getMessage());
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

    public static void setClosed() {
        isOpen = false;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
