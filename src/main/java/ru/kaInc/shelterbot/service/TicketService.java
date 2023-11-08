package ru.kaInc.shelterbot.service;

import ru.kaInc.shelterbot.model.Ticket;

public interface TicketService {
    Ticket createTicket(String issueDescription, String username);
}
