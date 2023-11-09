package ru.kaInc.shelterbot.service.implementation.keyboard;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;

import org.springframework.stereotype.Service;

import ru.kaInc.shelterbot.model.enums.Callback;
import ru.kaInc.shelterbot.model.enums.Type;

import java.util.ArrayList;

import java.util.List;


@Service
public class UniversalKeyboard {
    private Type type;
    private final UniqueButtonCreator buttonCreator;


    public UniversalKeyboard(UniqueButtonCreator buttonCreator) {
        this.buttonCreator = buttonCreator;
    }

    public void process(List<Update> updates, TelegramBot telegramBot) {
        updates.forEach(update -> {
            Callback callback = defineCallback(getText(update));
            if (callback.getShelter() != null) {
                type = callback.getShelter();
            }
            buttonCreator.createButtons(getChatId(update), telegramBot, callback.getText(), getLabels(callback), getButtons(callback));
        });

    }

    //Переделал стримы на циклы во имя производительности
    private List<String> getLabels(Callback callback) {
        ArrayList<Callback> callbacks = new ArrayList<>(List.copyOf(callback.getNextMenu()));
        if (type != null && callback.getUniqueCallbacks() != null) {
            callbacks.addAll(callback.getUniqueCallbacks().get(type));
        }

        List<String> result = new ArrayList<>();
        for (int i = 0; i < callbacks.size(); i++) {
            result.add(i, callbacks.get(i).getText());
        }

        return result;
    }

    private List<String> getButtons(Callback callback) {
        ArrayList<Callback> callbacks = new ArrayList<>(List.copyOf(callback.getNextMenu()));
        if (type != null && callback.getUniqueCallbacks() != null) {
            callbacks.addAll(callback.getUniqueCallbacks().get(type));
        }

        List<String> result = new ArrayList<>();
        for (int i = 0; i < callbacks.size(); i++) {
            result.add(i, callbacks.get(i).name());
        }

        return result;
    }

    private Callback defineCallback(String callback) {
        //Избавился от стрима во имя производительности
        for (int i = 0; i < Callback.values().length; i++) {
            if (Callback.values()[i].name().equals(callback)) {
                return Callback.values()[i];
            }
        }
        return Callback.DEFAULT_MENU;
    }

    private Long getChatId(Update update) {
        if (update.message() != null) {
            return update.message().chat().id();
        }
        return update.callbackQuery().message().chat().id();
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
