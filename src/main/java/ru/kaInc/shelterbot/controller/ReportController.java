package ru.kaInc.shelterbot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kaInc.shelterbot.model.Report;
import ru.kaInc.shelterbot.service.ReportService;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
@Tag(name = "Отчеты", description = "Операция с отчетами")
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = "Получить все отчеты")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отчеты найдены",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = Report.class)))),
            @ApiResponse(responseCode = "404", description = "Отчеты не найдены")})
    @GetMapping
    public ResponseEntity<List<Report>> getAll() {
        List<Report> reports = reportService.getAll();
        if (reports.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reports);
    }

    @Operation(summary = "Получить отчет по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отчет найден",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Report.class))}),
            @ApiResponse(responseCode = "404", description = "Отчет не найден")})
    @GetMapping("/{id}")
    public ResponseEntity<Report> findReportById(@Parameter(description = "Идентификатор отчета")
                                                 @PathVariable("id") Long id) {
        Report report = reportService.getReportById(id);
        if (report == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(report);
    }

    @Operation(summary = "Изменить отчет")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отчет изменен",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Report.class))}),
            @ApiResponse(responseCode = "404", description = "Отчет не найден")})
    @PutMapping
    public ResponseEntity<Optional<Report>> updateReport(@RequestBody Report report) {
        Report foundReport = reportService.getReportById(report.getId());
        if (foundReport == null) {
            return ResponseEntity.notFound().build();
        }
        Optional<Report> updatedReport = reportService.updatedReport(foundReport);
        return ResponseEntity.ok(updatedReport);
    }


    @Operation(summary = "Получить отчеты по идентификатору пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отчет найден",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Report.class))}),
            @ApiResponse(responseCode = "404", description = "Отчет не найден")})
    @GetMapping("user/{id}")

    public ResponseEntity<List<Report>> findReportByUserId(@Parameter(description = "Идентификатор пользователя")
                                                           @PathVariable("id") Long id) {
        List<Report> report = reportService.getReportsByUserId(id);
        if (report.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(report);
    }

    @Operation(summary = "Получить все отчеты пользователя по username в telegram")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отчеты найдены",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = Report.class)))),
            @ApiResponse(responseCode = "404", description = "Отчеты не найдены")})
    @GetMapping("/username")
    public ResponseEntity<?> findReportByUsername(@Parameter(description = "username пользователя в telegram")
                                                  @RequestParam("username") String username
    ) {
        List<Report> foundReports = reportService.findReportsByUsername(username);
        if (foundReports.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(foundReports);
    }

    @Operation(summary = "Получить проверенные и одобренные отчеты по username пользователя в telegram")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отчеты найдены",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = Report.class)))),
            @ApiResponse(responseCode = "404", description = "Отчеты не найдены")})
    @GetMapping("/username/valid")
    public ResponseEntity<?> findValidReportsByUsername(@Parameter(description = "username пользователя в telegram")
                                                        @RequestParam("username") String username
    ) {
        List<Report> foundReports = reportService.findValidReportsByUsername(username);
        if (foundReports.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(foundReports);
    }

    @Operation(summary = "Поставить отметку на отчете")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отчет изменен",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Report.class))}),
            @ApiResponse(responseCode = "404", description = "Отчет не найден")})
    @PatchMapping("/validate/{id}")
    public ResponseEntity<?> checkReport(@PathVariable("id") Long id,
                                         @Parameter(description = "true - отчет соответствует требованиям, false - не соответствует")
                                         @RequestParam Boolean isValid
    ) {
        Report checkedReport = reportService.checkReport(id, isValid);
        if (checkedReport == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(checkedReport);
    }

    @Operation(summary = "Удалить отчет")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отчет удален",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = Report.class)))),
            @ApiResponse(responseCode = "404", description = "Отчет не найдены")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReportById(@Parameter(description = "Идентификатор отчета")
                                                 @PathVariable Long id) {
        Report foundReport = reportService.getReportById(id);
        if (foundReport == null) {
            return ResponseEntity.notFound().build();
        }
        reportService.deleteReportById(id);
        return ResponseEntity.ok().build();
    }
}
