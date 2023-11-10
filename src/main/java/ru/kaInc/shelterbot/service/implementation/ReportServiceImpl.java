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
@Transactional
public class ReportServiceImpl implements ReportService {
    private final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

    private final ReportRepo reportRepo;
    private final UserRepo userRepo;

    private Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    public ReportServiceImpl(ReportRepo reportRepo, UserRepo userRepo) {
        this.reportRepo = reportRepo;
        this.userRepo = userRepo;
    }

    @Override
    public Report createReport(Report report) {
        logger.debug("Creating {} {}", report);
        if(report == null) {
            throw new IllegalArgumentException("Report was null");
        }

//        if (userRepo.findById(report.getUser().getId()).isEmpty()) {
//            throw new EntityNotFoundException(String.format("User with id %s not found", report.getUser().getId()));
//        }

//        if (report.getPhoto() == null) {
//            throw new EntityNotFoundException("Photo not found");
//        }
//
//        if (timestamp.compareTo(report.getDate()) > 0) {
//            throw new IllegalArgumentException("Еhe specified time has not arrived");
//        }

        return reportRepo.save(report);
    }

    @Override
    public List<Report> getAll() {
        if (reportRepo.findAll().isEmpty()) {
            throw new EntityNotFoundException("Report not found");
        }
        return reportRepo.findAll();
    }

    @Override
    public Report getReportById(Long id) {
        return reportRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Report with id %s not found", id)));
    }

    @Override
    public Optional<Report> updatedReport(Report report) {
        Optional<Report> newReport = reportRepo.findById(report.getId());

        if(newReport.isEmpty()){
            throw new EntityNotFoundException(String.format("Report with id %s not found", report.getId()));
        }
        if (userRepo.findById(report.getUser().getId()).isEmpty()) {
            throw new EntityNotFoundException(String.format("User with id %s not found", report.getUser().getId()));
        }

        if (report.getPhoto() == null) {
            throw new EntityNotFoundException("Photo not found");
        }

        if (timestamp.compareTo(report.getDate()) > 0) {
            throw new IllegalArgumentException("Еhe specified time has not arrived");
        }


        newReport.get().setId(report.getId());
        newReport.get().setDate(report.getDate());
        newReport.get().setDiet(report.getDiet());
        newReport.get().setHealth(report.getHealth());
        newReport.get().setPhoto(report.getPhoto());
        newReport.get().setBehavior(report.getBehavior());
        newReport.get().setUser(report.getUser());

        return newReport;
    }

    @Override
    public List<Report> getReportsByUserId(Long userId) {
        List<Report> foundReports = reportRepo.getReportsByUserId(userId);
        if (foundReports.isEmpty()) {
            throw new EntityNotFoundException(String.format("Not found Reports by %s user id", userId));
        }
        return reportRepo.getReportsByUserId(userId);
    }

    @Override
    public void deleteReportById(Long id) {
        if(reportRepo.findById(id).isEmpty()) {
            throw new EntityNotFoundException(String.format("Report with id %s not found", id));
        }
        reportRepo.deleteById(id);
    }
}
