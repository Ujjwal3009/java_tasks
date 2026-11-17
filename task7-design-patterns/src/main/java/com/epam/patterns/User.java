package com.epam.patterns;

public class User {
    private String username;
    private String accessLevel;
    
    public User(String username, String accessLevel) {
        this.username = username;
        this.accessLevel = accessLevel;
    }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getAccessLevel() { return accessLevel; }
    public void setAccessLevel(String accessLevel) { this.accessLevel = accessLevel; }
}
