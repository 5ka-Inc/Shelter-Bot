package ru.kaInc.shelterbot.service.implementation;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.response.GetFileResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kaInc.shelterbot.model.Photo;
import ru.kaInc.shelterbot.model.Report;
import ru.kaInc.shelterbot.repo.PhotoRepo;
import ru.kaInc.shelterbot.service.ReportService;
import ru.kaInc.shelterbot.service.SaveReportsService;
import ru.kaInc.shelterbot.service.UserService;

import java.io.*;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SaveReportsServiceImpl implements SaveReportsService {

    @Autowired
    private final ReportService reportService;
    @Autowired
    private final UserService userService;
    @Autowired
    private final PhotoServiceImpl photoService;
    private final PhotoRepo photoRepo;


    private List<String> messages = new ArrayList<>();
    private Long increment = 1L;
    private Long photoId = 1L;
    private Long userId;

    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    LocalDateTime localDateTime = timestamp.toLocalDateTime();
    LocalDate localDate = localDateTime.toLocalDate();
    int day1 = localDate.getDayOfMonth();

    public SaveReportsServiceImpl(ReportService reportService, UserService userService, PhotoServiceImpl photoService, PhotoRepo photoRepo) {
        this.reportService = reportService;
        this.userService = userService;
        this.photoService = photoService;
        this.photoRepo = photoRepo;
    }

    public Report createReport(List<String> messages, Photo photo) {

        List<Report> reports = reportService.getReportsByUserId(userId);
        Report report1 = new Report();

        reports.forEach(report -> {

            LocalDateTime localDate = report.getDate().toLocalDateTime();
            int day = localDate.getDayOfMonth();
            if (day == day1) {
                throw new RuntimeException("Сегодня уже был отправлен отчет!");
            }
        });
        if (messages.size() >= 3) {
            messages.get(0);

            report1.setId(increment);
            report1.setUser(userService.findById(userId));
            report1.setDiet(messages.get(0));

            if (messages.size() > 1) {
                report1.setHealth(messages.get(1));
            }
            if (messages.size() > 2) {
                report1.setBehavior(messages.get(2));
            }

            report1.setDate(timestamp);
            report1.setPhoto(photo);

            reportService.createReport(report1);
            increment++;

            messages.subList(0, 3).clear();


        }
        return report1;

    }

    @Override
    public void saveReports(List<Update> updates, TelegramBot bot) throws IOException {
        if (updates.isEmpty()) {
            throw new RuntimeException("Пусто");
        }
        for (Update update : updates) {
            userId = update.message().from().id();
            if (userService.findById(userId) == null) {
                throw new EntityNotFoundException("Not Found");
            }
            if (update.message().text() instanceof String) {
                messages.add(update.message().text());
            }
            System.out.println("update = " + update);

        }
        System.out.println(messages);
        Photo photo = savePhoto(updates, bot);
        createReport(messages, photo);
    }


    @Override
    public Photo savePhoto(List<Update> updates, TelegramBot bot) throws IOException {
        Photo photo = new Photo();

        for (Update update : updates) {
            if (update.message() != null && update.message().photo() != null) {
                // Получаем информацию о самой большой фотографии
                PhotoSize photoSizes = update.message().photo()[0];

                // Получаем fileId фотографии
                String fileId = photoSizes.fileId();

                GetFile getFile = new GetFile(fileId);
                GetFileResponse getFileResponse = bot.execute(getFile);
                File file = getFileResponse.file();

                // Путь файла
                String filePath = file.filePath();
                Long fileSize = file.fileSize();

                byte[] photoByte = filePath.getBytes();
                System.out.println(filePath);

                String fileUrl = "https://api.telegram.org/file/bot" + bot.getToken() +"/" + filePath;

                photo.setData(photoByte);
                photo.setFileSize(fileSize);
                Optional<Photo> testPhoto = photoService.findPhotoById(photoId);
                if (testPhoto.isPresent()) {
                    photoId++;
                }
                if (photo.equals(testPhoto)) throw new RuntimeException("Ошибка фотки одинаковые, отправте новую фотку");
                photo.setId(photoId);
                photoRepo.save(photo);
                System.out.println("фотка с таким id = " + photoId);
                photoId++;
            }
        }
        return photo;
    }

    private byte[] readFileToByteArray(String filePath) throws IOException {
        URL url = new URL(filePath);
        try (InputStream in = url.openStream();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }

            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace(); // Добавьте эту строку для вывода информации об ошибке в консоль
            throw e; // Повторное бросание исключения для передачи ошибки выше
        }
    }


}
