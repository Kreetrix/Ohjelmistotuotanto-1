package model.entity;

import java.sql.Timestamp;

public class CardHistory {
    private int history_id;
    private int card_id;
    private int deck_version;
    private String front_text;
    private String back_text;
    private String image_url;
    private String extra_info;
    private Timestamp modified_at;

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
    public int getHistory_id() {
        return history_id;
    }

    public void setHistory_id(int history_id) {
        this.history_id = history_id;
    }

    public int getCard_id() {
        return card_id;
    }

    public void setCard_id(int card_id) {
        this.card_id = card_id;
    }

    public int getDeck_version() {
        return deck_version;
    }

    public void setDeck_version(int deck_version) {
        this.deck_version = deck_version;
    }

    public String getFront_text() {
        return front_text;
    }

    public void setFront_text(String front_text) {
        this.front_text = front_text;
    }

    public String getBack_text() {
        return back_text;
    }

    public void setBack_text(String back_text) {
        this.back_text = back_text;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getExtra_info() {
        return extra_info;
    }

    public void setExtra_info(String extra_info) {
        this.extra_info = extra_info;
    }

    public Timestamp getModified_at() {
        return modified_at;
    }

    public void setModified_at(Timestamp modified_at) {
        this.modified_at = modified_at;
    }

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