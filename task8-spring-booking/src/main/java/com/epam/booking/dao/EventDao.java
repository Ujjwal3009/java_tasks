package com.epam.booking.dao;

import com.epam.booking.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EventDao {
    private static final Logger logger = LoggerFactory.getLogger(EventDao.class);
    private Storage storage;

    public EventDao() {
    }

    // autowired setter
    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public Storage getStorage() {
        return this.storage;
    }

    public Event save(Event event) {
        if (event == null) {
            return null;
        }
        if (event.getId() <= 0) {
            long newId = generateNextId();
            event.setId(newId);
        }
        logger.debug("Saving event into storage: {}", event);
        storage.put("event:" + event.getId(), event);
        return event;
    }

    public Event findById(long id) {
        logger.debug("Finding event by id: {}", id);
        return (Event) storage.get("event:" + id);
    }

    public boolean delete(long id) {
        logger.debug("Deleting event with id: {}", id);
        return storage.remove("event:" + id) != null;
    }

    public List<Event> findAll() {
        logger.debug("Finding all events from storage");
        List<Event> eventList = new ArrayList<>();
        Map<String, Object> allData = storage.getAll();
        for (Map.Entry<String, Object> entry : allData.entrySet()) {
            if (entry.getKey().startsWith("event:")) {
                if (entry.getValue() instanceof Event) {
                    eventList.add((Event) entry.getValue());
                }
            }
        }
        return eventList;
    }

    private synchronized long generateNextId() {
        long maxId = 0;
        List<Event> all = findAll();
        for (Event e : all) {
            if (e.getId() > maxId) {
                maxId = e.getId();
            }
        }
        return maxId + 1;
    }
}
