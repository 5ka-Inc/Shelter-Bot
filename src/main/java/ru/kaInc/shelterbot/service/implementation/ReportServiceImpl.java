package ru.kaInc.shelterbot.service.implementation;

import com.pengrad.telegrambot.model.Update;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.kaInc.shelterbot.model.Photo;
import ru.kaInc.shelterbot.model.Report;
import ru.kaInc.shelterbot.repo.PhotoRepo;
import ru.kaInc.shelterbot.repo.ReportRepo;
import ru.kaInc.shelterbot.service.ReportService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReportServiceImpl implements ReportService {
    private final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

    private final ReportRepo reportRepo;
    private final PhotoRepo photoRepo;

    public ReportServiceImpl(ReportRepo reportRepo, PhotoRepo photoRepo) {
        this.reportRepo = reportRepo;
        this.photoRepo = photoRepo;
    }

    @Override
    @Transactional
    public Report createReport(Report report) {
        logger.debug("Creating report: {}", report);
        validateReport(report);

        return reportRepo.save(report);
    }

    @Override
    @Transactional
    public void saveReportWithPhoto(Report report, Photo photo) {
        photo.setReport(report);
        photoRepo.save(photo);
        logger.info("Запись фотографии сохранена с ID: {}", photo.getId());

        report.setPhoto(photo);
        reportRepo.save(report);
        logger.info("Запись отчета сохранена с ID: {}", report.getId());
    }

    @Override
    public List<Report> getAll() {
        List<Report> reports = reportRepo.findAll();
        if (reports.isEmpty()) {
            throw new EntityNotFoundException("No reports found");
        }
        return reports;
    }

    @Override
    public Report getReportById(Long id) {
        return reportRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Report with id %s not found", id)));
    }

    @Override
    public List<Report> findReportsByUsername(String username) {
        List<Report> foundReports = reportRepo.findReportsByUsername(username);
        if (foundReports.isEmpty()) {
            throw new EntityNotFoundException(String.format("There is no reports of %s", username));
        }
        return foundReports;
    }

    @Override
    public List<Report> findValidReportsByUsername(String username) {
        List<Report> foundReports = reportRepo.findValidReportsByUsername(username);
        if (foundReports.isEmpty()) {
            throw new EntityNotFoundException(String.format("There is no reports of %s", username));
        }
        return foundReports;
    }

    @Override
    @Transactional
    public Optional<Report> updatedReport(Report report) {
        validateReport(report);

        Report existingReport = reportRepo.findById(report.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Report with id %s not found", report.getId())));

        existingReport.setId(report.getId());
        existingReport.setDate(report.getDate());
        existingReport.setDiet(report.getDiet());
        existingReport.setHealth(report.getHealth());
        existingReport.setPhoto(report.getPhoto());
        existingReport.setBehavior(report.getBehavior());
        existingReport.setUser(report.getUser());


        return Optional.of(reportRepo.save(existingReport));
    }

    @Override
    public Report checkReport(Long id, Boolean isValid) {
        Report foundReport = reportRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Report with id %s not found", id)));

        foundReport.setReportValid(isValid);
        return foundReport;
    }

    @Override
    public List<Report> getReportsByUserId(Long userId) {
        List<Report> foundReports = reportRepo.getReportsByUserId(userId);
        if (foundReports.isEmpty()) {
            throw new EntityNotFoundException(String.format("No reports found by user id %s", userId));
        }
        return foundReports;
    }

    @Override
    @Transactional
    public void deleteReportById(Long id) {
        if (getReportById(id) == null) {
            throw new EntityNotFoundException(String.format("Report with id %s not found", id));
        }
        reportRepo.deleteById(id);
    }

    private void validateReport(Report report) {
        if (report == null) {
            throw new IllegalArgumentException("Report was null");
        }
        if (report.getPhoto() == null) {
            //throw new IllegalArgumentException("Photo not found");
            System.err.println("PHOTO");
        }
        LocalDateTime localDateTime = LocalDateTime.now();
        if (localDateTime.compareTo(report.getDate()) < 0) {
            throw new IllegalArgumentException("The specified time has not arrived");
        }
    }

    public String sendDiet(Update update, Report report) {
        if (update.message().text() != null) {
            report.setDiet(update.message().text());
            return "Информация сохранена";
        }
        return "Ошибочщка";
    }

    public String sendBehavior(Update update, Report report) {
        if (update.message().text() != null) {
            report.setBehavior(update.message().text());
            return "Информация сохранена";
        }
        return "Ошибочщка";
    }

    public String sendDHealth(Update update, Report report) {
        if (update.message().text() != null) {
            report.setHealth(update.message().text());
            return "Информация сохранена";
        }
        return "Ошибочщка";
    }
}