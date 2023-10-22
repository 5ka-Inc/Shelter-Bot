package ru.kaInc.shelterbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import ru.kaInc.shelterbot.model.User;

import java.util.List;


public interface UpdateHubService {

    void addUserIfNew(Update update);

    User createNewUser(Long id, Long chatId, String name);

    User createNewUSer(com.pengrad.telegrambot.model.User user, Long chatId);

    void processStart(Update update, List<Update> updates, TelegramBot telegramBot);
}
