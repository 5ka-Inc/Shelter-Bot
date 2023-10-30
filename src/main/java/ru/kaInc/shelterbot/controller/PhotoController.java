package ru.kaInc.shelterbot.controller;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.kaInc.shelterbot.model.Photo;
import ru.kaInc.shelterbot.service.PhotoService;

import java.io.IOException;
import java.util.Optional;

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
    public ResponseEntity<byte[]> findPhotoById(@Parameter(description = "Идентификатор фото")
                                                @PathVariable("id") Long id) {
        Optional<Photo> foundPhoto = photoService.findPhotoById(id);

        if (foundPhoto.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Photo photo = foundPhoto.get();
        HttpHeaders headers = new HttpHeaders();

        // Устанавливаем заголовок Content-Type для изображения
        headers.setContentType(MediaType.IMAGE_JPEG); // Предполагая, что формат изображения JPEG

        // Возвращаем изображение в байтах и устанавливаем заголовки
        return new ResponseEntity<>(photo.getData(), headers, HttpStatus.OK);
    }

    @Operation(summary = "Сохранить фото")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Фото успешно изменено"),
            @ApiResponse(responseCode = "404", description = "Фото не найдено")})
    @PutMapping(value = "refactor-photo/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadPhoto(@Parameter(description = "Идентификатор фото")
                                            @PathVariable("id") Long id, @RequestParam MultipartFile multipartFile) throws IOException {
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
        Optional<Photo> foundPhoto = photoService.findPhotoById(id);
        if (foundPhoto.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        photoService.deletePhoto(id);
        return ResponseEntity.ok().build();
    }

}
