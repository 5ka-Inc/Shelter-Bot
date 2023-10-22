package ru.kaInc.shelterbot.service;

import ru.kaInc.shelterbot.model.Shelter;

import java.util.List;

public interface ShelterService {
    Shelter createShelter(Shelter shelter);

    Shelter findById(Long id);

    List<Shelter> findAll();

    List<Shelter> findByShelterType(String type);

    Shelter updateShelter(Shelter shelter);

    void deleteShelter(Long id);
}
