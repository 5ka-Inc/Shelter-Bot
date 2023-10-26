package ru.kaInc.shelterbot.utils;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
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
     * @param chatId
     * @param text
     * @param buttonLabels
     * @return объект SendMessage, который затем можно отправить боту для отображения пользователю.
     */
    public static SendMessage createKeyboard(long chatId, String text, List<List<String>> buttonLabels) {
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

}

