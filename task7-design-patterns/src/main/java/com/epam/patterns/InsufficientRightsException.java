package com.epam.patterns;

public class InsufficientRightsException extends RuntimeException {
    private User user;
    private String path;
    
    public InsufficientRightsException(User user, String path) {
        super("User " + user.getUsername() + " does not have access to " + path);
        this.user = user;
        this.path = path;
    }
    
    public User getUser() { return user; }
    public String getPath() { return path; }
}
