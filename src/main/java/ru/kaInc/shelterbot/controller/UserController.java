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
import ru.kaInc.shelterbot.model.User;
import ru.kaInc.shelterbot.model.enums.Role;
import ru.kaInc.shelterbot.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "Пользователи", description = "Операции с пользователями")
public class UserController {

    private final UserService service;

    @Operation(summary = "Получить всех пользователей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Записи найдены",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = User.class)))),
            @ApiResponse(responseCode = "404", description = "Записи не найдены")})
    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        List<User> users = service.findAll();
        if (users.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Получить пользователя по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь найден",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")})
    @GetMapping("id/{id}")
    public ResponseEntity<User> findUserById(@Parameter(description = "Идентификатор пользователя")
                                             @PathVariable("id") Long id) {
        User foundUser = service.findById(id);
        if (foundUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundUser);
    }

    @Operation(summary = "Получить список пользователей с определенной ролью")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователи найдены",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = User.class)))),
            @ApiResponse(responseCode = "404", description = "Пользователи не найдены")})
    @GetMapping("role/{role}")
    public ResponseEntity<List<User>> getUsersByRole(@Parameter(description = "Искомая роль (из enum Role)")
                                                     @PathVariable("role") Role role) {
        List<User> foundUsers = service.findUsersByRole(role);
        if (foundUsers == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundUsers);
    }

    @Operation(summary = "Изменить данные пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные изменены",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")})
    @PutMapping
    public ResponseEntity<?> changeUser(@RequestBody User user) {
        User currentUser = service.findById(user.getId());
        if (currentUser == null) {
            return ResponseEntity.notFound().build();
        }
        User updatedUser = service.updateUser(user);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Удалить пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно удален"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")})
    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteUser(@Parameter(description = "Идентификатор пользователя")
                                           @PathVariable Long id) {
        User currentUser = service.findById(id);
        if (currentUser == null) {
            return ResponseEntity.notFound().build();
        }
        service.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
