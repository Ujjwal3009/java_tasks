package com.epam.booking.dao;

import com.epam.booking.model.Event;
import com.epam.booking.model.Ticket;
import com.epam.booking.model.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Storage {
    private static final Logger logger = LoggerFactory.getLogger(Storage.class);

    private Map<String, Object> store = new ConcurrentHashMap<>();

    // data file path configured via Spring
    private String dataFilePath;

    public Storage() {
    }

    public void put(String key, Object value) {
        store.put(key, value);
    }

    public Object get(String key) {
        return store.get(key);
    }

    public Object remove(String key) {
        return store.remove(key);
    }

    public Map<String, Object> getAll() {
        return new HashMap<>(store);
    }

    public String getDataFilePath() {
        return dataFilePath;
    }

    public void setDataFilePath(String dataFilePath) {
        this.dataFilePath = dataFilePath;
    }

    public void init() {
        logger.info("Initializing Storage with dataFilePath: {}", dataFilePath);
        System.out.println("Storage: initializing data from " + dataFilePath);

        if (dataFilePath == null || dataFilePath.trim().isEmpty()) {
            logger.warn("dataFilePath is null or empty, skipping initialization.");
            return;
        }

        try {
            InputStream inputStream = null;
            if (dataFilePath.startsWith("classpath:")) {
                String resourcePath = dataFilePath.substring("classpath:".length());
                if (resourcePath.startsWith("/")) {
                    resourcePath = resourcePath.substring(1);
                }
                inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath);
                if (inputStream == null) {
                    inputStream = Storage.class.getClassLoader().getResourceAsStream(resourcePath);
                }
            } else {
                File file = new File(dataFilePath);
                if (file.exists()) {
                    inputStream = new FileInputStream(file);
                } else {
                    inputStream = Storage.class.getClassLoader().getResourceAsStream(dataFilePath);
                }
            }

            if (inputStream == null) {
                logger.warn("Could not find data file at path: {}", dataFilePath);
                System.out.println("Storage warning: Could not find data file at: " + dataFilePath);
                return;
            }

            ObjectMapper mapper = new ObjectMapper();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            mapper.setDateFormat(sdf);

            JsonNode rootNode = mapper.readTree(inputStream);
            if (rootNode != null) {
                // Load users
                if (rootNode.has("users")) {
                    JsonNode usersNode = rootNode.get("users");
                    for (JsonNode uNode : usersNode) {
                        User user = mapper.treeToValue(uNode, User.class);
                        put("user:" + user.getId(), user);
                        logger.info("Loaded user: {}", user);
                        System.out.println("Loaded user into storage: " + user.getName() + " (" + user.getEmail() + ")");
                    }
                }

                // Load events
                if (rootNode.has("events")) {
                    JsonNode eventsNode = rootNode.get("events");
                    for (JsonNode eNode : eventsNode) {
                        Event event = mapper.treeToValue(eNode, Event.class);
                        put("event:" + event.getId(), event);
                        logger.info("Loaded event: {}", event);
                        System.out.println("Loaded event into storage: " + event.getTitle());
                    }
                }

                // Load tickets if any exist in the JSON
                if (rootNode.has("tickets")) {
                    JsonNode ticketsNode = rootNode.get("tickets");
                    for (JsonNode tNode : ticketsNode) {
                        Ticket ticket = mapper.treeToValue(tNode, Ticket.class);
                        put("ticket:" + ticket.getId(), ticket);
                        logger.info("Loaded ticket: {}", ticket);
                        System.out.println("Loaded ticket into storage: id=" + ticket.getId());
                    }
                }
            }

            logger.info("Storage initialization completed. Total items in store: {}", store.size());
            System.out.println("Storage initialized successfully. Total items: " + store.size());
        } catch (Exception e) {
            logger.error("Error occurred while initializing storage from: " + dataFilePath, e);
            System.out.println("Error initializing storage: " + e.getMessage());
        }
    }
}
