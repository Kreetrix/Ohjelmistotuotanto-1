package com.example.memorymaster.entity;
import jakarta.persistence.*;

// TODO
// ADD CODE


@Entity
public class Deck {
    @Id
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
