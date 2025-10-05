package model.entity;

import java.sql.Timestamp;

/**
 * Represents the assignment of decks to students.
 * Tracks which decks are assigned to which students and when.
 */
public class DeckAssignments {
    private int assignment_id;
    private int deck_id;
    private int student_id;
    private Timestamp assigned_at;

    /**
     * Constructs a new DeckAssignments entity.
     *
     * @param deck_id the ID of the deck being assigned
     * @param student_id the ID of the student receiving the assignment
     * @param assigned_at the timestamp when the assignment was created
     */
    public DeckAssignments(int deck_id, int student_id, Timestamp assigned_at) {
        this.deck_id = deck_id;
        this.student_id = student_id;
        this.assigned_at = assigned_at;
    }

    // Getters and Setters

    /**
     * @return the assignment ID
     */
    public int getAssignment_id() {
        return assignment_id;
    }

    /**
     * @param assignment_id the assignment ID to set
     */
    public void setAssignment_id(int assignment_id) {
        this.assignment_id = assignment_id;
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
     * @return the student ID
     */
    public int getStudent_id() {
        return student_id;
    }

    /**
     * @param student_id the student ID to set
     */
    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    /**
     * @return the assignment timestamp
     */
    public Timestamp getAssigned_at() {
        return assigned_at;
    }

    /**
     * @param assigned_at the assignment timestamp to set
     */
    public void setAssigned_at(Timestamp assigned_at) {
        this.assigned_at = assigned_at;
    }

    /**
     * @return string representation of the DeckAssignments entity
     */
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