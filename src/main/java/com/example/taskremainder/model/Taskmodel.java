package com.example.taskremainder.model;

import java.time.LocalDateTime;

public class Taskmodel {

    private int id;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private String status;
    private int userId;
    private String userEmail;

    public Taskmodel() {
    }

    public Taskmodel(int id, String title, String description, LocalDateTime dueDate, String status, int userId,String userEmail) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
        this.userId = userId;
        this.userEmail = userEmail;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
}