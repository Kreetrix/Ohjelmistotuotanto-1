package model.entity;

import java.sql.Timestamp;

public class DeckAssignments {
    private int assignment_id;
    private int deck_id;
    private int student_id;
    private Timestamp assigned_at;

    public DeckAssignments(int deck_id, int student_id, Timestamp assigned_at) {
        this.deck_id = deck_id;
        this.student_id = student_id;
        this.assigned_at = assigned_at;
    }

    // Getters and Setters
    public int getAssignment_id() {
        return assignment_id;
    }

    public void setAssignment_id(int assignment_id) {
        this.assignment_id = assignment_id;
    }

    public int getDeck_id() {
        return deck_id;
    }

    public void setDeck_id(int deck_id) {
        this.deck_id = deck_id;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public Timestamp getAssigned_at() {
        return assigned_at;
    }

    public void setAssigned_at(Timestamp assigned_at) {
        this.assigned_at = assigned_at;
    }

    @Override
    public String toString() {
        return "DeckAssignments{" +
                "assignment_id=" + assignment_id +
                ", deck_id=" + deck_id +
                ", student_id=" + student_id +
                ", assigned_at=" + assigned_at +
                '}';
    }
}