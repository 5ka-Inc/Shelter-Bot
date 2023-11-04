package ru.kaInc.shelterbot.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;
import ru.kaInc.shelterbot.exception.ImageSizeExceededException;
import ru.kaInc.shelterbot.model.Photo;
import ru.kaInc.shelterbot.repo.PhotoRepo;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PhotoServiceImplTest {

    @Mock
    private PhotoRepo photoRepo;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private PhotoServiceImpl photoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findPhotoById_existingId_shouldReturnPhoto() {
        Long photoId = 1L;
        Photo photo = new Photo();
        when(photoRepo.findById(photoId)).thenReturn(Optional.of(photo));

        Optional<Photo> result = photoService.findPhotoById(photoId);

        assertTrue(result.isPresent());
        assertEquals(photo, result.get());
        verify(photoRepo, times(1)).findById(photoId);
    }

    @Test
    void findPhotoById_nonExistingId_shouldThrowEntityNotFoundException() {
        Long photoId = 1L;
        when(photoRepo.findById(photoId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> photoService.findPhotoById(photoId));

        assertEquals(String.format("Photo with id %s not found", photoId), exception.getMessage());
    }

    @Test
    void upLoadPhoto_validInput_shouldSaveAndReturnPhotoId() throws IOException, ImageSizeExceededException {
        Long photoId = 1L;
        when(multipartFile.getSize()).thenReturn(100L);  // Assuming size is valid
        when(photoRepo.findById(photoId)).thenReturn(Optional.empty());
        when(photoRepo.save(any(Photo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Long result = photoService.upLoadPhoto(photoId, multipartFile);

        assertEquals(photoId, result);
        verify(photoRepo, times(1)).save(any(Photo.class));
    }

    // TODO: дописать тесты для эксепшенов

    @Test
    void deletePhoto_existingPhoto_shouldDeletePhoto() {
        Long photoId = 1L;
        when(photoRepo.findById(photoId)).thenReturn(Optional.of(new Photo()));

        photoService.deletePhoto(photoId);

        verify(photoRepo, times(1)).deleteById(photoId);
    }

    @Test
    void deletePhoto_nonExistingPhoto_shouldThrowEntityNotFoundException() {
        Long photoId = 1L;
        when(photoRepo.findById(photoId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> photoService.deletePhoto(photoId));

        assertEquals(String.format("Photo with id %s not found", photoId), exception.getMessage());
    }

    @Test
    void refactorPhoto_validInput_shouldSaveAndReturnPhoto() throws IOException {
        Long photoId = 1L;
        when(multipartFile.getBytes()).thenReturn(new byte[0]);
        when(photoRepo.save(any(Photo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Photo result = photoService.refactorPhoto(photoId, multipartFile);

        assertNotNull(result);
        assertEquals(photoId, result.getId());
        verify(photoRepo, times(1)).save(any(Photo.class));
    }
}
