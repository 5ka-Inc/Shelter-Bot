package ru.kaInc.shelterbot.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.kaInc.shelterbot.model.Ticket;
import ru.kaInc.shelterbot.model.User;
import ru.kaInc.shelterbot.repo.TicketRepo;
import ru.kaInc.shelterbot.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TicketServiceImplTest {

    @Mock
    private TicketRepo ticketRepo;

    @Mock
    private UserService userService;

    @InjectMocks
    private TicketServiceImpl ticketService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTicketShouldCreateAndReturnTicket() {
        String issueDescription = "Test Issue";
        String username = "Test User";

        when(userService.findAvailableVolunteer()).thenReturn(new User());

        Ticket ticket = ticketService.createTicket(issueDescription, username);

        assertNotNull(ticket);
        assertEquals(issueDescription, ticket.getIssueDescription());
        assertEquals(username, ticket.getCreatorsUsername());
        assertFalse(ticket.getIsClosed());

        verify(ticketRepo, times(2)).save(any(Ticket.class));
        verify(userService, times(1)).findAvailableVolunteer();
    }

    @Test
    void findByIdShouldReturnTicket() {
        Long ticketId = 1L;
        Ticket expectedTicket = new Ticket();
        expectedTicket.setId(ticketId);

        when(ticketRepo.findById(ticketId)).thenReturn(Optional.of(expectedTicket));

        Ticket actualTicket = ticketService.findById(ticketId);

        assertNotNull(actualTicket);
        assertEquals(ticketId, actualTicket.getId());
    }

    @Test
    void findByIdShouldThrowEntityNotFoundException() {
        Long ticketId = 1L;
        when(ticketRepo.findById(ticketId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> ticketService.findById(ticketId));
    }

    @Test
    void closeTicketShouldCloseTicket() {
        Long ticketId = 1L;
        Ticket ticket = new Ticket();
        ticket.setId(ticketId);
        ticket.setIsClosed(false);

        when(ticketRepo.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(ticketRepo.save(any(Ticket.class))).thenAnswer(i -> i.getArgument(0));

        Ticket closedTicket = ticketService.closeTicket(ticketId);

        assertTrue(closedTicket.getIsClosed());
    }

    @Test
    void findAllShouldReturnListOfTickets() {
        when(ticketRepo.findAll()).thenReturn(Collections.singletonList(new Ticket()));

        List<Ticket> tickets = ticketService.findAll();

        assertFalse(tickets.isEmpty());
    }

    @Test
    void findAllShouldThrowEntityNotFoundException() {
        when(ticketRepo.findAll()).thenReturn(Collections.emptyList());

        assertThrows(EntityNotFoundException.class, () -> ticketService.findAll());
    }

    @Test
    void findByUsernameShouldReturnTickets() {
        String username = "Test User";
        when(ticketRepo.findTicketsByUserName(username)).thenReturn(Collections.singletonList(new Ticket()));

        List<Ticket> tickets = ticketService.findByUsername(username);

        assertFalse(tickets.isEmpty());
    }

    @Test
    void findByUsernameShouldThrowEntityNotFoundException() {
        String username = "Test User";
        when(ticketRepo.findTicketsByUserName(username)).thenReturn(Collections.emptyList());

        assertThrows(EntityNotFoundException.class, () -> ticketService.findByUsername(username));
    }

    @Test
    void findByVolunteerShouldReturnTickets() {
        Long volunteerId = 1L;
        when(ticketRepo.findTicketsByVolunteerId(volunteerId)).thenReturn(Collections.singletonList(new Ticket()));

        List<Ticket> tickets = ticketService.findByVolunteer(volunteerId);

        assertFalse(tickets.isEmpty());
    }

    @Test
    void findByVolunteerShouldThrowEntityNotFoundException() {
        Long volunteerId = 1L;
        when(ticketRepo.findTicketsByVolunteerId(volunteerId)).thenReturn(Collections.emptyList());

        assertThrows(EntityNotFoundException.class, () -> ticketService.findByVolunteer(volunteerId));
    }
}