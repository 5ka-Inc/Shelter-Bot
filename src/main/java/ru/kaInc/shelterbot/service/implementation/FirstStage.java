package ru.kaInc.shelterbot.service.implementation;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.kaInc.shelterbot.service.KeyboardBasic;
import ru.kaInc.shelterbot.utils.CustomKeyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
@Slf4j
public class FirstStage implements KeyboardBasic {

    @Override
    public void processCommands(List<Update> updates, TelegramBot telegramBot) {
        AtomicLong chatId = new AtomicLong();
        updates.forEach((update -> {
            if (update.callbackQuery() != null && update.callbackQuery().message() != null) {
                chatId.set(update.callbackQuery().message().chat().id());
                String callbackData = update.callbackQuery().data();

                switch (callbackData) {
                    case "Информация о приюте":
                        showShelterInfo(chatId.get(), telegramBot);
                        break;
                    case "Как взять животное из приюта":
                        showAdoptionInfo(chatId.get(), telegramBot);
                        break;
                    case "Позвать волонтера":
                        callVolunteer();
                        break;
                    default:
                        log.warn("Unknown callback data first stage: " + callbackData);
                        break;
                }
            }
        }));
    }

    public void showShelterInfo(long chatId, TelegramBot telegramBot) {
        List<List<String>> buttonLabels = new ArrayList<>();
        List<List<String>> callbackData = new ArrayList<>();

        buttonLabels.add(Arrays.asList("Рассказать о приюте", "График работы", "Адрес"));
        buttonLabels.add(Arrays.asList("Контактные данные охраны", "Техника безопасности"));

        callbackData.add(Arrays.asList("GENERAL", "SCHEDULE", "ADDRESS"));
        callbackData.add(Arrays.asList("SECURITY", "SAFETY_PRECAUTIONS"));

        SendMessage message = CustomKeyboard.createKeyboardInline(chatId, "Информация о приюте", buttonLabels, callbackData);
        telegramBot.execute(message);
    }

    public void showAdoptionInfo(long chatId, TelegramBot telegramBot) {
        // Отправить информацию о том, как взять животное из приюта и/или новые кнопки
        // ...
    }

    @Override
    public void callVolunteer() {

    }
}
