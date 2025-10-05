package model.entity;

/**
 * Represents a flash card entity with front and back content.
 * Contains the core data for individual flashcards in the system.
 */
public class Cards {
    private int card_id;
    private int deck_id;
    private String front_text;
    private String back_text;
    private String image_url;
    private String extra_info;
    private boolean is_deleted;

    /**
     * Constructs a new Cards entity.
     *
     * @param deck_id the ID of the deck this card belongs to
     * @param front_text the question or front side text of the card
     * @param back_text the answer or back side text of the card
     * @param image_url optional image URL associated with the card
     * @param extra_info optional additional information for the card
     * @param is_deleted whether the card is marked as deleted
     */
    public Cards(int deck_id, String front_text, String back_text, String image_url, String extra_info, boolean is_deleted) {
        this.deck_id = deck_id;
        this.front_text = front_text;
        this.back_text = back_text;
        this.image_url = image_url;
        this.extra_info = extra_info;
        this.is_deleted = is_deleted;
    }

    // Getters and Setters

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
     * @return the deck ID this card belongs to
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
     * @return the front text (question) of the card
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
     * @return the back text (answer) of the card
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
     * @return the image URL associated with the card
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
     * @return the extra information for the card
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
     * @return whether the card is marked as deleted
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
     * @return string representation of the Cards entity
     */
    @Override
    public String toString() {
        return "Cards{" +
                "card_id=" + card_id +
                ", deck_id=" + deck_id +
                ", front_text='" + front_text + '\'' +
                ", back_text='" + back_text + '\'' +
                ", image_url='" + image_url + '\'' +
                ", extra_info='" + extra_info + '\'' +
                ", is_deleted=" + is_deleted +
                '}';
    }
}