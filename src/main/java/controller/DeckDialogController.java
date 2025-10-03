package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.entity.Decks;
import model.dao.DecksDao;
import java.sql.SQLException;
import java.sql.Timestamp;

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
    
    public void initialize() {
        decksDao = new DecksDao();
        setupEventHandlers();
        setupVisibilityComboBox();
        styleVisibilityComboBox();
    }
    
    private void setupEventHandlers() {
        saveButton.setOnAction(e -> handleSave());
        cancelButton.setOnAction(e -> handleCancel());
        
        // Validation listeners
        deckNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateInput();
        });
    }
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
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
            visibilityComboBox.setValue("private"); // Default to private
        }
        
        validateInput();
    }
    
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
        
        if (!valid && !errorMessage.isEmpty()) {
            // You could show error message somewhere if needed
        }
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
                        1, // Initial version
                        visibilityComboBox.getValue(),
                        false, // Not deleted
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
        // Add visibility options
        visibilityComboBox.getItems().addAll("private", "public", "assigned");
        
        // Set default value
        visibilityComboBox.setValue("private");
    }

    private void styleVisibilityComboBox() {
        // Apply additional styling to ComboBox to ensure text visibility
        visibilityComboBox.setStyle(
            "-fx-background-color: #333333;" +
            "-fx-border-color: transparent;" +
            "-fx-border-width: 0;" +
            "-fx-background-radius: 0;" +
            "-fx-border-radius: 0;"
        );
        
        // Style the ComboBox when it's shown
        visibilityComboBox.setOnShown(e -> {
            visibilityComboBox.lookupAll(".list-cell").forEach(node -> {
                node.setStyle("-fx-background-color: #333333; -fx-text-fill: #ffffff;");
            });
        });
        
        // Apply cell factory for better text visibility
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
                    // Style the cell
                    setStyle("-fx-background-color: #333333; -fx-text-fill: #ffffff;");
                }
            };
        });
        
        // Style the button cell (the selected item display)
        visibilityComboBox.setButtonCell(new javafx.scene.control.ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Select visibility...");
                    setStyle("-fx-text-fill: #757575;"); // Placeholder color
                } else {
                    setText(capitalizeFirst(item));
                    setStyle("-fx-text-fill: #ffffff;"); // Selected text color
                }
                // Always set background
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