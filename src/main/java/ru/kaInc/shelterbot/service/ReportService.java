package ru.kaInc.shelterbot.service;

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
}
