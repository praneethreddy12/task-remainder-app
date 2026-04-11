package com.example.taskremainder.entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    private String description;

    private LocalDateTime dueDate;
    @Column(nullable = false)
    private String status;

    private String priority;

    private LocalDateTime createdDate;

    //constructor
    public  Task() {
        this.createdDate = LocalDateTime.now();
        this.status="PENDING";
    }
    //CONSTANTS
    public static final String STATUS_PENDING = "PENDING";

    public static final String STATUS_COMPLETED = "COMPLETED";
 public  static final String PRIORITY_LOW = "LOW";
    public  static final String PRIORITY_MEDIUM = "MEDIUM";
    public  static final String PRIORITY_HIGH = "HIGH";
}
