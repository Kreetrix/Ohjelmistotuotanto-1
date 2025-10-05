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

/**
 * Controller for the Card Dialog window used for creating and editing flash cards.
 *
 */
public class CardDialogController {

    // FXML injected components
    @FXML private ComboBox<Decks> deckComboBox;
    @FXML private TextField frontTextField;
    @FXML private TextField backTextField;
    @FXML private TextField imageUrlField;
    @FXML private TextField extraInfoField;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    @FXML private Label titleLabel;

    // Dialog and business logic fields
    private Stage dialogStage;
    private Cards card;
    private boolean okClicked = false;
    private boolean isEditMode = false;
    private CardsDao cardsDao;
    private DecksDao decksDao;

    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded.
     *
     * <p>Initializes DAOs, sets up event handlers, loads decks, and applies styling.
     */
    @FXML
    public void initialize() {
        cardsDao = new CardsDao();
        decksDao = new DecksDao();
        setupEventHandlers();
        loadDecks();
        styleComboBox();
    }

    /**
     * Sets up event handlers for user interactions.
     * Includes save/cancel buttons and input validation listeners.
     */
    private void setupEventHandlers() {
        saveButton.setOnAction(e -> handleSave());
        cancelButton.setOnAction(e -> handleCancel());

        // Real-time input validation
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

    /**
     * Loads available decks into the combo box based on user permissions.
     *
     */
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

                        return deck.getUser_id() == currentUserId;
                    })
                    .toList();

            deckComboBox.getItems().addAll(availableDecks);

            // Set up string converter for proper deck display
            deckComboBox.setConverter(new javafx.util.StringConverter<Decks>() {
                @Override
                public String toString(Decks deck) {
                    return deck != null ? deck.getDeck_name() : "";
                }

                @Override
                public Decks fromString(String string) {
                    return null; // Not needed for display-only combo box
                }
            });

        } catch (SQLException e) {
            showError("Failed to load decks: " + e.getMessage());
        }
    }

    /**
     * Sets the dialog stage for this controller.
     *
     * @param dialogStage the stage containing this dialog
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Sets the card to be edited. If null, the dialog will be in create mode.
     *
     * @param card the card to edit, or null for create mode
     */
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

    /**
     * Preselects a deck in the combo box (used when creating cards from a specific deck context).
     *
     * @param preselectedDeck the deck to preselect
     */
    public void setPreselectedDeck(Decks preselectedDeck) {
        if (preselectedDeck != null && !isEditMode) {
            deckComboBox.setValue(preselectedDeck);
        }
    }

    /**
     * Returns whether the user clicked OK to save changes.
     *
     * @return true if OK was clicked, false otherwise
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Performs basic input validation and enables/disables the save button accordingly.
     * Checks for required fields and enables save button only when all required fields are filled.
     */
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

    /**
     * Handles the save button action. Saves the card to the database
     * and closes the dialog if successful.
     */
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
                            false
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

    /**
     * Handles the cancel button action. Closes the dialog without saving.
     */
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Performs comprehensive input validation including length checks.
     *
     * @return true if all input is valid, false otherwise
     */
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

    /**
     * Displays an error alert dialog with the specified message.
     *
     * @param message the error message to display
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(dialogStage);
        alert.showAndWait();
    }

    /**
     * Displays an information alert dialog with the specified message.
     *
     * @param message the information message to display
     */
    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(dialogStage);
        alert.showAndWait();
    }

    /**
     * Applies custom dark theme styling to the deck combo box.
     * Ensures proper text visibility and consistent appearance with the application theme.
     */
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