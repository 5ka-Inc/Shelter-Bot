package ru.kaInc.shelterbot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kaInc.shelterbot.model.Shelter;

import java.util.List;

public interface ShelterRepo extends JpaRepository<Shelter, Long> {

    List<Shelter> findShelterByType(String type);
}
