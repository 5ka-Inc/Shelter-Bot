package ru.kaInc.shelterbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import ru.kaInc.shelterbot.model.User;

import java.util.List;

/**
 * The UpdateHubService interface defines methods for managing updates and users within the bot's system.
 * It provides functionality for adding new users, processing update events, and creating users based on Telegram updates.
 */

public interface UpdateHubService {

    /**
     * Adds a new user if they don't already exist in the bot's user repository based on the provided Update object.
     * @param update The Telegram Update object representing a user's interaction or message.
     */

    void addUserIfNew(Update update);

    /**
     * Creates a new user in the bot's database with the specified user information.
     * @param id The unique Telegram user ID to be used as the user's identifier in the bot's system.
     * @param chatId The ID of the chat associated with this user.
     * @param name The name of the user, which can be the default Telegram username or a real name.
     * @return The created User object.
     */

    User createNewUser(Long id, Long chatId, String name);

    /**
     * Creates a new user in the bot's database based on a Telegram User object and the associated chat ID.
     * @param user The Telegram User object from which the user's ID and username will be extracted and used.
     * @param chatId The ID of the chat associated with this user.
     * @return The created User object.
     */

    User createNewUSer(com.pengrad.telegrambot.model.User user, Long chatId);

    /**
     * Processes the "start" event and updates for a user, typically triggered when a user interacts with the bot.
     * @param update The Telegram Update object representing a user's interaction.
     * @param updates A list of updates to be processed.
     * @param telegramBot The TelegramBot instance responsible for handling updates and sending responses.
     */

    void processStart(Update update, List<Update> updates, TelegramBot telegramBot);

    void processCallVolunteer(Update update, TelegramBot telegramBot);
}
