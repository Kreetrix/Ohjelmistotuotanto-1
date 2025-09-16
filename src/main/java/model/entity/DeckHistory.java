package model.entity;

import java.sql.Timestamp;

public class DeckHistory {
    private int history_id;
    private int deck_id;
    private int version;
    private String deck_name;
    private String description;
    private Timestamp modified_at;

    public DeckHistory(int deck_id, int version, String deck_name, String description, Timestamp modified_at) {
        this.deck_id = deck_id;
        this.version = version;
        this.deck_name = deck_name;
        this.description = description;
        this.modified_at = modified_at;
    }

    // Getters and Setters
    public int getHistory_id() {
        return history_id;
    }

    public void setHistory_id(int history_id) {
        this.history_id = history_id;
    }

    public int getDeck_id() {
        return deck_id;
    }

    public void setDeck_id(int deck_id) {
        this.deck_id = deck_id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getDeck_name() {
        return deck_name;
    }

    public void setDeck_name(String deck_name) {
        this.deck_name = deck_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getModified_at() {
        return modified_at;
    }

    public void setModified_at(Timestamp modified_at) {
        this.modified_at = modified_at;
    }

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