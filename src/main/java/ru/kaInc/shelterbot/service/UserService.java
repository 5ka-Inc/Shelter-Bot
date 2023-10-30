package ru.kaInc.shelterbot.service;

import ru.kaInc.shelterbot.model.User;
import ru.kaInc.shelterbot.model.enums.Role;

import java.util.List;


public interface UserService {
    /**
     * Creates new user in DB via user repository. ID and chatId must not be null.
     *
     * @param id     telegtram userId will be used as id in bot's DB. Must not be null.
     * @param chatId id of chat with this user. Must not be null.
     * @param name   by default it is telegram username. Later volunteer can change this value to real name, if user going to take a pet.
     * @return created user or null if id or chatId is null
     * @see com.pengrad.telegrambot.model.User
     */
    User addNewUser(Long id, Long chatId, String name);

    /**
     * Creates new user in DB via user repository. ChatId must not be null.
     *
     * @param user   telegram user object. Method will extract its "id" and "username" to use as fields of new user object.
     * @param chatId id of chat with this user. Must not be null.
     * @return created user or null if chatId is null
     * @see com.pengrad.telegrambot.model.User
     */
    User addNewUser(com.pengrad.telegrambot.model.User user, Long chatId);

    /**
     * Returns true if user with such id is already exist in DB
     * @param id telegram user id
     * @return true if user is found in DB
     */
    boolean isUserPresent(Long id);

    User findById(Long id);

    List<User> findUsersByRole(Role role);

    List<User> findAll();

    User updateUser(User user);

    void deleteUser(Long id);
}
