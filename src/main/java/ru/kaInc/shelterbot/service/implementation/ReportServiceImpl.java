package ru.kaInc.shelterbot.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.kaInc.shelterbot.model.Report;
import ru.kaInc.shelterbot.repo.ReportRepo;
import ru.kaInc.shelterbot.repo.UserRepo;
import ru.kaInc.shelterbot.service.ReportService;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class ReportServiceImpl implements ReportService {
    private final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

    private final ReportRepo reportRepo;
    private final UserRepo userRepo;

    public ReportServiceImpl(ReportRepo reportRepo, UserRepo userRepo) {
        this.reportRepo = reportRepo;
        this.userRepo = userRepo;
    }

    @Override
    @Transactional
    public Report createReport(Report report) {
        logger.debug("Creating report: {}", report);
        validateReport(report);

        return reportRepo.save(report);
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

        if (userRepo.findById(report.getUser().getId()).isEmpty()) {
            throw new EntityNotFoundException(String.format("User with id %s not found", report.getUser().getId()));
        }

        if (report.getPhoto() == null) {
            throw new IllegalArgumentException("Photo not found");
        }

        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        if (currentTimestamp.compareTo(report.getDate()) > 0) {
            throw new IllegalArgumentException("The specified time has not arrived");
        }
    }
}
