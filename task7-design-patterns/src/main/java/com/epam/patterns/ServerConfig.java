package com.epam.patterns;

import java.util.HashMap;
import java.util.Map;

public class ServerConfig {
    private static volatile ServerConfig instance;
    private Map<String, String> accessLevels;
    private Map<String, String> pathRequirements;
    
    private ServerConfig() {
        // load configuration - simulated
        accessLevels = new HashMap<>();
        pathRequirements = new HashMap<>();
        // some default config
        pathRequirements.put("/admin", "admin");
        pathRequirements.put("/user", "user");
        pathRequirements.put("/public", "guest");
    }
    
    public static ServerConfig getInstance() {
        if (instance == null) {
            synchronized (ServerConfig.class) {
                if (instance == null) {
                    instance = new ServerConfig();
                }
            }
        }
        return instance;
    }
    
    public String getAccessLevel(User user) {
        return user.getAccessLevel();
    }
    
    public String getRequiredLevel(String path) {
        // check each path prefix
        for (Map.Entry<String, String> entry : pathRequirements.entrySet()) {
            if (path.startsWith(entry.getKey())) {
                return entry.getValue();
            }
        }
        return "guest"; // default
    }
    
    public void addPathRequirement(String path, String level) {
        pathRequirements.put(path, level);
    }
    
    // for testing - allow reset
    public static void resetInstance() {
        instance = null;
    }
}
