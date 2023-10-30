package ru.kaInc.shelterbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;

import java.util.List;

/**
 * The KeyboardBasic interface defines methods for processing user commands and initiating actions related to keyboard interactions in the bot's system.
 * It provides functionality for handling user updates and calling volunteers.
 */

public interface KeyboardBasic {

    /**
     * Processes user commands based on the provided list of Telegram Updates and sends responses using the specified TelegramBot instance.
     *
     * @param update      A list of Telegram Update objects representing user interactions.
     * @param telegramBot The TelegramBot instance responsible for processing updates and sending responses.
     */
    void processCommands(List<Update> update, TelegramBot telegramBot);

    /**
     * Initiates a call for volunteers or assistance, typically used in emergency situations or to request help.
     */

    void callVolunteer();
}
