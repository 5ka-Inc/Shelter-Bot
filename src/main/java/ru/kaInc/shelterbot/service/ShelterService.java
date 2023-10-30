package ru.kaInc.shelterbot.service;

import ru.kaInc.shelterbot.model.Shelter;

import java.util.List;

/**
 * The ShelterService interface defines methods for managing shelters in the bot's system.
 * It provides functionality for creating, retrieving, updating, and deleting shelter information.
 */
public interface ShelterService {

    /**
     * Creates a new shelter in the bot's database with the provided shelter information.
     *
     * @param shelter The Shelter object representing the details of the shelter to be created.
     * @return The created Shelter object with a unique identifier.
     */
    Shelter createShelter(Shelter shelter);

    /**
     * Retrieves a shelter from the bot's database based on its unique identifier.
     *
     * @param id The unique identifier of the shelter to be retrieved.
     * @return The Shelter object associated with the specified identifier, or null if not found.
     */

    Shelter findById(Long id);

    /**
     * Retrieves a list of all shelters available in the bot's system.
     *
     * @return A list of Shelter objects representing all the shelters in the database.
     */

    List<Shelter> findAll();

    /**
     * Retrieves a list of shelters based on their type.
     *
     * @param type The type of shelter to filter by (e.g., "animal shelter," "homeless shelter").
     * @return A list of Shelter objects that match the specified shelter type.
     */

    List<Shelter> findByShelterType(String type);

    /**
     * Updates the information of an existing shelter in the bot's database.
     *
     * @param shelter The Shelter object with modified details to be updated in the database.
     * @return The updated Shelter object.
     */

    Shelter updateShelter(Shelter shelter);

    /**
     * Deletes a shelter from the bot's database based on its unique identifier.
     *
     * @param id The unique identifier of the shelter to be deleted.
     */
    void deleteShelter(Long id);
}
