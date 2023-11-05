package ru.kaInc.shelterbot.service.implementation;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartException;
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

    @InjectMocks
    private PhotoServiceImpl photoService;

    private Photo photo;
    private Long photoId;
    private MultipartFile multipartFile;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        photoId = 1L;
        photo = new Photo();
        photo.setId(photoId);
        multipartFile = new MockMultipartFile(
                "photo", "test.jpg", "image/jpeg", "test image content".getBytes());
    }

    @Test
    public void findPhotoById_Found() {
        when(photoRepo.findById(photoId)).thenReturn(Optional.of(photo));

        Photo foundPhoto = photoService.findPhotoById(photoId);

        assertNotNull(foundPhoto);
        assertEquals(photoId, foundPhoto.getId());
        verify(photoRepo).findById(photoId);
    }

    @Test
    public void findPhotoById_NotFound_ThrowsEntityNotFoundException() {
        when(photoRepo.findById(photoId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> photoService.findPhotoById(photoId));
    }

    @Test
    public void upLoadPhoto_Success() throws IOException, ImageSizeExceededException {
        when(photoRepo.findById(photoId)).thenReturn(Optional.empty());
        when(photoRepo.save(any(Photo.class))).thenReturn(photo);

        Long uploadedPhotoId = photoService.upLoadPhoto(photoId, multipartFile);

        assertNotNull(uploadedPhotoId);
        verify(photoRepo).save(any(Photo.class));
    }

    @Test
    public void upLoadPhoto_NullPhoto_ThrowsMultipartException() {
        assertThrows(MultipartException.class, () -> photoService.upLoadPhoto(photoId, null));
    }

    @Test
    public void upLoadPhoto_ExceedsSize_ThrowsImageSizeExceededException() {
        long MAX_SIZE = 1024 * 600;
        MultipartFile bigFile = new MockMultipartFile(
                "bigphoto", "bigtest.jpg", "image/jpeg", new byte[(int) (MAX_SIZE + 1)]);

        assertThrows(ImageSizeExceededException.class, () -> photoService.upLoadPhoto(photoId, bigFile));
    }

    @Test
    public void upLoadPhoto_AlreadyExists_ThrowsEntityExistsException() {
        when(photoRepo.findById(photoId)).thenReturn(Optional.of(photo));

        assertThrows(EntityExistsException.class, () -> photoService.upLoadPhoto(photoId, multipartFile));
    }

    @Test
    public void deletePhoto_Success() {
        when(photoRepo.findById(photoId)).thenReturn(Optional.of(photo));
        doNothing().when(photoRepo).deleteById(photoId);

        assertDoesNotThrow(() -> photoService.deletePhoto(photoId));
        verify(photoRepo).deleteById(photoId);
    }

    @Test
    public void deletePhoto_NotFound_ThrowsEntityNotFoundException() {
        when(photoRepo.findById(photoId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> photoService.deletePhoto(photoId));
    }

    @Test
    public void refactorPhoto_Success() {
        when(photoRepo.save(any(Photo.class))).thenReturn(photo);

        Photo savedPhoto = photoService.refactorPhoto(photoId, multipartFile);

        assertNotNull(savedPhoto);
        verify(photoRepo).save(any(Photo.class));
    }

    @Test
    public void refactorPhoto_Fail_ThrowsRuntimeException() throws IOException {
        MultipartFile fileThatCausesIOException = mock(MultipartFile.class);
        when(fileThatCausesIOException.getBytes()).thenThrow(new IOException());

        assertThrows(RuntimeException.class, () -> photoService.refactorPhoto(photoId, fileThatCausesIOException));
    }
}

