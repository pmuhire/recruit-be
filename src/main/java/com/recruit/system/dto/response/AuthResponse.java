package com.recruit.system.dto.response;

public class AuthResponse {

    private boolean success;
    private String message;
    private String token;
    private String username;
    private String role;
    private Long userId;  // Add userId field

    // Default constructor
    public AuthResponse() {}

    // Constructor to initialize all fields
    public AuthResponse(boolean success, String message, String token, String username, String role, Long userId) {
        this.success = success;
        this.message = message;
        this.token = token;
        this.username = username;
        this.role = role;
        this.userId = userId;  // Set the userId
    }

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Long getUserId() { return userId; }  // Getter for userId
    public void setUserId(Long userId) { this.userId = userId; }  // Setter for userId
}