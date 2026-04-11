package com.example.taskremainder.model;

public class UserDTO {

    private Integer id;
    private String email;

    public UserDTO(Integer id, String email) {
        this.id = id;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}