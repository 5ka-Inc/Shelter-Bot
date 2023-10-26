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
import ru.kaInc.shelterbot.utils.CustomKeyboard;

import java.util.*;

@Service
public class KeyboardBasicIml implements KeyboardBasic {

    private final Logger logger = LoggerFactory.getLogger(KeyboardBasicIml.class);
    private final UniqueButtonCreation uniqueButtonCreation;
    private final KeyboardBasicShelterOpsImpl keyboardBasicShelterOps;

    public KeyboardBasicIml(UniqueButtonCreation uniqueButtonCreation, KeyboardBasicShelterOpsImpl keyboardBasicShelterOps) {
        this.uniqueButtonCreation = uniqueButtonCreation;
        this.keyboardBasicShelterOps = keyboardBasicShelterOps;
    }


    @Override
    public void processCommands(List<Update> updates, TelegramBot telegramBot) {
        if (updates == null || updates.isEmpty()) {
            logger.warn("Updates is null or empty");
            return;
        }

        /* для хранения уникальных chatId и List<Update> для хранения обновлений с запросами обратного вызова */
        Set<Long> chatIds = new HashSet<>();
        List<Update> callbackUpdates = new ArrayList<>();

        for (Update update : updates) {
            Long chatId = getChatId(update); /* Проходит через каждое обновление, извлекает chatId с помощью отдельного метода getChatId и добавляет его в chatIds */
            if (chatId != null) {
                chatIds.add(chatId);

                if (update.message() != null) {
                    /* Если сообщение присутствует в обновлении, создает клавиатуру с помощью CustomKeyboard и отправляет ее через telegramBot */
                    List<List<String>> buttonLabels = Arrays.asList(
                            Arrays.asList("Button 1", "Button 2"),
                            Arrays.asList("Button 3", "Button 4")
                    );
                    SendMessage message = CustomKeyboard.createKeyboard(chatId, "Choose an option:", buttonLabels);
                    telegramBot.execute(message);
                } else if (update.callbackQuery() != null) { /* Если запрос обратного вызова присутствует в обновлении, добавляет обновление в callbackUpdates */
                    callbackUpdates.add(update);
                }
            }
        }
        if (!callbackUpdates.isEmpty()) {
            keyboardBasicShelterOps.processCommands(callbackUpdates, telegramBot);
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

    @Override
    public void callVolunteer() {

    }
}
