package ru.kaInc.shelterbot.service;

import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.kaInc.shelterbot.exception.ImageSizeExceededException;
import ru.kaInc.shelterbot.model.Photo;
import ru.kaInc.shelterbot.model.Report;

import java.io.IOException;

@Service
public interface PhotoService {
    Photo findPhotoById(Long id);

    Long upLoadPhoto(Long id, MultipartFile photo) throws IOException, ImageSizeExceededException;


    void deletePhoto(Long photoId);


    Photo refactorPhoto(Long id, MultipartFile photo);

    Photo savePhoto(Update update, Report report);
}
