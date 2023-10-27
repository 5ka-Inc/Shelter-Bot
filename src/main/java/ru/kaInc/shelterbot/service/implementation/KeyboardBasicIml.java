package ru.kaInc.shelterbot.service.implementation;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.kaInc.shelterbot.service.KeyboardBasic;

import java.util.List;

/**
 * The KeyboardBasicIml class is an implementation of the KeyboardBasic interface and is responsible for handling keyboard interactions and creating unique inline keyboard buttons.
 */
@Service
public class KeyboardBasicIml implements KeyboardBasic {

    private final Logger logger = LoggerFactory.getLogger(KeyboardBasicIml.class);
    private final UniqueButtonCreation uniqueButtonCreation;

    /**
     * Constructor for creating an instance of KeyboardBasicIml with a UniqueButtonCreation dependency.
     *
     * @param uniqueButtonCreation The UniqueButtonCreation instance used for creating unique inline keyboard buttons.
     */
    public KeyboardBasicIml(UniqueButtonCreation uniqueButtonCreation) {
        this.uniqueButtonCreation = uniqueButtonCreation;
    }

    /**
     * Processes a list of updates, creates and sends unique inline keyboard buttons in response to each update to a specified chat.
     *
     * @param updates     A list of Telegram Update objects representing user interactions.
     * @param telegramBot The TelegramBot instance used to send messages with inline keyboard buttons.
     */
    @Override
    public void processCommands(List<Update> updates, TelegramBot telegramBot) {
        if (updates == null || updates.isEmpty()) {
            logger.warn("Updates is null or empty");
            return;
        }

        updates.forEach(update -> {
            Long chatId = update.message().chat().id();
            List<String> buttonsText = List.of("кнопкdsа 1", "кнопка2", "кнопка3ddd", "кнопка 4");
            List<String> buttonCallBack = List.of("First", "Last", "asdadad", "Asdad");
            uniqueButtonCreation.createButtons(chatId, telegramBot, "Текст сообшasdasdения", buttonsText, buttonCallBack);
        });
    }

    /**
     * Creates a message with inline keyboard buttons for selecting a shelter type and sends it to the specified chat.
     *
     * @param chatId      The ID of the chat to which the message with buttons should be sent.
     * @param telegramBot The TelegramBot instance used to send the message.
     */
    public void createButtons(long chatId, TelegramBot telegramBot) {

        // Создание кнопок
        InlineKeyboardButton button1 = new InlineKeyboardButton("Приют для кошек");
        InlineKeyboardButton button2 = new InlineKeyboardButton("Приют для собак");
        InlineKeyboardButton button3 = new InlineKeyboardButton("Позвать волонтера");

        // Создание разметки для кнопок
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{
                        button1.callbackData("callback_data_1"),
                        button2.callbackData("callback_data_2")
                },
                new InlineKeyboardButton[]{
                        button3.callbackData("callback_data_3")
                }
        );
        // Создание сообщения с кнопками
        SendMessage request = new SendMessage(chatId, "Выберите приют");
        request.replyMarkup(markup);
        telegramBot.execute(request);
    }

    /**
     * A placeholder method for handling the action of calling a volunteer.
     */
    @Override
    public void callVolunteer() {

    }
}
