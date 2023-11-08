package ru.kaInc.shelterbot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kaInc.shelterbot.model.Ticket;

public interface TicketRepo extends JpaRepository<Ticket, Long> {
}
