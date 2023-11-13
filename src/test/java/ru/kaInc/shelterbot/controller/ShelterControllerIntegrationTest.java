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
import ru.kaInc.shelterbot.model.Shelter;
import ru.kaInc.shelterbot.model.enums.Type;
import ru.kaInc.shelterbot.service.ShelterService;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
public class ShelterControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShelterService shelterService;

    private Shelter shelter;
    private Shelter nonExistingShelter;

    @BeforeEach
    public void setup() {
        shelter = new Shelter();
        shelter.setId(1L);
        shelter.setName("Test Shelter");
        shelter.setType(Type.DOG);

        shelterService.createShelter(shelter);

        nonExistingShelter = new Shelter();
        nonExistingShelter.setId(999L);

        given(shelterService.findAll()).willReturn(Collections.singletonList(shelter));
        given(shelterService.findById(shelter.getId())).willReturn(shelter);
        given(shelterService.findByShelterType(shelter.getType())).willReturn(Collections.singletonList(shelter));
        given(shelterService.updateShelter(any(Shelter.class))).willReturn(shelter);

    }

    @Test
    public void findAll_ShouldReturnAllShelters() throws Exception {
        mockMvc.perform(get("/shelter"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(shelter.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(shelter.getName())));
    }

    @Test
    public void findShelterById_ShouldReturnShelter() throws Exception {
        mockMvc.perform(get("/shelter/id/{id}", shelter.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(shelter.getId().intValue())))
                .andExpect(jsonPath("$.name", is(shelter.getName())));
    }

    @Test
    public void findByShelterType_ShouldReturnShelters() throws Exception {
        String typeParam = shelter.getType().toString();

        mockMvc.perform(get("/shelter/type")
                        .param("type", typeParam))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].type", is(typeParam)));
    }


    @Test
    public void changeShelter_ShouldUpdateShelter() throws Exception {
        shelter.setName("Updated Name");

        mockMvc.perform(put("/shelter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(shelter)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(shelter.getName())));
    }

    @Test
    public void deleteShelter_ShouldDeleteShelter() throws Exception {
        mockMvc.perform(delete("/shelter/delete/{id}", shelter.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void changeShelter_ShouldReturnNotFoundWhenShelterDoesNotExist() throws Exception {
        mockMvc.perform(put("/shelter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(nonExistingShelter)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findAll_ShouldReturnNotFoundWhenNoShelters() throws Exception {
        given(shelterService.findAll()).willReturn(Collections.emptyList());

        mockMvc.perform(get("/shelter"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findShelterById_ShouldReturnNotFoundWhenShelterDoesNotExist() throws Exception {
        given(shelterService.findById(nonExistingShelter.getId())).willReturn(null);

        mockMvc.perform(get("/shelter/id/{id}", nonExistingShelter.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findByShelterType_ShouldReturnNotFoundWhenNoSheltersOfThatType() throws Exception {
        given(shelterService.findByShelterType(Type.CAT)).willReturn(Collections.emptyList());

        mockMvc.perform(get("/shelter/type")
                        .param("type", Type.CAT.toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteShelter_ShouldReturnNotFoundWhenShelterDoesNotExist() throws Exception {
        Mockito.doThrow(new EntityNotFoundException()).when(shelterService).deleteShelter(nonExistingShelter.getId());

        mockMvc.perform(delete("/shelter/delete/{id}", nonExistingShelter.getId()))
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
