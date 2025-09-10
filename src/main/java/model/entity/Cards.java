package model.entity;

public class Cards {
    private int card_id;
    private int deck_id;
    private String front_text;
    private String back_text;
    private String image_url;
    private String extra_info;
    private boolean is_deleted;

    public Cards( int deck_id, String front_text, String back_text, String image_url, String extra_info, boolean is_deleted) {
        this.deck_id = deck_id;
        this.front_text = front_text;
        this.back_text = back_text;
        this.image_url = image_url;
        this.extra_info = extra_info;
        this.is_deleted = is_deleted;
    }

    // Getters and Setters
    public int getCard_id() {
        return card_id;
    }

    public void setCard_id(int card_id) {
        this.card_id = card_id;
    }

    public int getDeck_id() {
        return deck_id;
    }

    public void setDeck_id(int deck_id) {
        this.deck_id = deck_id;
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

    public boolean isIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(boolean is_deleted) {
        this.is_deleted = is_deleted;
    }

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
