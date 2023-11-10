package ru.kaInc.shelterbot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kaInc.shelterbot.model.Shelter;
import ru.kaInc.shelterbot.model.enums.Type;

import java.util.List;

/**
 * The ShelterRepo interface extends JpaRepository for Shelter entities and provides custom query methods for accessing and managing shelter data in the database.
 */
public interface ShelterRepo extends JpaRepository<Shelter, Long> {

    /**
     * Retrieves a list of shelters with a specific shelter type from the database.
     *
     * @param type The type of shelters to filter by (e.g., "animal shelter," "homeless shelter").
     * @return A list of Shelter objects that match the specified shelter type.
     */
    List<Shelter> findShelterByType(Type type);
}
