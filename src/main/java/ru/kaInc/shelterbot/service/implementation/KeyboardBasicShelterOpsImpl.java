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
public class KeyboardBasicShelterOpsImpl implements KeyboardBasic {

    private final Logger logger = LoggerFactory.getLogger(KeyboardBasicShelterOpsImpl.class);

    @Override
    public void processCommands(List<Update> updates, TelegramBot telegramBot) {
        if (updates == null || updates.isEmpty()) {
            logger.warn("Updates is null or empty");
            return;
        }
        AtomicLong chatId = new AtomicLong();
        updates.forEach((update -> {
            chatId.set(update.callbackQuery().message().chat().id());
            createButtons(chatId.get(), telegramBot, update.callbackQuery().data());
        }));

    }

    public void createButtons(long chatId, TelegramBot telegramBot, String callbackData) {

        // Создание кнопок
        InlineKeyboardButton button1 = new InlineKeyboardButton("Информация о приюте");
        InlineKeyboardButton button2 = new InlineKeyboardButton("Как взять животное из приюта");
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
        SendMessage request = new SendMessage(chatId, callbackData + ":");

        request.replyMarkup(markup);
        telegramBot.execute(request);
    }

    @Override
    public void callVolunteer() {

    }

}