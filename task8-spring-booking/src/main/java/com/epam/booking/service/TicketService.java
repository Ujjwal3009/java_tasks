package com.epam.booking.service;

import com.epam.booking.dao.TicketDao;
import com.epam.booking.model.Event;
import com.epam.booking.model.Ticket;
import com.epam.booking.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class TicketService {
    private static final Logger logger = LoggerFactory.getLogger(TicketService.class);

    private TicketDao ticketDao;

    public TicketService() {
    }

    // setter injection for TicketDao
    public void setTicketDao(TicketDao ticketDao) {
        this.ticketDao = ticketDao;
    }

    public TicketDao getTicketDao() {
        return this.ticketDao;
    }

    public Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category) {
        logger.info("TicketService: Booking ticket - userId={}, eventId={}, place={}, category={}",
                userId, eventId, place, category);
        System.out.println("TicketService: Booking ticket for user " + userId + ", event " + eventId + ", place " + place);

        // Check if place is already booked for this event and category
        List<Ticket> existingTickets = ticketDao.findAll();
        for (Ticket t : existingTickets) {
            if (t.getEventId() == eventId && t.getPlace() == place && t.getCategory() == category) {
                logger.warn("Place {} already booked for event {} in category {}", place, eventId, category);
                System.out.println("TicketService warning: Place already booked!");
                throw new IllegalStateException("Place " + place + " is already booked for event " + eventId);
            }
        }

        Ticket ticket = new Ticket(0, userId, eventId, place, category);
        Ticket savedTicket = ticketDao.save(ticket);
        logger.info("Ticket successfully booked with id: {}", savedTicket.getId());
        return savedTicket;
    }

    public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
        logger.info("TicketService: Fetching booked tickets for user: {}, pageSize={}, pageNum={}",
                user != null ? user.getId() : "null", pageSize, pageNum);

        if (user == null) {
            return new ArrayList<>();
        }

        List<Ticket> allTickets = ticketDao.findAll();
        List<Ticket> userTickets = new ArrayList<>();

        for (Ticket t : allTickets) {
            if (t.getUserId() == user.getId()) {
                userTickets.add(t);
            }
        }

        return paginate(userTickets, pageSize, pageNum);
    }

    public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
        logger.info("TicketService: Fetching booked tickets for event: {}, pageSize={}, pageNum={}",
                event != null ? event.getId() : "null", pageSize, pageNum);

        if (event == null) {
            return new ArrayList<>();
        }

        List<Ticket> allTickets = ticketDao.findAll();
        List<Ticket> eventTickets = new ArrayList<>();

        for (Ticket t : allTickets) {
            if (t.getEventId() == event.getId()) {
                eventTickets.add(t);
            }
        }

        return paginate(eventTickets, pageSize, pageNum);
    }

    public boolean cancelTicket(long ticketId) {
        logger.info("TicketService: Canceling ticket with id: {}", ticketId);
        System.out.println("TicketService: cancelTicket called for id: " + ticketId);
        return ticketDao.delete(ticketId);
    }

    public Ticket getTicketById(long ticketId) {
        logger.info("TicketService: Fetching ticket by id: {}", ticketId);
        return ticketDao.findById(ticketId);
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
