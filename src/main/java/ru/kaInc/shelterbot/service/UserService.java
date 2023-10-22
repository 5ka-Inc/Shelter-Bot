package ru.kaInc.shelterbot.service;

import ru.kaInc.shelterbot.model.User;


public interface UserService {

    User addNewUser(Long id, long chatId, String name);
    User addNewUser(com.pengrad.telegrambot.model.User user, Long chatId);

    boolean isUserPresent(Long id);
}
