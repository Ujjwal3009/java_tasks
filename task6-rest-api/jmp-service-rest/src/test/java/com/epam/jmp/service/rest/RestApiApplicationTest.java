package com.epam.jmp.service.rest;

import com.epam.jmp.dto.SubscriptionRequestDto;
import com.epam.jmp.dto.SubscriptionResponseDto;
import com.epam.jmp.dto.UserRequestDto;
import com.epam.jmp.dto.UserResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.EntityModel;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RestApiApplicationTest {

    @Autowired
    private UserController userController;

    @Autowired
    private ServiceController serviceController;

    @Test
    void contextLoads() {
        assertNotNull(userController);
        assertNotNull(serviceController);
    }

    @Test
    void testUserCrudAndHateoas() {
        UserRequestDto userRequest = new UserRequestDto("John", "Doe", "1990-01-01");
        EntityModel<UserResponseDto> created = userController.createUser(userRequest);
        assertNotNull(created);
        assertNotNull(created.getContent());
        Long userId = created.getContent().getId();
        assertNotNull(userId);
        assertTrue(created.hasLink("self"));
        assertTrue(created.hasLink("users"));

        EntityModel<UserResponseDto> fetched = userController.getUser(userId);
        assertEquals("John", fetched.getContent().getName());
        assertEquals("Doe", fetched.getContent().getSurname());
        assertEquals("1990-01-01", fetched.getContent().getBirthday());

        // Update
        UserRequestDto updateRequest = new UserRequestDto(userId, "Johnny", "Doe", "1990-01-02");
        EntityModel<UserResponseDto> updated = userController.updateUser(updateRequest);
        assertEquals("Johnny", updated.getContent().getName());
        assertEquals("1990-01-02", updated.getContent().getBirthday());

        List<EntityModel<UserResponseDto>> allUsers = userController.getAllUser();
        assertFalse(allUsers.isEmpty());

        // Test Subscription
        SubscriptionRequestDto subRequest = new SubscriptionRequestDto(userId);
        EntityModel<SubscriptionResponseDto> createdSub = serviceController.createSubscription(subRequest);
        assertNotNull(createdSub);
        assertNotNull(createdSub.getContent());
        Long subId = createdSub.getContent().getId();
        assertNotNull(subId);
        assertTrue(createdSub.hasLink("self"));
        assertTrue(createdSub.hasLink("subscriptions"));
        assertTrue(createdSub.hasLink("user"));

        EntityModel<SubscriptionResponseDto> fetchedSub = serviceController.getSubscription(subId);
        assertEquals(userId, fetchedSub.getContent().getUserId());

        List<EntityModel<SubscriptionResponseDto>> allSubs = serviceController.getAllSubscription();
        assertFalse(allSubs.isEmpty());

        // Delete
        serviceController.deleteSubscription(subId);
        userController.deleteUser(userId);
    }
}
