package ru.kaInc.shelterbot.service;

import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Service;
import ru.kaInc.shelterbot.model.Photo;
import ru.kaInc.shelterbot.model.Report;

import java.util.List;
import java.util.Optional;

@Service
public interface ReportService {
    Report createReport(Report report);

    void saveReportWithPhoto(Report report, Photo photo);

    List<Report> getAll();

    Report getReportById(Long id);

    List<Report> findReportsByUsername(String username);

    List<Report> findValidReportsByUsername(String username);

    Optional<Report> updatedReport(Report report);

    Report checkReport(Long id, Boolean isValid);

    List<Report> getReportsByUserId(Long userId);

    void deleteReportById(Long id);

    String sendDiet(Update update, Report report);

    String sendBehavior(Update update, Report report);

    String sendDHealth(Update update, Report report);
}
