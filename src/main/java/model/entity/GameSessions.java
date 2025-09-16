package model.entity;

import java.sql.Timestamp;

public class GameSessions {
    private int session_id;
    private int user_id;
    private int deck_id;
    private Timestamp start_time;
    private Timestamp end_time;

    public GameSessions(int user_id, int deck_id, Timestamp start_time, Timestamp end_time) {
        this.user_id = user_id;
        this.deck_id = deck_id;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    // Getters and Setters
    public int getSession_id() {
        return session_id;
    }

    public void setSession_id(int session_id) {
        this.session_id = session_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getDeck_id() {
        return deck_id;
    }

    public void setDeck_id(int deck_id) {
        this.deck_id = deck_id;
    }

    public Timestamp getStart_time() {
        return start_time;
    }

    public void setStart_time(Timestamp start_time) {
        this.start_time = start_time;
    }

    public Timestamp getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Timestamp end_time) {
        this.end_time = end_time;
    }

    @Override
    public String toString() {
        return "GameSessions{" +
                "session_id=" + session_id +
                ", user_id=" + user_id +
                ", deck_id=" + deck_id +
                ", start_time=" + start_time +
                ", end_time=" + end_time +
                '}';
    }
}