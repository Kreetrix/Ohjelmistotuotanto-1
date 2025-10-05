package model.entity;

import java.sql.Timestamp;

/**
 * Represents the history of changes made to flash cards.
 * Tracks previous versions of cards for audit and rollback purposes.
 */
public class CardHistory {
    private int history_id;
    private int card_id;
    private int deck_version;
    private String front_text;
    private String back_text;
    private String image_url;
    private String extra_info;
    private Timestamp modified_at;

    /**
     * Constructs a new CardHistory entity.
     *
     * @param card_id the ID of the card being tracked
     * @param deck_version the version of the deck when this history was created
     * @param front_text the front text of the card at this history point
     * @param back_text the back text of the card at this history point
     * @param image_url the image URL of the card at this history point
     * @param extra_info the extra information of the card at this history point
     * @param modified_at the timestamp when this history record was created
     */
    public CardHistory(int card_id, int deck_version, String front_text, String back_text, String image_url, String extra_info, Timestamp modified_at) {
        this.card_id = card_id;
        this.deck_version = deck_version;
        this.front_text = front_text;
        this.back_text = back_text;
        this.image_url = image_url;
        this.extra_info = extra_info;
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
     * @return the card ID
     */
    public int getCard_id() {
        return card_id;
    }

    /**
     * @param card_id the card ID to set
     */
    public void setCard_id(int card_id) {
        this.card_id = card_id;
    }

    /**
     * @return the deck version
     */
    public int getDeck_version() {
        return deck_version;
    }

    /**
     * @param deck_version the deck version to set
     */
    public void setDeck_version(int deck_version) {
        this.deck_version = deck_version;
    }

    /**
     * @return the front text of the card
     */
    public String getFront_text() {
        return front_text;
    }

    /**
     * @param front_text the front text to set
     */
    public void setFront_text(String front_text) {
        this.front_text = front_text;
    }

    /**
     * @return the back text of the card
     */
    public String getBack_text() {
        return back_text;
    }

    /**
     * @param back_text the back text to set
     */
    public void setBack_text(String back_text) {
        this.back_text = back_text;
    }

    /**
     * @return the image URL of the card
     */
    public String getImage_url() {
        return image_url;
    }

    /**
     * @param image_url the image URL to set
     */
    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    /**
     * @return the extra information of the card
     */
    public String getExtra_info() {
        return extra_info;
    }

    /**
     * @param extra_info the extra information to set
     */
    public void setExtra_info(String extra_info) {
        this.extra_info = extra_info;
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
     * @return string representation of the CardHistory entity
     */
    @Override
    public String toString() {
        return "CardHistory{" +
                "history_id=" + history_id +
                ", card_id=" + card_id +
                ", deck_version=" + deck_version +
                ", front_text='" + front_text + '\'' +
                ", back_text='" + back_text + '\'' +
                ", image_url='" + image_url + '\'' +
                ", extra_info='" + extra_info + '\'' +
                ", modified_at=" + modified_at +
                '}';
    }
}