package ru.kaInc.shelterbot.service.implementation.keyboards;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.kaInc.shelterbot.model.enums.Callback;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class UniversalKeyboard {

    Logger logger = LoggerFactory.getLogger(UniversalKeyboard.class);
    private final UniqueButtonCreator buttonCreator;
    private final Map<Callback, List<Callback>> menuMap = Map.of(
            Callback.INFO_SHELTER, List.of(Callback.ADDRESS, Callback.TIME),
            Callback.INFO_ADOPTION, List.of(Callback.DOCUMENTS, Callback.INTRODUCTION)


    );


    public UniversalKeyboard(UniqueButtonCreator buttonCreator) {
        this.buttonCreator = buttonCreator;
    }

    public void process(List<Update> updates, TelegramBot telegramBot) {
        updates.forEach(update -> {
            Callback callback = defineCallback(getText(update));
            System.err.println(callback);
            buttonCreator.createButtons(getChatId(update), telegramBot, callback.getText(), callback.getNextMenu().stream().map(e -> e.getText()).toList(), callback.getNextMenu().stream().map(e -> e.name()).toList());
        });

    }

    private Callback defineCallback(String callback) {
        AtomicReference<Callback> result = new AtomicReference<>(Callback.DEFAULT_MENU);
        Arrays.stream(Callback.values()).forEach(element -> {
            if (element.name().equals(callback)) {
                result.set(element);
            }
        });
        return result.get();
    }

    private Long getChatId(Update update) {
        if (update.message() != null) {
            return update.message().chat().id();
        } else if (update.callbackQuery() != null) {
            return update.callbackQuery().message().chat().id();
        }
        return null;
    }


    private String getText(Update update) {
        if (update.message() != null) {
            return update.message().text();
        } else if (update.callbackQuery() != null) {
            return update.callbackQuery().data();
        }
        return null;
    }

}
