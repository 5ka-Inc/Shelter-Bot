package ru.kaInc.shelterbot.service;

import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Service;
import ru.kaInc.shelterbot.model.Report;

import java.util.List;
import java.util.Optional;

@Service
public interface ReportService {
    Report createReport(Report report);

    List<Report> getAll();

    Report getReportById(Long id);

    Optional<Report> updatedReport(Report report);

    List<Report> getReportsByUserId(Long userId);

    void deleteReportById(Long id);

    String sendDiet(Update update, Report report);

    String sendBehavior(Update update, Report report);

    String sendDHealth(Update update, Report report);
}
