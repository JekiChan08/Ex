package com.example.ex_testing.Model;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "projects")
@Builder
public class Project {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "user_id")
    private Long userId;

    public Project() {

    }

    public Project(Long id, String name, String description, LocalDateTime createdAt, LocalDateTime updatedAt, Long userId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.userId = userId;
    }
}
