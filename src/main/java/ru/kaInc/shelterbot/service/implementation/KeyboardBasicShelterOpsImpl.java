package ru.kaInc.shelterbot.service.implementation;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.kaInc.shelterbot.service.KeyboardBasic;
import ru.kaInc.shelterbot.utils.CustomKeyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class KeyboardBasicShelterOpsImpl implements KeyboardBasic {

    private final FirstStage firstStage;
    private final Logger logger = LoggerFactory.getLogger(KeyboardBasicShelterOpsImpl.class);

    @Override
    public void processCommands(List<Update> updates, TelegramBot telegramBot) {
        if (updates == null || updates.isEmpty()) {
            logger.warn("Updates is null or empty");
            return;
        }

        AtomicLong chatId = new AtomicLong();
        List<Update> callbackUpdates = new ArrayList<>();

        updates.forEach((update -> {
            if (update.callbackQuery() != null && update.callbackQuery().message() != null) {
                chatId.set(update.callbackQuery().message().chat().id());
                String callbackData = update.callbackQuery().data();
                switch (callbackData) {
                    case "Приют для кошек", "Приют для собак":
                        createButtons(chatId.get(), telegramBot);
                        break;
                    case "Позвать волонтера":
                        callVolunteer();
                        break;
                    default:
                        logger.warn("Unknown callback data keybOps: " + callbackData);
                        break;
                }
            }
        }));
        firstStage.processCommands(callbackUpdates, telegramBot);
    }

    public void createButtons(Long chatId, TelegramBot telegramBot) {
        List<List<String>> buttonLabels = new ArrayList<>();
        List<List<String>> callbackData = new ArrayList<>();

        buttonLabels.add(Arrays.asList("Информация о приюте", "Как взять животное из приюта"));
        buttonLabels.add(Collections.singletonList("Позвать волонтера"));

        callbackData.add(Arrays.asList("Информация о приюте", "Как взять животное из приюта"));
        callbackData.add(Collections.singletonList("Позвать волонтера"));

        SendMessage message = CustomKeyboard.createKeyboardInline(chatId, "Что бы вы хотели узнать?", buttonLabels, callbackData);
        telegramBot.execute(message);
    }

    @Override
    public void callVolunteer() {

    }
}