package ru.kaInc.shelterbot.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.kaInc.shelterbot.model.Shelter;
import ru.kaInc.shelterbot.model.enums.Type;
import ru.kaInc.shelterbot.repo.ShelterRepo;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ShelterServiceImplTest {

    @Mock
    private ShelterRepo shelterRepo;

    @InjectMocks
    private ShelterServiceImpl shelterService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createShelter() {
        Shelter shelter = new Shelter();
        when(shelterRepo.save(any(Shelter.class))).thenReturn(shelter);

        Shelter result = shelterService.createShelter(shelter);

        assertNotNull(result);
        verify(shelterRepo, times(1)).save(shelter);
    }

    @Test
    void findById_existingId_shouldReturnShelter() {
        Shelter shelter = new Shelter();
        shelter.setId(1L);
        when(shelterRepo.findById(anyLong())).thenReturn(Optional.of(shelter));

        Shelter result = shelterService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void findById_nonExistingId_shouldThrowException() {
        when(shelterRepo.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> shelterService.findById(1L));
    }

    @Test
    void findAll() {
        List<Shelter> shelters = Arrays.asList(new Shelter(), new Shelter());
        when(shelterRepo.findAll()).thenReturn(shelters);

        List<Shelter> result = shelterService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void findByShelterType_existingType_shouldReturnShelters() {
        List<Shelter> shelters = Arrays.asList(new Shelter(), new Shelter());
        when(shelterRepo.findShelterByType(anyString())).thenReturn(shelters);

        List<Shelter> result = shelterService.findByShelterType("someType");

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void findByShelterType_nonExistingType_shouldThrowException() {
        when(shelterRepo.findShelterByType(anyString())).thenReturn(Arrays.asList());

        assertThrows(EntityNotFoundException.class, () -> shelterService.findByShelterType("someType"));
    }

    @Test
    void updateShelter() {
        Shelter shelter = new Shelter();
        shelter.setId(1L);
        when(shelterRepo.findById(anyLong())).thenReturn(Optional.of(shelter));
        when(shelterRepo.save(any(Shelter.class))).thenReturn(shelter);

        Shelter updatedShelter = new Shelter();
        updatedShelter.setId(1L);
        updatedShelter.setName("New Name");
        updatedShelter.setType(Type.DOG);

        Shelter result = shelterService.updateShelter(updatedShelter);

        assertNotNull(result);
        assertEquals("New Name", result.getName());
        assertEquals(Type.DOG, result.getType());
    }

    @Test
    void deleteShelter_existingId_shouldDeleteShelter() {
        Shelter shelter = new Shelter();
        shelter.setId(1L);
        when(shelterRepo.findById(anyLong())).thenReturn(Optional.of(shelter));

        shelterService.deleteShelter(1L);

        verify(shelterRepo, times(1)).deleteById(1L);
    }

    @Test
    void deleteShelter_nonExistingId_shouldThrowException() {
        when(shelterRepo.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> shelterService.deleteShelter(1L));
    }
}
