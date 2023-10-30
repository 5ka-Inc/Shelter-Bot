package ru.kaInc.shelterbot.service.implementation;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UniqueButtonCreation {


    /*
    * Получаем на вход чат id, телеграм бот,
    *  текст сообщения к которому прикряпляются кнопки,
    *  лист текстов самих кнопок и callback данные чтобы знать какие кнопки были нажаты
    * */
    public void createButtons(long chatId, TelegramBot telegramBot, String messageText, List<String> buttonLabels, List<String> callbackData) {
        // Лист где будут храниться сами кнопик
        List<InlineKeyboardButton> buttons = new ArrayList<>();

        for (int i = 0; i < buttonLabels.size(); i++) {
            //создание каждой кнопки
            InlineKeyboardButton button = new InlineKeyboardButton(buttonLabels.get(i));
            button.callbackData(callbackData.get(i));
            //добавление этих кнопок в лист
            buttons.add(button);
        }

        // Создание разметки для кнопок
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(buttons.toArray(new InlineKeyboardButton[0]));

        // Создание сообщения и прекрепления к нему кнопок
        SendMessage request = new SendMessage(chatId, messageText);
        request.replyMarkup(markup);

        telegramBot.execute(request);
    }


}
