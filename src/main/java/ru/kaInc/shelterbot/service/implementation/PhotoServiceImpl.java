package ru.kaInc.shelterbot.service.implementation;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import ru.kaInc.shelterbot.exception.ImageSizeExceededException;
import ru.kaInc.shelterbot.model.Photo;
import ru.kaInc.shelterbot.repo.PhotoRepo;
import ru.kaInc.shelterbot.service.PhotoService;
import ru.kaInc.shelterbot.service.ReportService;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@Transactional
public class PhotoServiceImpl implements PhotoService {
    private final Logger logger = LoggerFactory.getLogger(PhotoServiceImpl.class);

    private long MAX_SIZE = 1024 * 600;
    private final PhotoRepo photoRepo;
    private final ReportService reportService;

    @Value("${photos.dir.path}")
    private String photoDir;
    private final Path photosDir = Paths.get("./photos");


    public PhotoServiceImpl(PhotoRepo photoRepo, ReportService reportService) {
        this.photoRepo = photoRepo;
        this.reportService = reportService;
    }

    @Override
    public Photo findPhotoById(Long id) {
        Photo foundPhoto = photoRepo.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Photo with id %s not found", id)));
        return foundPhoto;
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
        newPhoto.setMediaType(multipartFile.getContentType());
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
    public Photo refactorPhoto(Long id, MultipartFile photo) {
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

    @Override
    public Photo savePhoto(Long chatId, PhotoSize[] photoSizes, TelegramBot telegramBot) {
        logger.info("Обработка отчета для chatId: {}", chatId);

        PhotoSize photoSize = photoSizes[photoSizes.length - 1];
        GetFile getFileRequest = new GetFile(photoSize.fileId());
        GetFileResponse getFileResponse = telegramBot.execute(getFileRequest);
        Photo photo = new Photo();

        if (getFileResponse.isOk()) {
            File file = getFileResponse.file();
            String fullFilePath = telegramBot.getFullFilePath(file);

            try (InputStream fileStream = new URL(fullFilePath).openStream()) {
                String fileName = file.fileId() + ".jpg";
                Path pathToSave = photosDir.resolve(fileName);
                Files.createDirectories(photosDir);
                Files.copy(fileStream, pathToSave, StandardCopyOption.REPLACE_EXISTING);
                logger.info("Фотография сохранена по пути: {}", pathToSave);

                photo.setFilePath(pathToSave.toString());
                photo.setFileSize((long) photoSize.fileSize());
                photo.setMediaType("image/jpeg"); // Или определяем MIME-тип динамически

                telegramBot.execute(new SendMessage(chatId, "Отчет успешно отправлен!"));
                logger.info("Подтверждение отправлено пользователю с chatId: {}", chatId);
            } catch (IOException e) {
                e.printStackTrace();
                telegramBot.execute(new SendMessage(chatId, "Произошла ошибка при сохранении фотографии."));
                logger.error("Ошибка при сохранении фотографии для chatId: {}", chatId, e);
            }
        } else {
            telegramBot.execute(new SendMessage(chatId, "Не удалось получить информацию о файле."));
            logger.error("Не удалось получить информацию о файле для chatId: {}", chatId);
        }
        return photo;
    }
}
