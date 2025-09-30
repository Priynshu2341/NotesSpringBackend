package com.example.main.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "Notes")

public class Notes {
    @Id
    private String id;
    private String title;
    private String description;
    private LocalDateTime date;
    @DBRef
    private User user;
}
