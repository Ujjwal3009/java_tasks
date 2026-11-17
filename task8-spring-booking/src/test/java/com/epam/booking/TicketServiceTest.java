package com.epam.booking;

import com.epam.booking.dao.Storage;
import com.epam.booking.dao.TicketDao;
import com.epam.booking.model.Event;
import com.epam.booking.model.Ticket;
import com.epam.booking.model.User;
import com.epam.booking.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TicketServiceTest {

    private Storage storage;
    private TicketDao ticketDao;
    private TicketService ticketService;

    @BeforeEach
    public void setUp() {
        storage = new Storage();
        ticketDao = new TicketDao();
        ticketDao.setStorage(storage);
        ticketService = new TicketService();
        ticketService.setTicketDao(ticketDao);
    }

    @Test
    public void testBookTicket() {
        Ticket ticket = ticketService.bookTicket(1L, 100L, 15, Ticket.Category.PREMIUM);

        assertNotNull(ticket);
        assertTrue(ticket.getId() > 0);
        assertEquals(1L, ticket.getUserId());
        assertEquals(100L, ticket.getEventId());
        assertEquals(15, ticket.getPlace());
        assertEquals(Ticket.Category.PREMIUM, ticket.getCategory());
    }

    @Test
    public void testBookTicketDuplicatePlaceThrowsException() {
        ticketService.bookTicket(1L, 100L, 20, Ticket.Category.STANDARD);

        // Attempting to book the exact same place, event and category should fail
        assertThrows(IllegalStateException.class, () -> {
            ticketService.bookTicket(2L, 100L, 20, Ticket.Category.STANDARD);
        });
    }

    @Test
    public void testGetBookedTicketsByUserWithPagination() {
        User user = new User(5L, "Test User", "test@example.com");

        ticketService.bookTicket(user.getId(), 101L, 1, Ticket.Category.STANDARD);
        ticketService.bookTicket(user.getId(), 102L, 2, Ticket.Category.STANDARD);
        ticketService.bookTicket(user.getId(), 103L, 3, Ticket.Category.STANDARD);
        ticketService.bookTicket(999L, 104L, 4, Ticket.Category.STANDARD); // different user

        List<Ticket> page1 = ticketService.getBookedTickets(user, 2, 1);
        assertEquals(2, page1.size());

        List<Ticket> page2 = ticketService.getBookedTickets(user, 2, 2);
        assertEquals(1, page2.size());
    }

    @Test
    public void testGetBookedTicketsByEventWithPagination() {
        Event event = new Event(200L, "Grand Opera", new Date());

        ticketService.bookTicket(1L, event.getId(), 10, Ticket.Category.STANDARD);
        ticketService.bookTicket(2L, event.getId(), 11, Ticket.Category.PREMIUM);
        ticketService.bookTicket(3L, event.getId(), 12, Ticket.Category.BAR);
        ticketService.bookTicket(4L, 999L, 10, Ticket.Category.STANDARD); // different event

        List<Ticket> eventTickets = ticketService.getBookedTickets(event, 2, 1);
        assertEquals(2, eventTickets.size());

        List<Ticket> eventTicketsPage2 = ticketService.getBookedTickets(event, 2, 2);
        assertEquals(1, eventTicketsPage2.size());
    }

    @Test
    public void testCancelTicket() {
        Ticket booked = ticketService.bookTicket(10L, 300L, 5, Ticket.Category.BAR);
        assertNotNull(booked);

        boolean canceled = ticketService.cancelTicket(booked.getId());
        assertTrue(canceled);

        Ticket searchResult = ticketService.getTicketById(booked.getId());
        assertNull(searchResult);

        // Can now re-book the same place since previous was canceled
        Ticket rebooked = ticketService.bookTicket(11L, 300L, 5, Ticket.Category.BAR);
        assertNotNull(rebooked);
    }
}
