package com.epam.booking;

import com.epam.booking.dao.Storage;
import com.epam.booking.dao.UserDao;
import com.epam.booking.model.User;
import com.epam.booking.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    private Storage storage;
    private UserDao userDao;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        storage = new Storage();
        userDao = new UserDao();
        userDao.setStorage(storage);
        userService = new UserService();
        userService.setUserDao(userDao);
    }

    @Test
    public void testCreateUser() {
        User user = new User(1L, "Alice Smith", "alice@example.com");
        User created = userService.createUser(user);

        assertNotNull(created);
        assertEquals(1L, created.getId());
        assertEquals("Alice Smith", created.getName());
        assertEquals("alice@example.com", created.getEmail());
    }

    @Test
    public void testGetUserById() {
        User user = new User(10L, "Bob Jones", "bob@example.com");
        userService.createUser(user);

        User found = userService.getUserById(10L);
        assertNotNull(found);
        assertEquals("Bob Jones", found.getName());
        assertEquals("bob@example.com", found.getEmail());
    }

    @Test
    public void testGetUserByEmail() {
        User user = new User(20L, "Charlie Green", "charlie@example.com");
        userService.createUser(user);

        User found = userService.getUserByEmail("charlie@example.com");
        assertNotNull(found);
        assertEquals(20L, found.getId());
        assertEquals("Charlie Green", found.getName());
    }

    @Test
    public void testGetUsersByNameWithPagination() {
        userService.createUser(new User(1L, "David Copperfield", "david1@example.com"));
        userService.createUser(new User(2L, "David Bowie", "david2@example.com"));
        userService.createUser(new User(3L, "David Beckham", "david3@example.com"));
        userService.createUser(new User(4L, "John David", "david4@example.com"));
        userService.createUser(new User(5L, "Sarah Connor", "sarah@example.com"));

        // Page size 2, page 1 (1-based)
        List<User> page1 = userService.getUsersByName("David", 2, 1);
        assertEquals(2, page1.size());

        // Page size 2, page 2 (1-based)
        List<User> page2 = userService.getUsersByName("David", 2, 2);
        assertEquals(2, page2.size());

        // Page size 2, page 3 (should be empty since total 4)
        List<User> page3 = userService.getUsersByName("David", 2, 3);
        assertEquals(0, page3.size());
    }

    @Test
    public void testUpdateUser() {
        User user = new User(30L, "Eve Adams", "eve@example.com");
        userService.createUser(user);

        User updatedUser = new User(30L, "Eve Updated", "eve_new@example.com");
        User result = userService.updateUser(updatedUser);

        assertNotNull(result);
        assertEquals("Eve Updated", result.getName());
        assertEquals("eve_new@example.com", result.getEmail());

        User fromStorage = userService.getUserById(30L);
        assertEquals("Eve Updated", fromStorage.getName());
    }

    @Test
    public void testDeleteUser() {
        User user = new User(40L, "Frank Miller", "frank@example.com");
        userService.createUser(user);

        boolean deleted = userService.deleteUser(40L);
        assertTrue(deleted);

        User notFound = userService.getUserById(40L);
        assertNull(notFound);
    }
}
