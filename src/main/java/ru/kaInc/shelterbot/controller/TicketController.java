package ru.kaInc.shelterbot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kaInc.shelterbot.model.Ticket;
import ru.kaInc.shelterbot.service.TicketService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ticket")
@Tag(name = "Тикеты", description = "Api для работы с обращениями пользователей")
public class TicketController {

    private final TicketService ticketService;

    @Operation(summary = "Получить все тикеты")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Записи найдены",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = Ticket.class)))),
            @ApiResponse(responseCode = "404", description = "Записи не найдены")})
    @GetMapping
    public ResponseEntity<?> findAll() {
        List<Ticket> tickets = ticketService.findAll();
        if (tickets.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tickets);
    }

    @Operation(summary = "Получить тикет по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Тикет найден",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Ticket.class))}),
            @ApiResponse(responseCode = "404", description = "Тикет не найден")})
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {
        Ticket foundTicket = ticketService.findById(id);
        if (foundTicket == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundTicket);
    }

    @Operation(summary = "Получить все тикеты пользователя по username из telegram")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Записи найдены",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = Ticket.class)))),
            @ApiResponse(responseCode = "404", description = "Записи не найдены")})
    @GetMapping("/username")
    public ResponseEntity<?> findByUserName(@Parameter(description = "Имя пользователя в telegram") @RequestParam String username) {
        List<Ticket> tickets = ticketService.findByUsername(username);
        if (tickets.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tickets);
    }

    @Operation(summary = "Получить все тикеты, присвоенные волонтеру")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Записи найдены",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = Ticket.class)))),
            @ApiResponse(responseCode = "404", description = "Записи не найдены")})
    @GetMapping("/volunteer/{id}")
    public ResponseEntity<?> findByVolunteer(@PathVariable("id") Long id) {
        List<Ticket> tickets = ticketService.findByVolunteer(id);
        if (tickets.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tickets);
    }

    @Operation(summary = "Получить все нерешенные тикеты, присвоенные волонтеру")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Записи найдены",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = Ticket.class)))),
            @ApiResponse(responseCode = "404", description = "Записи не найдены")})
    @GetMapping("/volunteer/open/{id}")
    public ResponseEntity<?> findAllOpen(@PathVariable("id") Long id) {
        List<Ticket> tickets = ticketService.findAllOpen(id);
        if (tickets.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tickets);
    }

    @Operation(summary = "Закрыть тикет")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Тикет закрыт",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Ticket.class))}),
            @ApiResponse(responseCode = "404", description = "Тикет не найден")})
    @PatchMapping("/close/{id}")
    public ResponseEntity<?> closeTicket(@PathVariable("id") Long id) {
        Ticket closedTicket = ticketService.closeTicket(id);
        if (closedTicket == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(closedTicket);
    }
}
