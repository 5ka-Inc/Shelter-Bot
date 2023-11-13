package ru.kaInc.shelterbot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kaInc.shelterbot.model.Ticket;

import java.util.List;

public interface TicketRepo extends JpaRepository<Ticket, Long> {

    List<Ticket> findTicketsByUserName(String username);

    List<Ticket> findTicketsByVolunteerId(Long volunteerId);

    //    @Query(value = "SELECT t FROM tickets t WHERE t.is_closed = false AND t.volunteer_id = :volunteer_id", nativeQuery = true)
//    List<Ticket> findAllOpen(@Param("volunteerId") Long volunteerId);
    @Query("SELECT t FROM Ticket t WHERE t.isClosed = false AND t.volunteer.id = :volunteerId")
    List<Ticket> findAllOpen(@Param("volunteerId") Long volunteerId);

}
