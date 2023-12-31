package ru.kaInc.shelterbot.utils;

import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.SendMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс-утил со статик методом для создания любой клавиатуры
 *
 * @author fifimova
 */
public class CustomKeyboard {

    /**
     * Принимает идентификатор чата, текст сообщения и список списков строк, представляющих собой метки кнопок.
     * Каждый внутренний список представляет собой ряд кнопок на клавиатуре.*
     * Метод создает объект SendMessage, конфигурирует ReplyKeyboardMarkup с помощью предоставленных меток кнопок.
     *
     * @param chatId
     * @param text
     * @param buttonLabels
     * @return объект SendMessage, который затем можно отправить боту для отображения пользователю.
     */
    public static SendMessage createKeyboardButtons(long chatId, String text, List<List<String>> buttonLabels) {
        List<KeyboardButton[]> keyboardRows = new ArrayList<>();
        for (List<String> rowLabels : buttonLabels) {
            KeyboardButton[] row = new KeyboardButton[rowLabels.size()];
            for (int i = 0; i < rowLabels.size(); i++) {
                row[i] = new KeyboardButton(rowLabels.get(i));
            }
            keyboardRows.add(row);
        }
        Keyboard replyKeyboard = new ReplyKeyboardMarkup(keyboardRows.toArray(new KeyboardButton[0][0]));
        return new SendMessage(String.valueOf(chatId), text).replyMarkup(replyKeyboard);
    }

    public static SendMessage createKeyboardInline(long chatId, String text, List<List<String>> buttonLabels, List<List<String>> callbackData) {
        List<InlineKeyboardButton[]> keyboardRows = new ArrayList<>();
        for (int i = 0; i < buttonLabels.size(); i++) {
            List<String> rowLabels = buttonLabels.get(i);
            List<String> rowData = callbackData.get(i);
            InlineKeyboardButton[] row = new InlineKeyboardButton[rowLabels.size()];
            for (int j = 0; j < rowLabels.size(); j++) {
                row[j] = new InlineKeyboardButton(rowLabels.get(j)).callbackData(rowData.get(j));
            }
            keyboardRows.add(row);
        }
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(keyboardRows.toArray(new InlineKeyboardButton[0][0]));
        return new SendMessage(String.valueOf(chatId), text).replyMarkup(markup);
    }
}

