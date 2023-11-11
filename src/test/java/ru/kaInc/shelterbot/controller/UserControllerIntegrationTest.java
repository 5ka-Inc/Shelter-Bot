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
import ru.kaInc.shelterbot.model.User;
import ru.kaInc.shelterbot.model.enums.Role;
import ru.kaInc.shelterbot.service.UserService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private User user;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setId(1L);
        user.setChatId(1L);
        user.setName("Test");
        user.setRole(Role.VOLUNTEER);

        userService.save(user);

        given(userService.findAll()).willReturn(Collections.singletonList(user));
        given(userService.findById(anyLong())).willReturn(user);
        given(userService.findUsersByRole(any(Role.class))).willReturn(Collections.singletonList(user));
        given(userService.updateUser(any(User.class))).willReturn(user);

    }


    @Test
    public void findAll_ShouldReturnAllUsers() throws Exception {
        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void findUserById_ShouldReturnUser() throws Exception {

        mockMvc.perform(get("/user/id/{id}", user.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(user.getId()));
    }

    @Test
    public void getUsersByRole_ShouldReturnUsers() throws Exception {
        mockMvc.perform(get("/user/role/{role}", Role.VOLUNTEER))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void changeUser_ShouldUpdateUser() throws Exception {
        user.setName("Updated Name");

        mockMvc.perform(put("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    public void deleteUser_ShouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/user/delete/{id}", user.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void findAll_ShouldReturnNotFoundWhenNoUsers() throws Exception {
        given(userService.findAll()).willReturn(Collections.emptyList());

        mockMvc.perform(get("/user"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findUserById_ShouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        given(userService.findById(anyLong())).willReturn(null);

        mockMvc.perform(get("/user/id/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getUsersByRole_ShouldReturnNotFoundWhenNoUsersWithRole() throws Exception {
        given(userService.findUsersByRole(any(Role.class))).willReturn(Collections.emptyList());

        mockMvc.perform(get("/user/role/{role}", Role.USER))
                .andExpect(status().isNotFound());
    }

    @Test
    public void changeUser_ShouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        given(userService.findById(anyLong())).willReturn(null);

        mockMvc.perform(put("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(new User())))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteUser_ShouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        Mockito.doThrow(new EntityNotFoundException()).when(userService).deleteUser(anyLong());

        mockMvc.perform(delete("/user/delete/{id}", 999L))
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