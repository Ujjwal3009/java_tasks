package com.epam.booking;

import com.epam.booking.facade.BookingFacade;
import com.epam.booking.model.Event;
import com.epam.booking.model.Ticket;
import com.epam.booking.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "classpath:application-context.xml")
public class BookingIntegrationTest {

    @Autowired
    private BookingFacade bookingFacade;

    @Test
    public void testInitialDataLoadedFromStorage() {
        assertNotNull(bookingFacade);

        // check users from initial-data.json
        User user1 = bookingFacade.getUserById(1L);
        assertNotNull(user1);
        assertEquals("John Doe", user1.getName());
        assertEquals("john@example.com", user1.getEmail());

        User user2 = bookingFacade.getUserById(2L);
        assertNotNull(user2);
        assertEquals("Jane Smith", user2.getName());

        // check events
        Event event1 = bookingFacade.getEventById(1L);
        assertNotNull(event1);
        assertEquals("Java Conference", event1.getTitle());

        Event event2 = bookingFacade.getEventById(2L);
        assertNotNull(event2);
        assertEquals("Spring Meetup", event2.getTitle());
    }

    @Test
    public void testFullBookingWorkflow() {
        System.out.println("Starting integration test: create user -> create event -> book ticket -> verify -> cancel ticket");

        // 1. Create User
        User user = new User(100L, "Alexander Smith", "alexander@example.com");
        User createdUser = bookingFacade.createUser(user);
        assertNotNull(createdUser);
        assertEquals("Alexander Smith", createdUser.getName());

        // 2. Create Event
        Event event = new Event(200L, "Global Spring Summit", new Date());
        Event createdEvent = bookingFacade.createEvent(event);
        assertNotNull(createdEvent);
        assertEquals("Global Spring Summit", createdEvent.getTitle());

        // 3. Book Ticket
        int placeNumber = 42;
        Ticket ticket = bookingFacade.bookTicket(createdUser.getId(), createdEvent.getId(), placeNumber, Ticket.Category.PREMIUM);
        assertNotNull(ticket);
        assertTrue(ticket.getId() > 0);
        assertEquals(createdUser.getId(), ticket.getUserId());
        assertEquals(createdEvent.getId(), ticket.getEventId());
        assertEquals(placeNumber, ticket.getPlace());
        assertEquals(Ticket.Category.PREMIUM, ticket.getCategory());

        // 4. Verify booked tickets for User
        List<Ticket> userTickets = bookingFacade.getBookedTickets(createdUser, 10, 1);
        assertNotNull(userTickets);
        assertEquals(1, userTickets.size());
        assertEquals(ticket.getId(), userTickets.get(0).getId());

        // Verify booked tickets for Event
        List<Ticket> eventTickets = bookingFacade.getBookedTickets(createdEvent, 10, 1);
        assertNotNull(eventTickets);
        assertEquals(1, eventTickets.size());
        assertEquals(ticket.getId(), eventTickets.get(0).getId());

        // 5. Cancel Ticket
        boolean canceled = bookingFacade.cancelTicket(ticket.getId());
        assertTrue(canceled);

        // 6. check that tickets are gone now
        List<Ticket> remainingUserTickets = bookingFacade.getBookedTickets(createdUser, 10, 1);
        assertEquals(0, remainingUserTickets.size());

        List<Ticket> remainingEventTickets = bookingFacade.getBookedTickets(createdEvent, 10, 1);
        assertEquals(0, remainingEventTickets.size());

        System.out.println("Integration test passed successfully!");
    }
}
