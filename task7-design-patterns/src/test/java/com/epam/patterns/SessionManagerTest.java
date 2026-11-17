package com.epam.patterns;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SessionManagerTest {

    private SessionManager sessionManager;

    @BeforeEach
    public void setUp() {
        ServerConfig.resetInstance();
        AccessChecker.resetInstance();
        sessionManager = new SessionManager();
    }

    @Test
    public void testAdminAccessAdminPath() {
        User admin = new User("adminUser", "admin");
        Session session = sessionManager.createSession(admin, "/admin/dashboard");
        assertNotNull(session);
        assertEquals(admin, session.getUser());
        assertNotNull(session.getSessionId());
    }

    @Test
    public void testUserAccessUserPath() {
        User user = new User("regularUser", "user");
        Session session = sessionManager.createSession(user, "/user/profile");
        assertNotNull(session);
        assertEquals(user, session.getUser());
        assertNotNull(session.getSessionId());
    }

    @Test
    public void testGuestAccessPublicPath() {
        User guest = new User("guestUser", "guest");
        Session session = sessionManager.createSession(guest, "/public/home");
        assertNotNull(session);
        assertEquals(guest, session.getUser());
        assertNotNull(session.getSessionId());
    }

    @Test
    public void testUserCannotAccessAdminPath() {
        User user = new User("regularUser", "user");
        InsufficientRightsException exception = assertThrows(
                InsufficientRightsException.class,
                () -> sessionManager.createSession(user, "/admin/settings")
        );
        assertEquals(user, exception.getUser());
        assertEquals("/admin/settings", exception.getPath());
    }

    @Test
    public void testGuestCannotAccessUserPath() {
        User guest = new User("guestUser", "guest");
        InsufficientRightsException exception = assertThrows(
                InsufficientRightsException.class,
                () -> sessionManager.createSession(guest, "/user/settings")
        );
        assertEquals(guest, exception.getUser());
        assertEquals("/user/settings", exception.getPath());
    }
}
