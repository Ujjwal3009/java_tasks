package com.epam.booking;

import com.epam.booking.dao.EventDao;
import com.epam.booking.dao.Storage;
import com.epam.booking.model.Event;
import com.epam.booking.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EventServiceTest {

    private Storage storage;
    private EventDao eventDao;
    private EventService eventService;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @BeforeEach
    public void setUp() {
        storage = new Storage();
        eventDao = new EventDao();
        eventDao.setStorage(storage);
        eventService = new EventService();
        eventService.setEventDao(eventDao);
    }

    @Test
    public void testCreateEvent() throws Exception {
        Date eventDate = dateFormat.parse("2024-08-10");
        Event event = new Event(1L, "Tech Summit", eventDate);
        Event created = eventService.createEvent(event);

        assertNotNull(created);
        assertEquals(1L, created.getId());
        assertEquals("Tech Summit", created.getTitle());
        assertEquals(eventDate, created.getDate());
    }

    @Test
    public void testGetEventById() throws Exception {
        Date eventDate = dateFormat.parse("2024-09-15");
        Event event = new Event(2L, "AI Conference", eventDate);
        eventService.createEvent(event);

        Event found = eventService.getEventById(2L);
        assertNotNull(found);
        assertEquals("AI Conference", found.getTitle());
    }

    @Test
    public void testGetEventsByTitleWithPagination() throws Exception {
        Date date = dateFormat.parse("2024-10-01");
        eventService.createEvent(new Event(1L, "Java Bootcamp Part 1", date));
        eventService.createEvent(new Event(2L, "Java Bootcamp Part 2", date));
        eventService.createEvent(new Event(3L, "Java Bootcamp Part 3", date));
        eventService.createEvent(new Event(4L, "Python Workshop", date));

        List<Event> page1 = eventService.getEventsByTitle("Java", 2, 1);
        assertEquals(2, page1.size());

        List<Event> page2 = eventService.getEventsByTitle("Java", 2, 2);
        assertEquals(1, page2.size());
    }

    @Test
    public void testGetEventsForDayWithPagination() throws Exception {
        Date day1 = dateFormat.parse("2024-11-20");
        Date day2 = dateFormat.parse("2024-11-21");

        eventService.createEvent(new Event(10L, "Morning Keynote", day1));
        eventService.createEvent(new Event(11L, "Afternoon Workshop", day1));
        eventService.createEvent(new Event(12L, "Evening Gala", day1));
        eventService.createEvent(new Event(13L, "Next Day Session", day2));

        List<Event> day1EventsPage1 = eventService.getEventsForDay(day1, 2, 1);
        assertEquals(2, day1EventsPage1.size());

        List<Event> day1EventsPage2 = eventService.getEventsForDay(day1, 2, 2);
        assertEquals(1, day1EventsPage2.size());

        List<Event> day2Events = eventService.getEventsForDay(day2, 5, 1);
        assertEquals(1, day2Events.size());
        assertEquals("Next Day Session", day2Events.get(0).getTitle());
    }

    @Test
    public void testUpdateEvent() throws Exception {
        Date initialDate = dateFormat.parse("2024-12-01");
        Event event = new Event(20L, "Old Title", initialDate);
        eventService.createEvent(event);

        Date newDate = dateFormat.parse("2024-12-05");
        Event updated = new Event(20L, "New Title", newDate);
        Event result = eventService.updateEvent(updated);

        assertNotNull(result);
        assertEquals("New Title", result.getTitle());
        assertEquals(newDate, result.getDate());
    }

    @Test
    public void testDeleteEvent() throws Exception {
        Date date = dateFormat.parse("2024-12-10");
        Event event = new Event(30L, "Event to Delete", date);
        eventService.createEvent(event);

        boolean deleted = eventService.deleteEvent(30L);
        assertTrue(deleted);

        Event found = eventService.getEventById(30L);
        assertNull(found);
    }
}
