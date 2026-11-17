package com.epam.booking.service;

import com.epam.booking.dao.EventDao;
import com.epam.booking.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EventService {
    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    private EventDao eventDao;

    public EventService() {
    }

    // setter injection for EventDao
    public void setEventDao(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    public EventDao getEventDao() {
        return this.eventDao;
    }

    public Event getEventById(long eventId) {
        logger.info("EventService: Fetching event by id: {}", eventId);
        System.out.println("EventService: getEventById called for id: " + eventId);
        return eventDao.findById(eventId);
    }

    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        logger.info("EventService: Fetching events with title containing '{}', pageSize={}, pageNum={}", title, pageSize, pageNum);

        List<Event> allEvents = eventDao.findAll();
        List<Event> matchedEvents = new ArrayList<>();

        for (Event e : allEvents) {
            if (title == null || title.trim().isEmpty()) {
                matchedEvents.add(e);
            } else if (e.getTitle() != null && e.getTitle().toLowerCase().contains(title.toLowerCase())) {
                matchedEvents.add(e);
            }
        }

        return paginate(matchedEvents, pageSize, pageNum);
    }

    public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {
        logger.info("EventService: Fetching events for day: {}, pageSize={}, pageNum={}", day, pageSize, pageNum);

        if (day == null) {
            return new ArrayList<>();
        }

        Calendar targetCal = Calendar.getInstance();
        targetCal.setTime(day);
        int targetYear = targetCal.get(Calendar.YEAR);
        int targetDayOfYear = targetCal.get(Calendar.DAY_OF_YEAR);

        List<Event> allEvents = eventDao.findAll();
        List<Event> matchedEvents = new ArrayList<>();

        for (Event e : allEvents) {
            if (e.getDate() != null) {
                Calendar eventCal = Calendar.getInstance();
                eventCal.setTime(e.getDate());
                if (eventCal.get(Calendar.YEAR) == targetYear && eventCal.get(Calendar.DAY_OF_YEAR) == targetDayOfYear) {
                    matchedEvents.add(e);
                }
            }
        }

        return paginate(matchedEvents, pageSize, pageNum);
    }

    public Event createEvent(Event event) {
        logger.info("EventService: Creating event: {}", event);
        System.out.println("EventService: createEvent called for event: " + (event != null ? event.getTitle() : "null"));
        if (event == null) {
            return null;
        }
        return eventDao.save(event);
    }

    public Event updateEvent(Event event) {
        logger.info("EventService: Updating event: {}", event);
        System.out.println("EventService: updateEvent called for event: " + (event != null ? event.getId() : "null"));

        if (event == null) {
            return null;
        }

        Event existing = eventDao.findById(event.getId());
        if (existing == null) {
            logger.warn("Event with id {} not found for update", event.getId());
            return null;
        }

        return eventDao.save(event);
    }

    public boolean deleteEvent(long eventId) {
        logger.info("EventService: Deleting event with id: {}", eventId);
        System.out.println("EventService: deleteEvent called for id: " + eventId);
        return eventDao.delete(eventId);
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
