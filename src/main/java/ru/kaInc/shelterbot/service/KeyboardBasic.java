package ru.kaInc.shelterbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;

import java.util.List;

/**
 * На основе этого интерфейса создаются все простые наборы кнопок(команд)
 * Возможно, будем делать все меню от этого интерфейса, а возможно сделаем ему более узконаправленных наследников
 */

public interface KeyboardBasic {
    void processCommands(List<Update> update, TelegramBot telegramBot);

    void callVolunteer();
}
