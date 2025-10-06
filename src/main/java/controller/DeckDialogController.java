package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.entity.Decks;
import model.dao.DecksDao;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Controller for the Deck Dialog window used for creating and editing flashcard decks.
 * Handles deck creation, editing, validation, and saving operations.
 */
public class DeckDialogController {

    @FXML private TextField deckNameField;
    @FXML private TextField descriptionField;
    @FXML private ComboBox<String> visibilityComboBox;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    @FXML private Label titleLabel;

    private Stage dialogStage;
    private Decks deck;
    private boolean okClicked = false;
    private boolean isEditMode = false;
    private DecksDao decksDao;

    /**
     * Initializes the controller after FXML loading.
     * Sets up DAOs, event handlers, and UI components.
     */
    @FXML
    public void initialize() {
        decksDao = new DecksDao();
        setupEventHandlers();
        setupVisibilityComboBox();
        styleVisibilityComboBox();
    }

    private void setupEventHandlers() {
        saveButton.setOnAction(e -> handleSave());
        cancelButton.setOnAction(e -> handleCancel());

        deckNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateInput();
        });
    }

    /**
     * Sets the dialog stage for this controller.
     * @param dialogStage the stage containing this dialog
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Sets the deck to be edited. If null, the dialog will be in create mode.
     * @param deck the deck to edit, or null for create mode
     */
    public void setDeck(Decks deck) {
        this.deck = deck;

        if (deck != null) {
            isEditMode = true;
            titleLabel.setText("Edit Deck");
            deckNameField.setText(deck.getDeck_name());
            descriptionField.setText(deck.getDescription());
            visibilityComboBox.setValue(deck.getVisibility() != null ? deck.getVisibility() : "private");
        } else {
            isEditMode = false;
            titleLabel.setText("Create New Deck");
            deckNameField.clear();
            descriptionField.clear();
            visibilityComboBox.setValue("private");
        }

        validateInput();
    }

    /**
     * Returns whether the user clicked OK to save changes.
     * @return true if OK was clicked, false otherwise
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    private void validateInput() {
        boolean valid = true;
        String errorMessage = "";

        if (deckNameField.getText() == null || deckNameField.getText().trim().isEmpty()) {
            valid = false;
            errorMessage += "Deck name is required!\n";
        } else if (deckNameField.getText().trim().length() > 100) {
            valid = false;
            errorMessage += "Deck name must be less than 100 characters!\n";
        }

        if (descriptionField.getText() != null && descriptionField.getText().length() > 500) {
            valid = false;
            errorMessage += "Description must be less than 500 characters!\n";
        }

        saveButton.setDisable(!valid);
    }

    private void handleSave() {
        if (isInputValid()) {
            try {
                if (isEditMode) {
                    // Update existing deck
                    deck.setDeck_name(deckNameField.getText().trim());
                    deck.setDescription(descriptionField.getText() != null ? descriptionField.getText().trim() : "");
                    deck.setVisibility(visibilityComboBox.getValue());

                    decksDao.updateDeck(deck);
                    showInfo("Deck updated successfully!");
                } else {
                    // Create new deck
                    int currentUserId = Session.getInstance().getUserId();
                    if (currentUserId == -1) {
                        showError("User not logged in!");
                        return;
                    }

                    Decks newDeck = new Decks(
                            currentUserId,
                            deckNameField.getText().trim(),
                            descriptionField.getText() != null ? descriptionField.getText().trim() : "",
                            1,
                            visibilityComboBox.getValue(),
                            false,
                            new Timestamp(System.currentTimeMillis())
                    );

                    decksDao.persist(newDeck);
                    showInfo("Deck created successfully!");
                }

                okClicked = true;
                dialogStage.close();

            } catch (SQLException e) {
                showError("Database error: " + e.getMessage());
            }
        }
    }

    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (deckNameField.getText() == null || deckNameField.getText().trim().isEmpty()) {
            errorMessage += "Deck name is required!\n";
        } else if (deckNameField.getText().trim().length() > 100) {
            errorMessage += "Deck name must be less than 100 characters!\n";
        }

        if (descriptionField.getText() != null && descriptionField.getText().length() > 500) {
            errorMessage += "Description must be less than 500 characters!\n";
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            showError(errorMessage);
            return false;
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(dialogStage);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(dialogStage);
        alert.showAndWait();
    }

    private void setupVisibilityComboBox() {
        
        visibilityComboBox.getItems().addAll("private", "public", "assigned");

        
        visibilityComboBox.setValue("private");
    }

    private void styleVisibilityComboBox() {
        
        visibilityComboBox.setStyle(
                "-fx-background-color: #333333;" +
                        "-fx-border-color: transparent;" +
                        "-fx-border-width: 0;" +
                        "-fx-background-radius: 0;" +
                        "-fx-border-radius: 0;"
        );

        
        visibilityComboBox.setOnShown(e -> {
            visibilityComboBox.lookupAll(".list-cell").forEach(node -> {
                node.setStyle("-fx-background-color: #333333; -fx-text-fill: #ffffff;");
            });
        });

        
        visibilityComboBox.setCellFactory(listView -> {
            return new javafx.scene.control.ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(capitalizeFirst(item));
                    }
                    
                    setStyle("-fx-background-color: #333333; -fx-text-fill: #ffffff;");
                }
            };
        });

        
        visibilityComboBox.setButtonCell(new javafx.scene.control.ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Select visibility...");
                    setStyle("-fx-text-fill: #757575;");
                } else {
                    setText(capitalizeFirst(item));
                    setStyle("-fx-text-fill: #ffffff;");
                }
                
                setStyle(getStyle() + "-fx-background-color: #333333;");
            }
        });
    }

    private String capitalizeFirst(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}