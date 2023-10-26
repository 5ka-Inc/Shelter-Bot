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
import java.util.concurrent.atomic.AtomicLong;

@Service
public class KeyboardBasicIml implements KeyboardBasic {

    private final Logger logger = LoggerFactory.getLogger(KeyboardBasicIml.class);
    private final KeyboardBasicShelterOpsImpl keyboardBasicShelterOps;

    public KeyboardBasicIml(KeyboardBasicShelterOpsImpl keyboardBasicShelterOps) {
        this.keyboardBasicShelterOps = keyboardBasicShelterOps;
    }

    @Override

    public void processCommands(List<Update> updates, TelegramBot telegramBot) {
        if (updates == null || updates.isEmpty()) {
            logger.warn("Updates is null or empty");
            return;
        }
        AtomicLong chatId = new AtomicLong();
        updates.forEach(update -> {
            if (update.message() != null) {
                chatId.set(update.message().chat().id());
                createButtons(chatId.get(), telegramBot);
            } else if (update.callbackQuery() != null) {
                chatId.set(update.callbackQuery().message().chat().id());
                keyboardBasicShelterOps.processCommands(updates, telegramBot);
            }
        });
    }

    public void createButtons(long chatId, TelegramBot telegramBot) {

        // Создание кнопок
        InlineKeyboardButton button1 = new InlineKeyboardButton("Приют для кошек");
        InlineKeyboardButton button2 = new InlineKeyboardButton("Приют для собак");
        InlineKeyboardButton button3 = new InlineKeyboardButton("Позвать волонтера");

        // Создание разметки для кнопок
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{
                        button1.callbackData("Кошачий приют"),
                        button2.callbackData("Басячий приют")
                },
                new InlineKeyboardButton[]{
                        button3.callbackData("Вызов волонтера")
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
