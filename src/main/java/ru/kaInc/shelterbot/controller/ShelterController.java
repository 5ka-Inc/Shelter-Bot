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
import ru.kaInc.shelterbot.model.Shelter;
import ru.kaInc.shelterbot.model.User;
import ru.kaInc.shelterbot.model.enums.Type;
import ru.kaInc.shelterbot.service.ShelterService;

import java.util.List;

/**
 * The ShelterController class is a REST controller that handles shelter-related operations.
 * It provides endpoints for retrieving, modifying, and deleting shelter records.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/shelter")
@Tag(name = "Приюты", description = "Операции с приютами")
public class ShelterController {

    private final ShelterService service;

    /**
     * Retrieves a list of all shelters.
     *
     * @return ResponseEntity containing a list of Shelter objects if found, or a not found response.
     */
    @Operation(summary = "Получить все приюты")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Записи найдены",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = Shelter.class)))),
            @ApiResponse(responseCode = "404", description = "Записи не найдены")})
    @GetMapping
    public ResponseEntity<List<Shelter>> findAll() {
        List<Shelter> shelters = service.findAll();
        if (shelters.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(shelters);
    }

    /**
     * Retrieves a shelter by its unique identifier (id).
     *
     * @param id The unique identifier of the shelter.
     * @return ResponseEntity containing the Shelter object if found, or a not found response.
     */
    @Operation(summary = "Получить приют по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Приют найден",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "404", description = "Приют не найден")})
    @GetMapping("id/{id}")
    public ResponseEntity<Shelter> findShelterById(@Parameter(description = "Идентификатор приюта")
                                                   @PathVariable("id") Long id) {
        Shelter foundShelter = service.findById(id);
        if (foundShelter == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundShelter);
    }

    /**
     * Retrieves a list of shelters based on their type.
     *
     * @param type The type (from the Type enum) to filter shelters by.
     * @return ResponseEntity containing a list of Shelter objects with the specified type if found, or a not found response.
     */
    @Operation(summary = "Получить список приютов, в зависимости от типа")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Приюты найдены",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = Shelter.class)))),
            @ApiResponse(responseCode = "404", description = "Приюты не найдены")})
    @GetMapping("/type")
    public ResponseEntity<List<Shelter>> findByShelterType(@Parameter(description = "Искомый тип (из enum Type)")
                                                           @RequestParam("type") Type type) {
        List<Shelter> foundShelters = service.findByShelterType(type);
        if (foundShelters.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundShelters);
    }

    /**
     * Modifies shelter data.
     *
     * @param shelter The Shelter object with modified data.
     * @return ResponseEntity containing the updated Shelter object if successful, or a not found response if the shelter is not found.
     */
    @Operation(summary = "Изменить данные приюта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные изменены",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "404", description = "Приют не найден")})
    @PutMapping
    public ResponseEntity<?> changeShelter(@RequestBody Shelter shelter) {
        Shelter currentShelter = service.findById(shelter.getId());
        if (currentShelter == null) {
            return ResponseEntity.notFound().build();
        }
        Shelter updatedShelter = service.updateShelter(shelter);
        return ResponseEntity.ok(updatedShelter);
    }

    /**
     * Deletes a shelter by its unique identifier (id).
     *
     * @param id The unique identifier of the shelter to be deleted.
     * @return ResponseEntity indicating the success of the deletion or a not found response if the shelter is not found.
     */
    @Operation(summary = "Удалить приют")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Приют успешно удален"),
            @ApiResponse(responseCode = "404", description = "Приют не найден")})
    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteShelter(@Parameter(description = "Идентификатор приюта")
                                              @PathVariable Long id) {
        Shelter currentShelter = service.findById(id);
        if (currentShelter == null) {
            return ResponseEntity.notFound().build();
        }
        service.deleteShelter(id);
        return ResponseEntity.ok().build();
    }
}
