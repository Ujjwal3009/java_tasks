package com.epam.patterns;

public class Session {
    private User user;
    private String sessionId;
    
    public Session(User user) {
        this.user = user;
        this.sessionId = "session-" + System.currentTimeMillis();
    }
    
    public User getUser() { return user; }
    public String getSessionId() { return sessionId; }
}
