package ru.kaInc.shelterbot.controller;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.kaInc.shelterbot.model.Ticket;
import ru.kaInc.shelterbot.service.TicketService;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
public class TicketControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketService ticketService;

    private Ticket ticket;

    @BeforeEach
    public void setup() {
        ticket = new Ticket();
        ticket.setId(1L);
        ticket.setIssueDescription("Test Ticket");

        given(ticketService.findAll()).willReturn(java.util.Collections.singletonList(ticket));
        given(ticketService.findById(anyLong())).willReturn(ticket);
        given(ticketService.findByUsername(any(String.class))).willReturn(java.util.Collections.singletonList(ticket));
        given(ticketService.findByVolunteer(anyLong())).willReturn(java.util.Collections.singletonList(ticket));
        given(ticketService.closeTicket(anyLong())).willReturn(ticket);
    }

    @Test
    public void findAll_ShouldReturnAllTickets() throws Exception {
        mockMvc.perform(get("/ticket"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(ticket.getId().intValue())));
    }

    @Test
    public void findById_ShouldReturnTicket() throws Exception {
        mockMvc.perform(get("/ticket/{id}", ticket.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(ticket.getId().intValue())));
    }

    @Test
    public void findByUserName_ShouldReturnTickets() throws Exception {
        mockMvc.perform(get("/ticket/username?username=testuser"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void findByVolunteer_ShouldReturnTickets() throws Exception {
        mockMvc.perform(get("/ticket/volunteer/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void closeTicket_ShouldCloseTicket() throws Exception {
        mockMvc.perform(patch("/ticket/close/{id}", ticket.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(ticket.getId().intValue())));
    }

    @Test
    public void findAll_ShouldReturnNotFoundWhenNoTickets() throws Exception {
        given(ticketService.findAll()).willReturn(java.util.Collections.emptyList());

        mockMvc.perform(get("/ticket"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findById_ShouldReturnNotFoundWhenTicketDoesNotExist() throws Exception {
        given(ticketService.findById(anyLong())).willReturn(null);

        mockMvc.perform(get("/ticket/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findByUserName_ShouldReturnNotFoundWhenNoTicketsForUser() throws Exception {
        given(ticketService.findByUsername(any(String.class))).willReturn(java.util.Collections.emptyList());

        mockMvc.perform(get("/ticket/username?username=unknown"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findByVolunteer_ShouldReturnNotFoundWhenNoTicketsForVolunteer() throws Exception {
        given(ticketService.findByVolunteer(anyLong())).willReturn(java.util.Collections.emptyList());

        mockMvc.perform(get("/ticket/volunteer/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void closeTicket_ShouldReturnNotFoundWhenTicketDoesNotExist() throws Exception {
        given(ticketService.closeTicket(anyLong())).willReturn(null);

        mockMvc.perform(patch("/ticket/close/{id}", 9999L))
                .andExpect(status().isNotFound());
    }
}