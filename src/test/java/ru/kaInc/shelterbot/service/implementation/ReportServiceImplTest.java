package ru.kaInc.shelterbot.service.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.kaInc.shelterbot.model.Report;
import ru.kaInc.shelterbot.model.User;
import ru.kaInc.shelterbot.repo.ReportRepo;
import ru.kaInc.shelterbot.repo.UserRepo;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ReportServiceImplTest {

    @Mock
    private ReportRepo reportRepo;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private ReportServiceImpl reportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createReport_validInput_shouldSaveAndReturnReport() {
        Report report = new Report();
        when(userRepo.findById(anyLong())).thenReturn(Optional.of(mock(User.class)));
        when(reportRepo.save(any(Report.class))).thenReturn(report);

        Report result = reportService.createReport(report);

        assertNotNull(result);
        verify(reportRepo, times(1)).save(report);
    }

    @Test
    void getAll_reportsExist_shouldReturnAllReports() {
        List<Report> reports = Arrays.asList(new Report(), new Report());
        when(reportRepo.findAll()).thenReturn(reports);

        List<Report> result = reportService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getReportById_existingId_shouldReturnReport() {
        Report report = new Report();
        report.setId(1L);
        when(reportRepo.findById(anyLong())).thenReturn(Optional.of(report));

        Report result = reportService.getReportById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void updatedReport_validInput_shouldUpdateAndReturnReport() {
        Report report = new Report();
        report.setId(1L);
        when(reportRepo.findById(anyLong())).thenReturn(Optional.of(new Report()));
        when(userRepo.findById(anyLong())).thenReturn(Optional.of(mock(User.class)));
        when(reportRepo.save(any(Report.class))).thenReturn(report);

        Optional<Report> result = reportService.updatedReport(report);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void getReportsByUserId_existingUserId_shouldReturnReports() {
        List<Report> reports = Arrays.asList(new Report(), new Report());
        when(reportRepo.getReportsByUserId(anyLong())).thenReturn(reports);

        List<Report> result = reportService.getReportsByUserId(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void deleteReportById_existingId_shouldDeleteReport() {
        Report report = new Report();
        report.setId(1L);
        when(reportRepo.findById(anyLong())).thenReturn(Optional.of(report));

        reportService.deleteReportById(1L);

        verify(reportRepo, times(1)).deleteById(1L);
    }
    // TODO: дописать тесты для эксепшенов
}
