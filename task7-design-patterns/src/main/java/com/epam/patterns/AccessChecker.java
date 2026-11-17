package com.epam.patterns;

import java.util.Arrays;
import java.util.List;

public class AccessChecker {
    private static volatile AccessChecker instance;
    private ServerConfig config;
    
    // access levels from low to high
    private List<String> levels = Arrays.asList("guest", "user", "admin");
    
    private AccessChecker() {
        config = ServerConfig.getInstance();
    }
    
    public static AccessChecker getInstance() {
        if (instance == null) {
            synchronized (AccessChecker.class) {
                if (instance == null) {
                    instance = new AccessChecker();
                }
            }
        }
        return instance;
    }
    
    public boolean mayAccess(User user, String path) {
        String userLevel = config.getAccessLevel(user);
        String requiredLevel = config.getRequiredLevel(path);
        
        int userLevelIndex = levels.indexOf(userLevel);
        int requiredLevelIndex = levels.indexOf(requiredLevel);
        
        if (userLevelIndex == -1) {
            return false;
        }
        if (requiredLevelIndex == -1) {
            return true; // unknown requirement, allow
        }
        
        return userLevelIndex >= requiredLevelIndex;
    }
    
    // for testing
    public static void resetInstance() {
        instance = null;
    }
}
