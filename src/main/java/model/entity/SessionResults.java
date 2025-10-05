package model.entity;

/**
 * Represents the results of individual card attempts within study sessions.
 * Tracks user performance on specific cards during study sessions.
 */
public class SessionResults {
    private int result_id;
    private int session_id;
    private int card_id;
    private boolean is_correct;
    private int response_time;

    /**
     * Constructs a new SessionResults entity.
     *
     * @param session_id the ID of the study session
     * @param card_id the ID of the card being attempted
     * @param is_correct whether the user answered correctly
     * @param response_time the time taken to respond (in milliseconds)
     */
    public SessionResults(int session_id, int card_id, boolean is_correct, int response_time) {
        this.session_id = session_id;
        this.card_id = card_id;
        this.is_correct = is_correct;
        this.response_time = response_time;
    }

    // Getters and Setters

    /**
     * @return the result ID
     */
    public int getResult_id() {
        return result_id;
    }

    /**
     * @param result_id the result ID to set
     */
    public void setResult_id(int result_id) {
        this.result_id = result_id;
    }

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
     * @return whether the answer was correct
     */
    public boolean isIs_correct() {
        return is_correct;
    }

    /**
     * @param is_correct the correctness status to set
     */
    public void setIs_correct(boolean is_correct) {
        this.is_correct = is_correct;
    }

    /**
     * @return the response time in milliseconds
     */
    public int getResponse_time() {
        return response_time;
    }

    /**
     * @param response_time the response time to set
     */
    public void setResponse_time(int response_time) {
        this.response_time = response_time;
    }

    /**
     * @return string representation of the SessionResults entity
     */
    @Override
    public String toString() {
        return "SessionResults{" +
                "result_id=" + result_id +
                ", session_id=" + session_id +
                ", card_id=" + card_id +
                ", is_correct=" + is_correct +
                ", response_time=" + response_time +
                '}';
    }
}