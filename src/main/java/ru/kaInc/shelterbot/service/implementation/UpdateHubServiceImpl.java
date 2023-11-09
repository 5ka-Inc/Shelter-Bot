package ru.kaInc.shelterbot.service.implementation;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.kaInc.shelterbot.model.User;
import ru.kaInc.shelterbot.service.UpdateHubService;
import ru.kaInc.shelterbot.service.UserService;
import ru.kaInc.shelterbot.service.implementation.keyboard.UniversalKeyboard;

import java.util.List;

/**
 * The UpdateHubServiceImpl class is an implementation of the UpdateHubService interface and is responsible for processing updates and managing user interactions in the bot's system.
 */
@Service
public class UpdateHubServiceImpl implements UpdateHubService {

    UserService userService;
    UniversalKeyboard universalKeyboard;
    private final Logger logger = LoggerFactory.getLogger(UpdateHubServiceImpl.class);


    /**
     * Constructor for creating an instance of UpdateHubServiceImpl with UserService and KeyboardBasic dependencies.
     *
     * @param userService The UserService used for managing user-related operations.
     */

    public UpdateHubServiceImpl(UserService userService, UniversalKeyboard universalKeyboard) {
        this.userService = userService;
        this.universalKeyboard = universalKeyboard;
    }

    /**
     * Processes a list of Telegram updates, checks for the "/start" command, and calls relevant methods based on the update content.
     *
     * @param updates     A list of Telegram Update objects representing user interactions.
     * @param telegramBot The TelegramBot instance responsible for processing updates and sending responses.
     */
    public void process(List<Update> updates, TelegramBot telegramBot) {
        if (updates == null || updates.isEmpty()) {
            logger.warn("Updates is null or empty");
            return;
        }
        updates.forEach(update -> {
            if (update.message() != null && update.message().text() != null) {
                processStart(update, updates, telegramBot);
            } else if (update.callbackQuery() != null) {
                universalKeyboard.process(updates, telegramBot);

            }
        });
    }

    /**
     * Checks if the user associated with the given Update is present in the bot's database and adds them if not.
     *
     * @param update The Telegram Update object representing a user's interaction.
     */
    @Override
    public void addUserIfNew(Update update) {
        if (update == null) {
            logger.warn("Got null update in {}", Thread.currentThread().getStackTrace()[2].getMethodName());
            return;
        }
        if (update.message().from() == null) {
            logger.warn("Got null user in {}", Thread.currentThread().getStackTrace()[2].getMethodName());
            return;
        }

        if (!userService.isUserPresent(update.message().from().id())) {
            createNewUSer(update.message().from(), update.message().chat().id());
        }
    }

    /**
     * Creates a new user in the bot's database with the specified user information.
     *
     * @param id     The unique Telegram user ID to be used as the user's identifier in the bot's system.
     * @param chatId The ID of the chat associated with this user.
     * @param name   The name of the user, which can be the default Telegram username or a real name.
     * @return The created User object with a unique identifier.
     */
    @Override
    public User createNewUser(Long id, Long chatId, String name) {
        User newUser = userService.addNewUser(id, chatId, name);
        logger.info("Added {} {}", newUser.getName(), newUser.getId());
        return newUser;

    }

    /**
     * Creates a new user in the bot's database based on a Telegram User object and the associated chat ID.
     *
     * @param user   The Telegram User object from which the user's ID and username will be extracted and used.
     * @param chatId The ID of the chat associated with this user.
     * @return The created User object with a unique identifier.
     */
    @Override
    public User createNewUSer(com.pengrad.telegrambot.model.User user, Long chatId) {
        User newUser = userService.addNewUser(user, chatId);
        logger.info("Added {} {}", newUser.getName(), newUser.getId());
        return newUser;
    }

    /**
     * Processes the "start" command in a user interaction, calling addUserIfNew and handling keyboard interactions.
     *
     * @param update      The Telegram Update object representing a user's interaction.
     * @param updates     A list of updates to be processed.
     * @param telegramBot The TelegramBot instance responsible for handling updates and sending responses.
     */
    @Override
    public void processStart(Update update, List<Update> updates, TelegramBot telegramBot) {
        if (update.message().text().equals("/start")) {
            universalKeyboard.process(updates, telegramBot);
        } else {
            SendMessage message = new SendMessage(update.message().chat().id(), "Айнц - цвай - драй - ничего не панимай"); // ВЕРНУТЬ В СТАТИЧЕСКУЮ ПЕРЕМЕННУЮ
            telegramBot.execute(message);
            universalKeyboard.process(updates, telegramBot);
        }
    }
}

