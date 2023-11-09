package ru.kaInc.shelterbot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kaInc.shelterbot.model.Ticket;

import java.util.List;

public interface TicketRepo extends JpaRepository<Ticket, Long> {

    List<Ticket> findTicketsByUserName(String username);

    List<Ticket> findTicketsByVolunteerId(Long volunteerId);
}
