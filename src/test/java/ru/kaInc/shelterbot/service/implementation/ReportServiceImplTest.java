package ru.kaInc.shelterbot.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.kaInc.shelterbot.model.Photo;
import ru.kaInc.shelterbot.model.Report;
import ru.kaInc.shelterbot.model.User;
import ru.kaInc.shelterbot.repo.ReportRepo;
import ru.kaInc.shelterbot.repo.UserRepo;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReportServiceImplTest {

    @Mock
    private ReportRepo reportRepo;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private ReportServiceImpl reportService;

    private Report report;
    private User user;
    private Long reportId;
    private Long userId;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userId = 1L;
        user = new User();
        user.setId(userId);

        reportId = 1L;
        report = new Report();
        report.setId(reportId);
        report.setUser(user);
        report.setPhoto(new Photo());
        report.setDate(new Timestamp(System.currentTimeMillis() + 100000)); // Future timestamp for testing

        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
    }

    @Test
    public void createReport_Success() {
        when(reportRepo.save(any(Report.class))).thenReturn(report);

        Report createdReport = reportService.createReport(report);

        assertNotNull(createdReport);
        verify(reportRepo).save(report);
    }

    @Test
    public void createReport_ReportNull_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> reportService.createReport(null));
        assertEquals("Report was null", exception.getMessage());
    }

    @Test
    public void getAll_ReportsFound() {
        when(reportRepo.findAll()).thenReturn(List.of(report));

        List<Report> reports = reportService.getAll();

        assertFalse(reports.isEmpty());
        assertEquals(1, reports.size());
        verify(reportRepo).findAll();
    }

    @Test
    public void getAll_NoReportsFound_ThrowsEntityNotFoundException() {
        when(reportRepo.findAll()).thenReturn(List.of());

        assertThrows(EntityNotFoundException.class, () -> reportService.getAll());
    }

    @Test
    public void getReportById_ReportFound() {
        when(reportRepo.findById(reportId)).thenReturn(Optional.of(report));

        Report foundReport = reportService.getReportById(reportId);

        assertNotNull(foundReport);
        assertEquals(reportId, foundReport.getId());
    }

    @Test
    public void getReportById_ReportNotFound_ThrowsEntityNotFoundException() {
        when(reportRepo.findById(reportId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> reportService.getReportById(reportId));
    }

    @Test
    public void updatedReport_Success() {
        when(reportRepo.findById(reportId)).thenReturn(Optional.of(report));
        when(reportRepo.save(any(Report.class))).thenReturn(report);

        Optional<Report> updatedReport = reportService.updatedReport(report);

        assertTrue(updatedReport.isPresent());
        verify(reportRepo).save(report);
    }

    @Test
    public void updatedReport_ReportNotFound_ThrowsEntityNotFoundException() {
        when(reportRepo.findById(reportId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> reportService.updatedReport(report));
    }

    @Test
    public void getReportsByUserId_ReportsFound() {
        when(reportRepo.getReportsByUserId(userId)).thenReturn(List.of(report));

        List<Report> reports = reportService.getReportsByUserId(userId);

        assertFalse(reports.isEmpty());
        assertEquals(1, reports.size());
    }

    @Test
    public void getReportsByUserId_NoReportsFound_ThrowsEntityNotFoundException() {
        when(reportRepo.getReportsByUserId(userId)).thenReturn(List.of());

        assertThrows(EntityNotFoundException.class, () -> reportService.getReportsByUserId(userId));
    }

    @Test
    public void deleteReportById_ReportExists() {
        when(reportRepo.findById(reportId)).thenReturn(Optional.of(report));
        doNothing().when(reportRepo).deleteById(reportId);

        assertDoesNotThrow(() -> reportService.deleteReportById(reportId));
        verify(reportRepo).deleteById(reportId);
    }

    @Test
    public void deleteReportById_ReportNotFound_ThrowsEntityNotFoundException() {
        when(reportRepo.findById(reportId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> reportService.deleteReportById(reportId));
    }
}