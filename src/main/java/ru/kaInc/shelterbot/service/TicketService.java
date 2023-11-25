package ru.kaInc.shelterbot.service;

import ru.kaInc.shelterbot.model.Ticket;

import java.util.List;

public interface TicketService {
    Ticket createTicket(String issueDescription, String username);

    Ticket findById(Long id);

    Ticket closeTicket(Long id);

    List<Ticket> findAll();

    List<Ticket> findByUsername(String username);

    List<Ticket> findByVolunteer(Long id);

    List<Ticket> findAllOpen(Long id);
}
