package ru.kaInc.shelterbot.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.kaInc.shelterbot.model.User;
import ru.kaInc.shelterbot.model.enums.Role;
import ru.kaInc.shelterbot.repo.UserRepo;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserServiceImpl userService;

    public UserServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddNewUser() {
        User user = new User();
        user.setId(1L);
        when(userRepo.save(any(User.class))).thenReturn(user);

        User createdUser = userService.addNewUser(1L, 1L, "Test User");

        assertNotNull(createdUser);
        assertEquals(1L, createdUser.getId());
    }

    @Test
    public void testIsUserPresent() {
        when(userRepo.existsById(anyLong())).thenReturn(true);

        boolean exists = userService.isUserPresent(1L);

        assertTrue(exists);
    }

    @Test
    public void testFindById_userExists() {
        User user = new User();
        user.setId(1L);
        when(userRepo.findById(anyLong())).thenReturn(Optional.of(user));

        User foundUser = userService.findById(1L);

        assertNotNull(foundUser);
        assertEquals(1L, foundUser.getId());
    }

    @Test
    public void testFindById_userNotExists() {
        when(userRepo.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.findById(1L));
    }

    @Test
    public void testUpdateUser() {
        User oldUser = new User();
        oldUser.setId(1L);
        oldUser.setName("Old Name");
        when(userRepo.findById(anyLong())).thenReturn(Optional.of(oldUser));

        User newUser = new User();
        newUser.setId(1L);
        newUser.setName("New Name");
        when(userRepo.save(any(User.class))).thenReturn(newUser);

        User updatedUser = userService.updateUser(newUser);

        assertNotNull(updatedUser);
        assertEquals("New Name", updatedUser.getName());
    }

    @Test
    public void testFindUsersByRole_usersFound() {
        List<User> users = Arrays.asList(new User(), new User());
        when(userRepo.findUsersByRole(any(Role.class))).thenReturn(users);

        List<User> foundUsers = userService.findUsersByRole(Role.USER);

        assertNotNull(foundUsers);
        assertEquals(2, foundUsers.size());
    }

    @Test
    public void testFindUsersByRole_noUsersFound() {
        when(userRepo.findUsersByRole(any(Role.class))).thenReturn(Arrays.asList());

        assertThrows(EntityNotFoundException.class, () -> userService.findUsersByRole(Role.USER));
    }

    @Test
    public void testFindAll() {
        List<User> users = Arrays.asList(new User(), new User());
        when(userRepo.findAll()).thenReturn(users);

        List<User> foundUsers = userService.findAll();

        assertNotNull(foundUsers);
        assertEquals(2, foundUsers.size());
    }

    @Test
    public void testDeleteUser_userExists() {
        User user = new User();
        user.setId(1L);
        when(userRepo.findById(anyLong())).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(userRepo, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteUser_userNotExists() {
        when(userRepo.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(1L));
    }
}


