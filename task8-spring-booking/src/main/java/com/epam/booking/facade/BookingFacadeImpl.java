package com.epam.booking.facade;

import com.epam.booking.model.Event;
import com.epam.booking.model.Ticket;
import com.epam.booking.model.User;
import com.epam.booking.service.EventService;
import com.epam.booking.service.TicketService;
import com.epam.booking.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

public class BookingFacadeImpl implements BookingFacade {
    private static final Logger logger = LoggerFactory.getLogger(BookingFacadeImpl.class);

    private final UserService userService;
    private final EventService eventService;
    private final TicketService ticketService;

    // constructor injection
    public BookingFacadeImpl(UserService userService, EventService eventService, TicketService ticketService) {
        this.userService = userService;
        this.eventService = eventService;
        this.ticketService = ticketService;
    }

    // User methods
    @Override
    public User getUserById(long userId) {
        logger.info("getUserById called, id={}", userId);
        return userService.getUserById(userId);
    }

    @Override
    public User getUserByEmail(String email) {
        logger.info("looking up user by email: {}", email);
        return userService.getUserByEmail(email);
    }

    @Override
    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        logger.info("getUsersByName: {}", name);
        return userService.getUsersByName(name, pageSize, pageNum);
    }

    @Override
    public User createUser(User user) {
        logger.info("creating user: {}", user.getName());
        return userService.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        logger.info("updating user: {}", user);
        return userService.updateUser(user);
    }

    @Override
    public boolean deleteUser(long userId) {
        logger.info("deleteUser: {}", userId);
        return userService.deleteUser(userId);
    }

    // Event methods
    @Override
    public Event getEventById(long eventId) {
        logger.info("getEventById: {}", eventId);
        return eventService.getEventById(eventId);
    }

    @Override
    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        return eventService.getEventsByTitle(title, pageSize, pageNum);
    }

    @Override
    public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {
        logger.debug("getEventsForDay day={}", day);
        return eventService.getEventsForDay(day, pageSize, pageNum);
    }

    @Override
    public Event createEvent(Event event) {
        logger.info("creating event: {}", event.getTitle());
        return eventService.createEvent(event);
    }

    @Override
    public Event updateEvent(Event event) {
        return eventService.updateEvent(event);
    }

    @Override
    public boolean deleteEvent(long eventId) {
        logger.info("deleteEvent: {}", eventId);
        return eventService.deleteEvent(eventId);
    }

    // Ticket methods
    @Override
    public Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category) {
        logger.info("booking ticket for user {} event {} place {}", userId, eventId, place);
        return ticketService.bookTicket(userId, eventId, place, category);
    }

    @Override
    public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
        logger.info("getBookedTickets for user {}", user != null ? user.getId() : "null");
        return ticketService.getBookedTickets(user, pageSize, pageNum);
    }

    @Override
    public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
        logger.info("getBookedTickets for event {}", event != null ? event.getId() : "null");
        return ticketService.getBookedTickets(event, pageSize, pageNum);
    }

    @Override
    public boolean cancelTicket(long ticketId) {
        logger.info("canceling ticket {}", ticketId);
        return ticketService.cancelTicket(ticketId);
    }
}
