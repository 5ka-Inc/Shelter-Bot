package ru.kaInc.shelterbot.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.kaInc.shelterbot.model.Shelter;
import ru.kaInc.shelterbot.repo.ShelterRepo;
import ru.kaInc.shelterbot.service.ShelterService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShelterServiceImpl implements ShelterService {

    private ShelterRepo shelterRepo;

    @Override
    public Shelter createShelter(Shelter shelter) {
        return shelterRepo.save(shelter);
    }

    @Override
    public Shelter findById(Long id) {
        return shelterRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Shelter with id %s not found", id)));
    }

    @Override
    public List<Shelter> findAll() {
        return shelterRepo.findAll();
    }

    @Override
    public List<Shelter> findByShelterType(String type) {
        List<Shelter> shelters = shelterRepo.findShelterByType(type);
        if (shelters.isEmpty()) {
            throw new EntityNotFoundException(String.format("Not found shelters with type %s", type));
        }
        return shelters;
    }

    @Override
    public Shelter updateShelter(Shelter shelter) {
        Shelter foundShelter = findById(shelter.getId());

        foundShelter.setName(shelter.getName());
        foundShelter.setType(shelter.getType());

        return shelterRepo.save(foundShelter);
    }

    @Override
    public void deleteShelter(Long id) {
        if (shelterRepo.findById(id).isEmpty()) {
            throw new EntityNotFoundException(String.format("Not found shelter with id %s", id));
        }
        shelterRepo.deleteById(id);
    }
}
