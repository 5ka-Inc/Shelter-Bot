package ru.kaInc.shelterbot.service.implementation;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.kaInc.shelterbot.model.Ticket;
import ru.kaInc.shelterbot.model.User;
import ru.kaInc.shelterbot.repo.TicketRepo;
import ru.kaInc.shelterbot.service.TicketService;
import ru.kaInc.shelterbot.service.UserService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepo ticketRepo;

    private final UserService userService;

    private Logger logger = LoggerFactory.getLogger(TicketServiceImpl.class);

    @Override
    public Ticket createTicket(String issueDescription, String username) {
        Ticket ticket = new Ticket();
        ticket.setIssueDescription(issueDescription);
        ticket.setCreationTime(LocalDateTime.now());
        ticket.setCreatorsUsername(username);

        ticketRepo.save(ticket);
        logger.debug("ticket {} saved", ticket.getIssueDescription());

        User volunteer = userService.findAvailableVolunteer();
        logger.debug("ticket {} assigned to a volunteer {}", ticket.getIssueDescription(), volunteer.getName());

        ticket.setVolunteer(volunteer);
        ticket.setReceivedByVolunteerTime(LocalDateTime.now());
        return ticketRepo.save(ticket);
    }
}

