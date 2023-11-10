package ru.kaInc.shelterbot.service.implementation;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import ru.kaInc.shelterbot.exception.ImageSizeExceededException;
import ru.kaInc.shelterbot.model.Photo;
import ru.kaInc.shelterbot.repo.PhotoRepo;
import ru.kaInc.shelterbot.service.PhotoService;


import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Service
@Transactional
public class PhotoServiceImpl implements PhotoService {
    private final Logger logger = LoggerFactory.getLogger(PhotoServiceImpl.class);

    private long MAX_SIZE = 1024 * 600;
    private final PhotoRepo photoRepo;

    public PhotoServiceImpl(PhotoRepo photoRepo) {
        this.photoRepo = photoRepo;
    }

    @Override
    public Optional<Photo> findPhotoById(Long id) {
        if (photoRepo.findById(id) == null) {
            throw new EntityNotFoundException(String.format("Photo with id %s not found", id));
        }
        return photoRepo.findById(id);
    }

    @Override
    @Transactional
    public Long upLoadPhoto(Long id, MultipartFile multipartFile) throws IOException, ImageSizeExceededException {
        if (multipartFile == null) {
            throw new MultipartException("Photo was null");
        }
        if (multipartFile.getSize() > MAX_SIZE) {
            throw new ImageSizeExceededException("Photo size is too big, max size ", MAX_SIZE);
        }
        if (photoRepo.findById(id).isPresent()) {
            throw new EntityExistsException(String.format("Photo with id %s exists", id));
        }
        Photo newPhoto = new Photo();

        newPhoto.setId(id);
        newPhoto.setFileSize(multipartFile.getSize());
        newPhoto.setData(generateDataForDB(multipartFile));

        photoRepo.save(newPhoto);

        return newPhoto.getId();
    }

    private byte[] generateDataForDB(MultipartFile file) throws IOException {
        try (InputStream is = file.getInputStream();
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            int read;
            byte[] buffer = new byte[1024];
            while ((read = bis.read(buffer)) != -1) {
                baos.write(buffer, 0, read);
            }
            return baos.toByteArray();
        }
    }

    @Override
    public void deletePhoto(Long photoId) {
        logger.info("Photo deleted");
        if (photoRepo.findById(photoId).isEmpty()) {
            throw new EntityNotFoundException(String.format("Photo with id %s not found", photoId));
        }
        photoRepo.deleteById(photoId);
    }



    @Override
    public Photo refactorPhoto(Long id, MultipartFile photo){
        logger.info("Photo saved {}", id);

        Photo newPhoto = new Photo();

        try {
            newPhoto.setData(photo.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Ошибкааааааааа" + e);
        }
        newPhoto.setId(id);
        System.out.println(id);

        return photoRepo.save(newPhoto);
    }

}
