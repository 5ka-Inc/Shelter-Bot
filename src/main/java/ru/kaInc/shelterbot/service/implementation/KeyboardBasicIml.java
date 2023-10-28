package ru.kaInc.shelterbot.service.implementation;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
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
        List<List<String>> buttonLabels = new ArrayList<>();
        List<List<String>> callbackData = new ArrayList<>();

        // Создание меток и данных обратного вызова для кнопок
        buttonLabels.add(Arrays.asList("Приют для кошек", "Приют для собак"));
        buttonLabels.add(Collections.singletonList("Позвать волонтера"));

        callbackData.add(Arrays.asList("Кошачий приют", "Собачий приют"));
        callbackData.add(Collections.singletonList("Вызов волонтера"));

        // Создание клавиатуры с помощью CustomKeyboard.createKeyboard
        SendMessage message = CustomKeyboard.createKeyboardInline(chatId, "Выберите приют:", buttonLabels, callbackData);
        telegramBot.execute(message);
    }

    @Override
    public void callVolunteer() {

    }
}
