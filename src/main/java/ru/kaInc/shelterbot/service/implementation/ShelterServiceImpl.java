package ru.kaInc.shelterbot.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.kaInc.shelterbot.model.Shelter;
import ru.kaInc.shelterbot.repo.ShelterRepo;
import ru.kaInc.shelterbot.service.ShelterService;

import java.util.List;

/**
 * The ShelterServiceImpl class is an implementation of the ShelterService interface and is responsible for managing shelter-related operations in the bot's system.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ShelterServiceImpl implements ShelterService {

    private final ShelterRepo shelterRepo;

    /**
     * Creates a new shelter in the bot's database with the provided shelter details.
     *
     * @param shelter The Shelter object representing the details of the shelter to be created.
     * @return The created Shelter object with a unique identifier.
     */
    @Override
    public Shelter createShelter(Shelter shelter) {
        return shelterRepo.save(shelter);
    }

    /**
     * Retrieves a shelter from the bot's database based on its unique identifier.
     *
     * @param id The unique identifier of the shelter to be retrieved.
     * @return The Shelter object associated with the specified identifier.
     * @throws EntityNotFoundException if the shelter is not found.
     */
    @Override
    public Shelter findById(Long id) {
        return shelterRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Shelter with id %s not found", id)));
    }

    /**
     * Retrieves a list of all shelters available in the bot's system.
     *
     * @return A list of Shelter objects representing all the shelters in the database.
     */
    @Override
    public List<Shelter> findAll() {
        return shelterRepo.findAll();
    }

    /**
     * Retrieves a list of shelters based on their type.
     *
     * @param type The type of shelter to filter by (e.g., "animal shelter," "homeless shelter").
     * @return A list of Shelter objects that match the specified shelter type.
     * @throws EntityNotFoundException if no shelters with the specified type are found.
     */
    @Override
    public List<Shelter> findByShelterType(String type) {
        List<Shelter> shelters = shelterRepo.findShelterByType(type);
        if (shelters.isEmpty()) {
            throw new EntityNotFoundException(String.format("Not found shelters with type %s", type));
        }
        return shelters;
    }

    /**
     * Updates the information of an existing shelter in the bot's database.
     *
     * @param shelter The Shelter object with modified details to be updated in the database.
     * @return The updated Shelter object.
     */
    @Override
    public Shelter updateShelter(Shelter shelter) {
        Shelter foundShelter = findById(shelter.getId());

        foundShelter.setName(shelter.getName());
        foundShelter.setType(shelter.getType());

        return shelterRepo.save(foundShelter);
    }

    /**
     * Deletes a shelter from the bot's database based on its unique identifier.
     *
     * @param id The unique identifier of the shelter to be deleted.
     * @throws EntityNotFoundException if the shelter is not found.
     */
    @Override
    public void deleteShelter(Long id) {
        if (shelterRepo.findById(id).isEmpty()) {
            throw new EntityNotFoundException(String.format("Not found shelter with id %s", id));
        }
        shelterRepo.deleteById(id);
    }
}
