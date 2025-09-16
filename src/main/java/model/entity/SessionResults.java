package model.entity;

public class SessionResults {
    private int result_id;
    private int session_id;
    private int card_id;
    private boolean is_correct;
    private int response_time;

    public SessionResults(int session_id, int card_id, boolean is_correct, int response_time) {
        this.session_id = session_id;
        this.card_id = card_id;
        this.is_correct = is_correct;
        this.response_time = response_time;
    }

    // Getters and Setters
    public int getResult_id() {
        return result_id;
    }

    public void setResult_id(int result_id) {
        this.result_id = result_id;
    }

    public int getSession_id() {
        return session_id;
    }

    public void setSession_id(int session_id) {
        this.session_id = session_id;
    }

    public int getCard_id() {
        return card_id;
    }

    public void setCard_id(int card_id) {
        this.card_id = card_id;
    }

    public boolean isIs_correct() {
        return is_correct;
    }

    public void setIs_correct(boolean is_correct) {
        this.is_correct = is_correct;
    }

    public int getResponse_time() {
        return response_time;
    }

    public void setResponse_time(int response_time) {
        this.response_time = response_time;
    }

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