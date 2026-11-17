package com.epam.booking.service;

import com.epam.booking.dao.UserDao;
import com.epam.booking.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private UserDao userDao;

    public UserService() {
    }

    // setter injection for UserDao
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public UserDao getUserDao() {
        return this.userDao;
    }

    public User getUserById(long userId) {
        logger.info("UserService: Fetching user by id: {}", userId);
        System.out.println("UserService: getUserById called for id: " + userId);
        return userDao.findById(userId);
    }

    public User getUserByEmail(String email) {
        logger.info("UserService: Fetching user by email: {}", email);
        System.out.println("UserService: getUserByEmail called for email: " + email);

        if (email == null) {
            return null;
        }

        List<User> allUsers = userDao.findAll();
        for (User u : allUsers) {
            if (u.getEmail() != null && u.getEmail().equalsIgnoreCase(email)) {
                logger.debug("User found with email {}: {}", email, u);
                return u;
            }
        }

        logger.debug("No user found with email: {}", email);
        return null;
    }

    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        logger.info("UserService: Fetching users by name containing '{}', pageSize={}, pageNum={}", name, pageSize, pageNum);

        List<User> allUsers = userDao.findAll();
        List<User> matchedUsers = new ArrayList<>();

        for (User u : allUsers) {
            if (name == null || name.trim().isEmpty()) {
                matchedUsers.add(u);
            } else if (u.getName() != null && u.getName().toLowerCase().contains(name.toLowerCase())) {
                matchedUsers.add(u);
            }
        }

        return paginate(matchedUsers, pageSize, pageNum);
    }

    public User createUser(User user) {
        logger.info("UserService: Creating user: {}", user);
        System.out.println("UserService: createUser called for user: " + (user != null ? user.getName() : "null"));
        if (user == null) {
            return null;
        }
        return userDao.save(user);
    }

    public User updateUser(User user) {
        logger.info("UserService: Updating user: {}", user);
        System.out.println("UserService: updateUser called for user: " + (user != null ? user.getId() : "null"));

        if (user == null) {
            return null;
        }

        User existing = userDao.findById(user.getId());
        if (existing == null) {
            logger.warn("User with id {} not found for update", user.getId());
            return null;
        }

        return userDao.save(user);
    }

    public boolean deleteUser(long userId) {
        logger.info("UserService: Deleting user with id: {}", userId);
        System.out.println("UserService: deleteUser called for id: " + userId);
        return userDao.delete(userId);
    }

    // Simple student helper method for pagination
    private <T> List<T> paginate(List<T> list, int pageSize, int pageNum) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }
        if (pageSize <= 0) {
            pageSize = 10;
        }
        if (pageNum <= 0) {
            pageNum = 1;
        }

        int startIndex = (pageNum - 1) * pageSize;
        if (startIndex >= list.size()) {
            return new ArrayList<>();
        }

        int endIndex = startIndex + pageSize;
        if (endIndex > list.size()) {
            endIndex = list.size();
        }

        return new ArrayList<>(list.subList(startIndex, endIndex));
    }
}
