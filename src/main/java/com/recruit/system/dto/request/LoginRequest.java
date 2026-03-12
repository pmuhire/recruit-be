package com.recruit.system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 40, message = "Password must be between 8 and 40 characters")
    private String password;

    // Default constructor
    public LoginRequest() {}

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        // Normalize input to prevent issues like "John" vs "john"
        if (username != null) {
            this.username = username.trim().toLowerCase();
        } else {
            this.username = null;
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}