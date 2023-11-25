package ru.kaInc.shelterbot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.kaInc.shelterbot.model.Report;
import ru.kaInc.shelterbot.service.ReportService;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
public class ReportControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;

    private Report report;

    @BeforeEach
    public void setup() {
        report = new Report();
        report.setId(1L);

        given(reportService.getAll()).willReturn(Collections.singletonList(report));
        given(reportService.getReportById(anyLong())).willReturn(report);
        given(reportService.getReportsByUserId(anyLong())).willReturn(Collections.singletonList(report));
        given(reportService.updatedReport(any(Report.class))).willReturn(Optional.of(report));
    }

    @Test
    public void getAll_ShouldReturnAllReports() throws Exception {
        mockMvc.perform(get("/report/get-all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void findReportById_ShouldReturnReport() throws Exception {
        mockMvc.perform(get("/report/id/{id}", report.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(report.getId().intValue())));
    }

    @Test
    public void updateReport_ShouldUpdateReport() throws Exception {
        mockMvc.perform(put("/report")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(report)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(report.getId().intValue())));
    }

    @Test
    public void findReportByUserId_ShouldReturnReports() throws Exception {
        mockMvc.perform(get("/report/user-id/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void deleteReportById_ShouldDeleteReport() throws Exception {
        mockMvc.perform(delete("/report/delete/{id}", report.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void getAll_ShouldReturnNotFoundWhenNoReports() throws Exception {
        given(reportService.getAll()).willReturn(Collections.emptyList());

        mockMvc.perform(get("/report/get-all"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findReportById_ShouldReturnNotFoundWhenReportDoesNotExist() throws Exception {
        given(reportService.getReportById(anyLong())).willReturn(null);

        mockMvc.perform(get("/report/id/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateReport_ShouldReturnNotFoundWhenReportDoesNotExist() throws Exception {
        given(reportService.updatedReport(any(Report.class))).willReturn(Optional.empty());

        mockMvc.perform(put("/report")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(new Report())))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findReportByUserId_ShouldReturnNotFoundWhenNoReportsForUser() throws Exception {
        given(reportService.getReportsByUserId(anyLong())).willReturn(Collections.emptyList());

        mockMvc.perform(get("/report/user-id/{id}", 9999L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteReportById_ShouldReturnNotFoundWhenReportDoesNotExist() throws Exception {
        Mockito.doThrow(new EntityNotFoundException()).when(reportService).deleteReportById(anyLong());

        mockMvc.perform(delete("/report/delete/{id}", 9999L))
                .andExpect(status().isNotFound());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
