package ru.kaInc.shelterbot.service;

import com.pengrad.telegrambot.model.Update;
import ru.kaInc.shelterbot.model.User;


public interface UpdateHubService {

    void addUserIfNew(Update update);

    User createNewUser(Long id, Long chatId, String name);

    void processStart(Update update);


}
