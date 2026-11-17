package com.epam.booking.dao;

import com.epam.booking.model.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class TicketDao {
    private static final Logger logger = LoggerFactory.getLogger(TicketDao.class);
    private Storage storage;
    private AtomicLong idCounter = new AtomicLong(1);

    public TicketDao() {
    }

    // autowired setter
    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public Storage getStorage() {
        return this.storage;
    }

    public Ticket save(Ticket ticket) {
        if (ticket == null) {
            return null;
        }
        if (ticket.getId() <= 0) {
            long newId = getNextId();
            ticket.setId(newId);
        } else {
            if (ticket.getId() >= idCounter.get()) {
                idCounter.set(ticket.getId() + 1);
            }
        }
        logger.debug("Saving ticket into storage: {}", ticket);
        storage.put("ticket:" + ticket.getId(), ticket);
        return ticket;
    }

    public Ticket findById(long id) {
        logger.debug("Finding ticket by id: {}", id);
        return (Ticket) storage.get("ticket:" + id);
    }

    public boolean delete(long id) {
        logger.debug("Deleting ticket with id: {}", id);
        return storage.remove("ticket:" + id) != null;
    }

    public List<Ticket> findAll() {
        logger.debug("Finding all tickets from storage");
        List<Ticket> ticketList = new ArrayList<>();
        Map<String, Object> allData = storage.getAll();
        for (Map.Entry<String, Object> entry : allData.entrySet()) {
            if (entry.getKey().startsWith("ticket:")) {
                if (entry.getValue() instanceof Ticket) {
                    ticketList.add((Ticket) entry.getValue());
                }
            }
        }
        return ticketList;
    }

    public synchronized long getNextId() {
        long max = 0;
        List<Ticket> all = findAll();
        for (Ticket t : all) {
            if (t.getId() > max) {
                max = t.getId();
            }
        }
        if (max >= idCounter.get()) {
            idCounter.set(max + 1);
        }
        return idCounter.getAndIncrement();
    }
}
