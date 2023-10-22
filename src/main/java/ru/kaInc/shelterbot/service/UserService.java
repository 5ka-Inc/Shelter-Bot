package ru.kaInc.shelterbot.service;

import ru.kaInc.shelterbot.model.User;


public interface UserService {

    User addNewUser(Long id, long chatId, String name);

    boolean isUserPresent(Long id);
}
