package com.epam.booking.dao;

import com.epam.booking.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);
    private Storage storage;

    public UserDao() {
    }

    // autowired setter
    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public Storage getStorage() {
        return this.storage;
    }

    public User save(User user) {
        if (user == null) {
            return null;
        }
        if (user.getId() <= 0) {
            long newId = generateNextId();
            user.setId(newId);
        }
        logger.debug("Saving user into storage: {}", user);
        storage.put("user:" + user.getId(), user);
        return user;
    }

    public User findById(long id) {
        logger.debug("Finding user by id: {}", id);
        return (User) storage.get("user:" + id);
    }

    public boolean delete(long id) {
        logger.debug("Deleting user with id: {}", id);
        return storage.remove("user:" + id) != null;
    }

    public List<User> findAll() {
        logger.debug("Finding all users from storage");
        List<User> userList = new ArrayList<>();
        Map<String, Object> allData = storage.getAll();
        for (Map.Entry<String, Object> entry : allData.entrySet()) {
            if (entry.getKey().startsWith("user:")) {
                if (entry.getValue() instanceof User) {
                    userList.add((User) entry.getValue());
                }
            }
        }
        return userList;
    }

    private synchronized long generateNextId() {
        long maxId = 0;
        List<User> all = findAll();
        for (User u : all) {
            if (u.getId() > maxId) {
                maxId = u.getId();
            }
        }
        return maxId + 1;
    }
}
