package model.entity;

import java.sql.Timestamp;

/**
 * Represents a study session entity tracking user engagement with decks.
 * Records start and end times for study sessions for progress tracking.
 */
public class GameSessions {
    private int session_id;
    private int user_id;
    private int deck_id;
    private Timestamp start_time;
    private Timestamp end_time;

    /**
     * Constructs a new GameSessions entity.
     *
     * @param user_id the ID of the user participating in the session
     * @param deck_id the ID of the deck being studied
     * @param start_time the timestamp when the session started
     * @param end_time the timestamp when the session ended (can be null for ongoing sessions)
     */
    public GameSessions(int user_id, int deck_id, Timestamp start_time, Timestamp end_time) {
        this.user_id = user_id;
        this.deck_id = deck_id;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    // Getters and Setters

    /**
     * @return the session ID
     */
    public int getSession_id() {
        return session_id;
    }

    /**
     * @param session_id the session ID to set
     */
    public void setSession_id(int session_id) {
        this.session_id = session_id;
    }

    /**
     * @return the user ID
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
     * @return the session start time
     */
    public Timestamp getStart_time() {
        return start_time;
    }

    /**
     * @param start_time the session start time to set
     */
    public void setStart_time(Timestamp start_time) {
        this.start_time = start_time;
    }

    /**
     * @return the session end time
     */
    public Timestamp getEnd_time() {
        return end_time;
    }

    /**
     * @param end_time the session end time to set
     */
    public void setEnd_time(Timestamp end_time) {
        this.end_time = end_time;
    }

    /**
     * @return string representation of the GameSessions entity
     */
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