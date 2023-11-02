package ru.kaInc.shelterbot.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.kaInc.shelterbot.exception.ImageSizeExceededException;
import ru.kaInc.shelterbot.model.Photo;

import java.io.IOException;
import java.util.Optional;

@Service
public interface PhotoService {
    Optional<Photo> findPhotoById(Long id);

    Long upLoadPhoto(Long id, MultipartFile photo) throws IOException, ImageSizeExceededException;

//    Photo savePhoto(Photo photo);

    void deletePhoto(Long photoId);


    Photo refactorPhoto(Long id, MultipartFile photo);

}
