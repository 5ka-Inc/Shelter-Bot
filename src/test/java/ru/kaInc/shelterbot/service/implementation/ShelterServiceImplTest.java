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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ShelterServiceImplTest {

    @Mock
    private ShelterRepo shelterRepo;

    @InjectMocks
    private ShelterServiceImpl shelterService;

    private Shelter shelterDog;
    private Shelter shelterCat;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        shelterDog = createShelter(1L, "Dog Shelter", Type.DOG);
        shelterCat = createShelter(2L, "Cat Shelter", Type.CAT);
    }

    private Shelter createShelter(Long id, String name, Type type) {
        Shelter shelter = new Shelter();
        shelter.setId(id);
        shelter.setName(name);
        shelter.setType(type);
        return shelter;
    }

    @Test
    public void testCreateShelter() {
        when(shelterRepo.save(any(Shelter.class))).thenReturn(shelterDog);

        Shelter created = shelterService.createShelter(shelterDog);

        assertNotNull(created);
        assertEquals(shelterDog, created);
    }

    @Test
    public void testFindByIdFound() {
        when(shelterRepo.findById(anyLong())).thenReturn(Optional.of(shelterDog));

        Shelter found = shelterService.findById(shelterDog.getId());

        assertNotNull(found);
        assertEquals(shelterDog.getId(), found.getId());
    }

    @Test
    public void testFindByIdNotFound() {
        when(shelterRepo.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> shelterService.findById(anyLong()));
    }

    @Test
    public void testFindAll() {
        when(shelterRepo.findAll()).thenReturn(List.of(shelterDog, shelterCat));

        List<Shelter> foundShelters = shelterService.findAll();

        assertNotNull(foundShelters);
        assertEquals(2, foundShelters.size());
    }

    @Test
    public void testFindByShelterTypeFound() {
        String type = "DOG";
        when(shelterRepo.findShelterByType(type)).thenReturn(List.of(shelterDog));

        List<Shelter> foundShelters = shelterService.findByShelterType(type);

        assertNotNull(foundShelters);
        assertEquals(1, foundShelters.size());
    }

    @Test
    public void testFindByShelterTypeNotFound() {
        when(shelterRepo.findShelterByType(anyString())).thenReturn(List.of());

        assertThrows(EntityNotFoundException.class, () -> shelterService.findByShelterType(anyString()));
    }

    @Test
    public void testUpdateShelter() {
        when(shelterRepo.findById(anyLong())).thenReturn(Optional.of(shelterDog));
        when(shelterRepo.save(any(Shelter.class))).thenReturn(shelterDog);

        shelterDog.setName("Абракадабра");
        Shelter updated = shelterService.updateShelter(shelterDog);

        assertNotNull(updated);
        assertEquals("Абракадабра", updated.getName());
    }

    @Test
    public void testDeleteShelterFound() {
        when(shelterRepo.findById(anyLong())).thenReturn(Optional.of(shelterDog));
        doNothing().when(shelterRepo).deleteById(shelterDog.getId());

        assertDoesNotThrow(() -> shelterService.deleteShelter(shelterDog.getId()));
        verify(shelterRepo, times(1)).deleteById(shelterDog.getId());
    }

    @Test
    public void testDeleteShelterNotFound() {
        when(shelterRepo.findById(shelterDog.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> shelterService.deleteShelter(shelterDog.getId()));
    }
}
