package ru.kaInc.shelterbot.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.kaInc.shelterbot.model.Report;
import ru.kaInc.shelterbot.repo.ReportRepo;
import ru.kaInc.shelterbot.service.ReportService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReportServiceImpl implements ReportService {
    private final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

    private final ReportRepo reportRepo;

    public ReportServiceImpl(ReportRepo reportRepo) {
        this.reportRepo = reportRepo;
    }

    @Override
    public Report createReport(Report report) {
        logger.debug("Creating {} {}", report);
        Report newReport = new Report();

        newReport.setId(report.getId());
        newReport.setDiet(report.getDiet());
        newReport.setHealth(report.getHealth());
        newReport.setBehavior(report.getBehavior());
        newReport.setReportValid(report.isReportValid());
        newReport.setPhoto(report.getPhoto());
        newReport.setUser(report.getUser());

        return reportRepo.save(newReport);
    }

    @Override
    public List<Report> getAll() {
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
        return reportRepo.getReportsByUserId(userId);
    }

    @Override
    public void deleteReportById(Long id) {
        reportRepo.deleteById(id);
    }
}
