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

/**
 * The UserController class is a REST controller that handles user-related operations.
 * It provides endpoints for retrieving, modifying, and deleting user records.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "Пользователи", description = "Операции с пользователями")
public class UserController {

    private final UserService service;

    /**
     * Retrieves a list of all users.
     *
     * @return ResponseEntity containing a list of User objects if found, or a not found response.
     */
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

    /**
     * Retrieves a user by their unique identifier (id).
     *
     * @param id The unique identifier of the user.
     * @return ResponseEntity containing the User object if found, or a not found response.
     */
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

    /**
     * Retrieves a list of users with a specific role.
     *
     * @param role The role (from the Role enum) to filter users by.
     * @return ResponseEntity containing a list of User objects with the specified role if found, or a not found response.
     */
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
        if (foundUsers.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundUsers);
    }

    /**
     * Modifies user data.
     *
     * @param user The User object with modified data.
     * @return ResponseEntity containing the updated User object if successful, or a not found response if the user is not found.
     */
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

    /**
     * Deletes a user by their unique identifier (id).
     *
     * @param id The unique identifier of the user to be deleted.
     * @return ResponseEntity indicating the success of the deletion or a not found response if the user is not found.
     */
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
