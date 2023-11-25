package ru.kaInc.shelterbot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.kaInc.shelterbot.exception.ImageSizeExceededException;
import ru.kaInc.shelterbot.model.Photo;
import ru.kaInc.shelterbot.service.PhotoService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequiredArgsConstructor
@RequestMapping("/photo")
@Tag(name = "фото", description = "Операция с фото")
public class PhotoController {

    private final PhotoService photoService;

    @Operation(summary = "Получить фото")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Фото найдено"),
            @ApiResponse(responseCode = "404", description = "Фото не найдено")})
    @GetMapping("id/{id}")
    public void findPhotoById(@Parameter(description = "Идентификатор фото")
                              @PathVariable("id") Long id, HttpServletResponse response) throws IOException {

        Photo foundPhoto;

        try {
            foundPhoto = photoService.findPhotoById(id);
        } catch (EntityNotFoundException e) {
            response.sendError(404);
            return;
        }
        Path path = Path.of(foundPhoto.getFilePath());

        try (
                InputStream is = Files.newInputStream(path);
                OutputStream os = response.getOutputStream();
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {

            response.setStatus(200);
            response.setContentType(foundPhoto.getMediaType());
            response.setContentLength(foundPhoto.getFileSize().intValue());
            bis.transferTo(bos);
        }


        // Возвращаем изображение в байтах и устанавливаем заголовки

    }
    @Operation(summary = "Сохранить фото")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Фото успешно изменено"),
            @ApiResponse(responseCode = "404", description = "Фото не найдено")})
    @PostMapping(value = "refactor-photo/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadPhoto(@Parameter(description = "Идентификатор фото")
                                            @PathVariable("id") Long id, @RequestParam MultipartFile multipartFile) throws IOException, ImageSizeExceededException {
        if (multipartFile == null) {
            return ResponseEntity.notFound().build();
        }
        photoService.upLoadPhoto(id, multipartFile);
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "Удалить фото")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Фото удалено",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = Photo.class)))),
            @ApiResponse(responseCode = "404", description = "Фото не найдено")})
    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deletePhotoById(@Parameter(description = "Идентификатор фото")
                                                @PathVariable("id") Long id) {
        Photo foundPhoto = photoService.findPhotoById(id);
        if (foundPhoto == null) {
            return ResponseEntity.notFound().build();
        }
        photoService.deletePhoto(id);
        return ResponseEntity.ok().build();
    }
}
