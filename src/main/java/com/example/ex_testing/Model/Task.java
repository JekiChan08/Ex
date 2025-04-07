package com.example.ex_testing.Model;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "tasks")
public class Task {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "status")
    private String status;
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "project_id")
    private Long projectId;
    @Column(name = "assigned_user_id")
    private Long assignedUserId;

    public Task() {

    }

    public Task(Long id, String title, String description, String status, LocalDateTime createdAt, LocalDateTime updatedAt, Long assignedUserId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.assignedUserId = assignedUserId;
    }
}
