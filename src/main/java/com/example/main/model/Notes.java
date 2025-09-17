package com.example.main.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity(name = "Notes")
public class Notes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private User user;
}
