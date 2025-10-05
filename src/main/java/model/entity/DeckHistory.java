package model.entity;

import java.sql.Timestamp;

/**
 * Represents the history of changes made to flashcard decks.
 * Tracks previous versions of decks for audit and rollback purposes.
 */
public class DeckHistory {
    private int history_id;
    private int deck_id;
    private int version;
    private String deck_name;
    private String description;
    private Timestamp modified_at;

    /**
     * Constructs a new DeckHistory entity.
     *
     * @param deck_id the ID of the deck being tracked
     * @param version the version number of this history record
     * @param deck_name the deck name at this history point
     * @param description the deck description at this history point
     * @param modified_at the timestamp when this history record was created
     */
    public DeckHistory(int deck_id, int version, String deck_name, String description, Timestamp modified_at) {
        this.deck_id = deck_id;
        this.version = version;
        this.deck_name = deck_name;
        this.description = description;
        this.modified_at = modified_at;
    }

    // Getters and Setters

    /**
     * @return the history record ID
     */
    public int getHistory_id() {
        return history_id;
    }

    /**
     * @param history_id the history record ID to set
     */
    public void setHistory_id(int history_id) {
        this.history_id = history_id;
    }

    /**
     * @return the deck ID
     */
    public int getDeck_id() {
        return deck_id;
    }

    /**
     * @param deck_id the deck ID to set
     */
    public void setDeck_id(int deck_id) {
        this.deck_id = deck_id;
    }

    /**
     * @return the version number
     */
    public int getVersion() {
        return version;
    }

    /**
     * @param version the version number to set
     */
    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * @return the deck name
     */
    public String getDeck_name() {
        return deck_name;
    }

    /**
     * @param deck_name the deck name to set
     */
    public void setDeck_name(String deck_name) {
        this.deck_name = deck_name;
    }

    /**
     * @return the deck description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the deck description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the modification timestamp
     */
    public Timestamp getModified_at() {
        return modified_at;
    }

    /**
     * @param modified_at the modification timestamp to set
     */
    public void setModified_at(Timestamp modified_at) {
        this.modified_at = modified_at;
    }

    /**
     * @return string representation of the DeckHistory entity
     */
    @Override
    public String toString() {
        return "DeckHistory{" +
                "history_id=" + history_id +
                ", deck_id=" + deck_id +
                ", version=" + version +
                ", deck_name='" + deck_name + '\'' +
                ", description='" + description + '\'' +
                ", modified_at=" + modified_at +
                '}';
    }
}