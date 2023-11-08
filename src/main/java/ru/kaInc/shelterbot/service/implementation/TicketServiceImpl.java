package ru.kaInc.shelterbot.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.kaInc.shelterbot.model.Ticket;
import ru.kaInc.shelterbot.model.User;
import ru.kaInc.shelterbot.repo.TicketRepo;
import ru.kaInc.shelterbot.service.TicketService;
import ru.kaInc.shelterbot.service.UserService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketServiceImpl implements TicketService {

    private final TicketRepo ticketRepo;

    private final UserService userService;

    @Override
    public Ticket createTicket(String errorDescription, Long chatId) {
        User user = userService.findByChatId(chatId);
        Ticket ticket = new Ticket();
        ticket.setErrorDescription(errorDescription);
        ticket.setUser(user);
        ticket.setCreationTime(LocalDateTime.now());
        ticket.setCreatorsUsername(user.getUsername());

        // Сохраняем тикет в базе данных
        ticketRepo.save(ticket);

        // Находим доступного волонтера
        User volunteer = userService.findAvailableVolunteer();

        // Присваиваем тикет волонтеру
        ticket.setVolunteer(volunteer);
        ticket.setReceivedByVolunteerTime(LocalDateTime.now());
        return ticketRepo.save(ticket);
    }
}

