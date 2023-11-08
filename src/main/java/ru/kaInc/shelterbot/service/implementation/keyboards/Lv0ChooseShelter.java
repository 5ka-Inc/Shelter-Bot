package ru.kaInc.shelterbot.service.implementation.keyboards;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.kaInc.shelterbot.service.KeyboardBasic;
import ru.kaInc.shelterbot.utils.CustomKeyboard;

import java.util.*;


/**
 * The KeyboardBasicIml class is an implementation of the KeyboardBasic interface and is responsible for handling keyboard interactions and creating unique inline keyboard buttons.
 */

@Service
public class Lv0ChooseShelter implements KeyboardBasic {

    private final Logger logger = LoggerFactory.getLogger(Lv0ChooseShelter.class);

    private final Lv1ChooseInfo nextLvlKeyboard;

    private final UniqueButtonCreator buttonCreator;

    private static final List<String> BUTTONS = List.of("Приют для кошек", "Приют для собак");
    private static final String TITLE = "Выберите приют:";

    public Lv0ChooseShelter(Lv1ChooseInfo nextLvlKeyboard, UniqueButtonCreator buttonCreator) {
        this.nextLvlKeyboard = nextLvlKeyboard;
        this.buttonCreator = buttonCreator;
    }


    /**
     * Processes a list of updates, creates and sends unique inline keyboard buttons in response to each update to a specified chat.
     *
     * @param updates     A list of Telegram Update objects representing user interactions.
     * @param telegramBot The TelegramBot instance used to send messages with inline keyboard buttons.
     */
    @Override
    public void processCommands(List<Update> updates, TelegramBot telegramBot) {


        /* для хранения уникальных chatId и List<Update> для хранения обновлений с запросами обратного вызова */
        Set<Long> chatIds = new HashSet<>();
        List<Update> callbackUpdates = new ArrayList<>();

        for (Update update : updates) {
            Long chatId = getChatId(update); /* Проходит через каждое обновление, извлекает chatId с помощью отдельного метода getChatId и добавляет его в chatIds */
            if (chatId != null) {
                chatIds.add(chatId);

                if (update.message() != null) {
                    /* Если сообщение присутствует в обновлении, создает клавиатуру с помощью CustomKeyboard и отправляет ее через telegramBot */
                    createButtons(chatId, telegramBot);
                } else if (update.callbackQuery() != null) { /* Если запрос обратного вызова присутствует в обновлении, добавляет обновление в callbackUpdates */
                    callbackUpdates.add(update);
                }
            }
        }
        if (!callbackUpdates.isEmpty()) {
           nextLvlKeyboard.processCommands(callbackUpdates, telegramBot);
        }
    }

    private Long getChatId(Update update) {
        if (update.message() != null) {
            return update.message().chat().id();
        } else if (update.callbackQuery() != null) {
            return update.callbackQuery().message().chat().id();
        }
        return null;
    }

    /**
     * Creates a message with inline keyboard buttons for selecting a shelter type and sends it to the specified chat.
     *
     * @param chatId      The ID of the chat to which the message with buttons should be sent.
     * @param telegramBot The TelegramBot instance used to send the message.
     */
    public void createButtons(long chatId, TelegramBot telegramBot) {
        buttonCreator.createButtons(chatId,telegramBot, TITLE, BUTTONS, BUTTONS);
    }

}
