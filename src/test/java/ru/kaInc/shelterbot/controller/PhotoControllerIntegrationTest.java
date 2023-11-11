package ru.kaInc.shelterbot.controller;

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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import ru.kaInc.shelterbot.model.Photo;
import ru.kaInc.shelterbot.service.PhotoService;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
public class PhotoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PhotoService photoService;

    private Photo photo;

    @BeforeEach
    public void setup() {
        photo = new Photo();
        photo.setId(1L);
        photo.setData(new byte[1024]);

        given(photoService.findPhotoById(anyLong())).willReturn(photo);
    }

    @Test
    public void findPhotoById_ShouldReturnPhoto() throws Exception {
        mockMvc.perform(get("/photo/id/{id}", photo.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                .andExpect(content().bytes(photo.getData()));
    }

    @Test
    public void findPhotoById_ShouldReturnNotFoundWhenPhotoDoesNotExist() throws Exception {
        given(photoService.findPhotoById(anyLong())).willReturn(null);

        mockMvc.perform(get("/photo/id/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void uploadPhoto_ShouldReturnOk() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "multipartFile", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "test image content".getBytes());

        mockMvc.perform(multipart("/photo/refactor-photo/{id}", photo.getId())
                        .file(mockMultipartFile))
                .andExpect(status().isOk());
    }

    @Test
    public void deletePhotoById_ShouldReturnOk() throws Exception {
        mockMvc.perform(delete("/photo/delete/{id}", photo.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void deletePhotoById_ShouldReturnNotFoundWhenPhotoDoesNotExist() throws Exception {
        Mockito.doThrow(new EntityNotFoundException()).when(photoService).deletePhoto(anyLong());

        mockMvc.perform(delete("/photo/delete/{id}", 999L))
                .andExpect(status().isNotFound());
    }
}
