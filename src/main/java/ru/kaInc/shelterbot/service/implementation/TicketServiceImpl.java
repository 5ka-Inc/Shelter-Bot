package ru.kaInc.shelterbot.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepo ticketRepo;

    private final UserService userService;

    private Logger logger = LoggerFactory.getLogger(TicketServiceImpl.class);

    @Override
    @Transactional
    public Ticket createTicket(String issueDescription, String username) {
        Ticket ticket = new Ticket();
        ticket.setIssueDescription(issueDescription);
        ticket.setCreationTime(LocalDateTime.now());
        ticket.setCreatorsUsername(username);
        ticket.setIsClosed(false);

        ticketRepo.save(ticket);
        logger.info("ticket {} saved", ticket.getIssueDescription());

        User volunteer = userService.findAvailableVolunteer();
        logger.info("ticket {} assigned to a volunteer {}", ticket.getIssueDescription(), volunteer.getName());

        ticket.setVolunteer(volunteer);
        ticket.setReceivedByVolunteerTime(LocalDateTime.now());
        ticketRepo.save(ticket);
        return ticket;
    }

    @Override
    public Ticket findById(Long id) {
        Ticket foundTicket = ticketRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Ticket %s not found", id)));
        return foundTicket;
    }
    @Override
    @Transactional
    public Ticket closeTicket(Long id) {
        Ticket foundTicket = findById(id);

        foundTicket.setIsClosed(true);
        return ticketRepo.save(foundTicket);
    }

    @Override
    public List<Ticket> findAll() {
        List<Ticket> tickets = ticketRepo.findAll();
        if (tickets.isEmpty()) {
            throw new EntityNotFoundException(String.format("Tickets not found"));
        }
        return tickets;
    }

    @Override
    public List<Ticket> findByUsername(String username) {
        List<Ticket> tickets = ticketRepo.findTicketsByUserName(username);
        if (tickets.isEmpty()) {
            throw new EntityNotFoundException(String.format("Tickets not found for user %s", username));
        }
        return tickets;
    }

    @Override
    public List<Ticket> findByVolunteer(Long id) {
        List<Ticket> tickets = ticketRepo.findTicketsByVolunteerId(id);
        if (tickets.isEmpty()) {
            throw new EntityNotFoundException(String.format("Tickets not found for volunteer %s", id));
        }
        return tickets;
    }

    @Override
    public List<Ticket> findAllOpen(Long id) {
        List<Ticket> tickets = ticketRepo.findAllOpen(id);
        if (tickets.isEmpty()) {
            throw new EntityNotFoundException(String.format("Open tickets not found for volunteer %s", id));
        }
        return tickets;
    }
}

