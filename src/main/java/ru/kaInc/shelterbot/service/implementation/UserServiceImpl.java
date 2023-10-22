package ru.kaInc.shelterbot.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.kaInc.shelterbot.model.User;
import ru.kaInc.shelterbot.model.enums.Role;
import ru.kaInc.shelterbot.repo.UserRepo;
import ru.kaInc.shelterbot.service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    UserRepo userRepo;

    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public User addNewUser(Long id, Long chatId, String name) {
        logger.debug("Creating {} {}", name, id);

        if (id == null || chatId == null) {
            logger.error("Id and chat id must not be null! Aborting.");
            return null;
        }

        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setChatId(chatId);
        user.setIsAdopter(false);
        user.setRole(Role.USER);

        return userRepo.save(user);
    }

    @Override
    public User addNewUser(com.pengrad.telegrambot.model.User user, Long chatId) {
        logger.debug("Creating {} {}", user.username(), user.id());

        if (chatId == null || user.id() == null) {
            logger.error("Id and chat id must not be null! Aborting.");
            return null;
        }

        User newUser = new User();

        newUser.setId(user.id());
        newUser.setName(user.username());
        newUser.setChatId(chatId);
        newUser.setIsAdopter(false);
        newUser.setRole(Role.USER);

        return userRepo.save(newUser);
    }

    @Override
    public boolean isUserPresent(Long id) {
        return userRepo.existsById(id);
    }

    @Override
    public User findById(Long id) {
        return userRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id %s not found", id)));
    }

    @Override
    public List<User> findUsersByRole(String role) {

        List<User> foundUsers = userRepo.findUsersByRole(role);

        if (foundUsers.isEmpty()) {
            throw new EntityNotFoundException(String.format("Not found users with role %s", role));
        }
        return foundUsers;
    }

    @Override
    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Override
    public User updateUser(User user) {

        User foundUser = findById(user.getId());

        foundUser.setName(user.getName());
        foundUser.setPhone(user.getPhone());
        foundUser.setRole(user.getRole());

        return userRepo.save(foundUser);
    }

    @Override
    public void deleteUser(Long id) {
        if (userRepo.findById(id).isEmpty()) {
            throw new EntityNotFoundException(String.format("User with id %s not found", id));
        }
        userRepo.deleteById(id);
    }
}
