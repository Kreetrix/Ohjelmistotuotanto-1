package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.entity.Cards;
import model.entity.Decks;
import model.dao.CardsDao;
import model.dao.DecksDao;
import java.sql.SQLException;
import java.util.List;

public class CardDialogController {
    
    @FXML private ComboBox<Decks> deckComboBox;
    @FXML private TextField frontTextField;
    @FXML private TextField backTextField;
    @FXML private TextField imageUrlField;
    @FXML private TextField extraInfoField;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    @FXML private Label titleLabel;
    
    private Stage dialogStage;
    private Cards card;
    private boolean okClicked = false;
    private boolean isEditMode = false;
    private CardsDao cardsDao;
    private DecksDao decksDao;
    
    public void initialize() {
        cardsDao = new CardsDao();
        decksDao = new DecksDao();
        setupEventHandlers();
        loadDecks();
        styleComboBox();
    }
    
    private void setupEventHandlers() {
        saveButton.setOnAction(e -> handleSave());
        cancelButton.setOnAction(e -> handleCancel());
        
        frontTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateInput();
        });
        
        backTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateInput();
        });
        
        deckComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            validateInput();
        });
    }
    
    private void loadDecks() {
        try {
            List<Decks> allDecks = decksDao.getAllDecks();
            deckComboBox.getItems().clear();
            
            // Filter decks based on user permissions
            String userRole = Session.getInstance().getRole();
            int currentUserId = Session.getInstance().getUserId();
            
            List<Decks> availableDecks = allDecks.stream()
                .filter(deck -> !deck.isIs_deleted())
                .filter(deck -> {
                    // Admin and teacher can use any deck
                    if ("admin".equalsIgnoreCase(userRole) || "teacher".equalsIgnoreCase(userRole)) {
                        return true;
                    }
                    // Students can only use their own decks or public decks
                    return deck.getUser_id() == currentUserId || "public".equals(deck.getVisibility());
                })
                .toList();
            
            deckComboBox.getItems().addAll(availableDecks);
            
            // Set up string converter for display
            deckComboBox.setConverter(new javafx.util.StringConverter<Decks>() {
                @Override
                public String toString(Decks deck) {
                    return deck != null ? deck.getDeck_name() : "";
                }
                
                @Override
                public Decks fromString(String string) {
                    return null;
                }
            });
            
        } catch (SQLException e) {
            showError("Failed to load decks: " + e.getMessage());
        }
    }
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    public void setCard(Cards card) {
        this.card = card;
        
        if (card != null) {
            isEditMode = true;
            titleLabel.setText("Edit Card");
            frontTextField.setText(card.getFront_text() != null ? card.getFront_text() : "");
            backTextField.setText(card.getBack_text() != null ? card.getBack_text() : "");
            imageUrlField.setText(card.getImage_url() != null ? card.getImage_url() : "");
            extraInfoField.setText(card.getExtra_info() != null ? card.getExtra_info() : "");
            
            // Select the appropriate deck
            for (Decks deck : deckComboBox.getItems()) {
                if (deck.getDeck_id() == card.getDeck_id()) {
                    deckComboBox.setValue(deck);
                    break;
                }
            }
        } else {
            isEditMode = false;
            titleLabel.setText("Create New Card");
            frontTextField.clear();
            backTextField.clear();
            imageUrlField.clear();
            extraInfoField.clear();
            deckComboBox.getSelectionModel().selectFirst();
        }
        
        validateInput();
    }
    
    public void setPreselectedDeck(Decks preselectedDeck) {
        if (preselectedDeck != null && !isEditMode) {
            deckComboBox.setValue(preselectedDeck);
        }
    }
    
    public boolean isOkClicked() {
        return okClicked;
    }
    
    private void validateInput() {
        boolean valid = true;
        
        if (frontTextField.getText() == null || frontTextField.getText().trim().isEmpty()) {
            valid = false;
        }
        
        if (backTextField.getText() == null || backTextField.getText().trim().isEmpty()) {
            valid = false;
        }
        
        if (deckComboBox.getValue() == null) {
            valid = false;
        }
        
        saveButton.setDisable(!valid);
    }
    
    private void handleSave() {
        if (isInputValid()) {
            try {
                if (isEditMode) {
                    // Update existing card
                    card.setDeck_id(deckComboBox.getValue().getDeck_id());
                    card.setFront_text(frontTextField.getText().trim());
                    card.setBack_text(backTextField.getText().trim());
                    card.setImage_url(imageUrlField.getText() != null && !imageUrlField.getText().trim().isEmpty() ? imageUrlField.getText().trim() : null);
                    card.setExtra_info(extraInfoField.getText() != null && !extraInfoField.getText().trim().isEmpty() ? extraInfoField.getText().trim() : null);
                    
                    cardsDao.updateCard(card);
                    showInfo("Card updated successfully!");
                } else {
                    // Create new card
                    Cards newCard = new Cards(
                        deckComboBox.getValue().getDeck_id(),
                        frontTextField.getText().trim(),
                        backTextField.getText().trim(),
                        imageUrlField.getText() != null && !imageUrlField.getText().trim().isEmpty() ? imageUrlField.getText().trim() : null,
                        extraInfoField.getText() != null && !extraInfoField.getText().trim().isEmpty() ? extraInfoField.getText().trim() : null,
                        false // Not deleted
                    );
                    
                    cardsDao.persist(newCard);
                    showInfo("Card created successfully!");
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
        
        if (frontTextField.getText() == null || frontTextField.getText().trim().isEmpty()) {
            errorMessage += "Front text (question) is required!\n";
        } else if (frontTextField.getText().trim().length() > 1000) {
            errorMessage += "Front text must be less than 1000 characters!\n";
        }
        
        if (backTextField.getText() == null || backTextField.getText().trim().isEmpty()) {
            errorMessage += "Back text (answer) is required!\n";
        } else if (backTextField.getText().trim().length() > 1000) {
            errorMessage += "Back text must be less than 1000 characters!\n";
        }
        
        if (deckComboBox.getValue() == null) {
            errorMessage += "Please select a deck!\n";
        }
        
        if (imageUrlField.getText() != null && imageUrlField.getText().length() > 500) {
            errorMessage += "Image URL must be less than 500 characters!\n";
        }
        
        if (extraInfoField.getText() != null && extraInfoField.getText().length() > 1000) {
            errorMessage += "Extra info must be less than 1000 characters!\n";
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

    private void styleComboBox() {
        // Apply additional styling to ComboBox to ensure text visibility
        deckComboBox.setStyle(
            "-fx-background-color: #333333;" +
            "-fx-border-color: transparent;" +
            "-fx-border-width: 0;" +
            "-fx-background-radius: 0;" +
            "-fx-border-radius: 0;"
        );
        
        // Style the ComboBox when it's shown
        deckComboBox.setOnShown(e -> {
            deckComboBox.lookupAll(".list-cell").forEach(node -> {
                node.setStyle("-fx-background-color: #333333; -fx-text-fill: #ffffff;");
            });
        });
        
        // Apply cell factory for better text visibility
        deckComboBox.setCellFactory(listView -> {
            return new javafx.scene.control.ListCell<Decks>() {
                @Override
                protected void updateItem(Decks item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getDeck_name());
                    }
                    // Style the cell
                    setStyle("-fx-background-color: #333333; -fx-text-fill: #ffffff;");
                }
            };
        });
        
        // Style the button cell (the selected item display)
        deckComboBox.setButtonCell(new javafx.scene.control.ListCell<Decks>() {
            @Override
            protected void updateItem(Decks item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Select a deck...");
                    setStyle("-fx-text-fill: #757575;"); // Placeholder color
                } else {
                    setText(item.getDeck_name());
                    setStyle("-fx-text-fill: #ffffff;"); // Selected text color
                }
                // Always set background
                setStyle(getStyle() + "-fx-background-color: #333333;");
            }
        });
    }
}