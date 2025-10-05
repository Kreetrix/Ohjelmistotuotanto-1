package model.entity;
import java.sql.Timestamp;

/**
 * Represents a flashcard deck entity containing collections of cards.
 * Manages deck properties including visibility, ownership, and versioning.
 */
public class Decks {
    private int deck_id;
    private int user_id;
    private String deck_name;
    private String description;
    private int version;
    private String visibility;
    private boolean is_deleted;
    private Timestamp created_at;

    /**
     * Constructs a new Decks entity.
     *
     * @param user_id the ID of the user who owns this deck
     * @param deck_name the name of the deck
     * @param description the description of the deck
     * @param version the version number of the deck
     * @param visibility the visibility setting ("private", "public", or "assigned")
     * @param is_deleted whether the deck is marked as deleted
     * @param created_at the timestamp when the deck was created
     */
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
     * @return the user ID who owns this deck
     */
    public int getUser_id() {
        return user_id;
    }

    /**
     * @param user_id the user ID to set
     */
    public void setUser_id(int user_id) {
        this.user_id = user_id;
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
     * @return the visibility setting
     */
    public String getVisibility() {
        return visibility;
    }

    /**
     * @param visibility the visibility setting to set
     */
    public void setVisibility(String visibility) {
        this.visibility = visibility != null ? visibility : "private";
    }

    /**
     * Convenience method for checking if deck is public.
     * @return true if deck visibility is "public"
     */
    public boolean isVisibility() {
        return "public".equals(visibility);
    }

    /**
     * Convenience method for setting visibility using boolean.
     * @param isPublic true to set visibility to "public", false for "private"
     */
    public void setVisibility(boolean isPublic) {
        this.visibility = isPublic ? "public" : "private";
    }

    /**
     * @return whether the deck is marked as deleted
     */
    public boolean isIs_deleted() {
        return is_deleted;
    }

    /**
     * @param is_deleted the deletion status to set
     */
    public void setIs_deleted(boolean is_deleted) {
        this.is_deleted = is_deleted;
    }

    /**
     * @return the creation timestamp
     */
    public Timestamp getCreated_at() {
        return created_at;
    }

    /**
     * @param created_at the creation timestamp to set
     */
    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    /**
     * @return string representation of the Decks entity
     */
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