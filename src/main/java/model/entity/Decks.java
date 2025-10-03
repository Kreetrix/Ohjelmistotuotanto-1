package model.entity;
//Decks.java
import java.sql.Timestamp;

public class Decks {
    private int deck_id;
    private int user_id;
    private String deck_name;
    private String description;
    private int version;
    private String visibility;
    private boolean is_deleted;
    private Timestamp created_at;

    public Decks(int user_id, String deck_name, String description, int version, String visibility, boolean is_deleted, Timestamp created_at) {
        this.user_id = user_id;
        this.deck_name = deck_name;
        this.description = description;
        this.version = version;
        this.visibility = visibility != null ? visibility : "private"; // Default to private
        this.is_deleted = is_deleted;
        this.created_at = created_at;
    }

    // Getters and Setters
    public int getDeck_id() {
        return deck_id;
    }

    public void setDeck_id(int deck_id) {
        this.deck_id = deck_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility != null ? visibility : "private";
    }

    // Convenience methods for backwards compatibility
    public boolean isVisibility() {
        return "public".equals(visibility);
    }

    public void setVisibility(boolean isPublic) {
        this.visibility = isPublic ? "public" : "private";
    }

    public boolean isIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(boolean is_deleted) {
        this.is_deleted = is_deleted;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "Decks{" +
                "deck_id=" + deck_id +
                ", user_id=" + user_id +
                ", deck_name='" + deck_name + '\'' +
                ", description='" + description + '\'' +
                ", version=" + version +
                ", visibility=" + visibility +
                ", is_deleted=" + is_deleted +
                ", created_at=" + created_at +
                '}';
    }
}