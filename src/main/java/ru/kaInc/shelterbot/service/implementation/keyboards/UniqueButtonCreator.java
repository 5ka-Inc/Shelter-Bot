package ru.kaInc.shelterbot.service.implementation.keyboards;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * The UniqueButtonCreation class provides a utility for creating unique inline keyboard buttons and sending them in a message to a chat.
 */
@Service
public class UniqueButtonCreator {
    /**
     * Creates a message with a set of unique inline keyboard buttons and sends it to a specified chat.
     *
     * @param chatId       The ID of the chat to which the message with buttons should be sent.
     * @param telegramBot  The TelegramBot instance used to send the message.
     * @param messageText  The text of the message that accompanies the inline keyboard buttons.
     * @param buttonLabels A list of labels for the inline keyboard buttons, where each label is unique.
     * @param callbackData A list of callback data associated with each button label, used to identify button actions.
     */
    public void createButtons(long chatId, TelegramBot telegramBot, String messageText, List<String> buttonLabels, List<String> callbackData) {

        if (buttonLabels.size() != callbackData.size()) {
            throw new IllegalArgumentException("Number of button labels must be equal to number of callbacks");
        }

        List<InlineKeyboardButton> buttons = new ArrayList<>();

        for (int i = 0; i < buttonLabels.size(); i++) {
            InlineKeyboardButton button = new InlineKeyboardButton(buttonLabels.get(i));
            button.callbackData(callbackData.get(i));
            buttons.add(button);
        }
        InlineKeyboardButton volunteerCall = new InlineKeyboardButton("Позвать волонтера");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(buttons.toArray(new InlineKeyboardButton[0]),
                new InlineKeyboardButton[]{
                        volunteerCall.callbackData("call_volunteer")}
        );


        SendMessage request = new SendMessage(chatId, messageText);
        request.replyMarkup(markup);

        telegramBot.execute(request);
    }

}
