package ru.kaInc.shelterbot.service.implementation.keyboards;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.kaInc.shelterbot.service.KeyboardBasic;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class Lv1ChooseInfo implements KeyboardBasic {
    private UniqueButtonCreator buttonsCreator;

    private final static List<String> CALLBACKS = List.of("Информация о приюте", "Как взять животное");

    private final static List<String> LABELS = List.copyOf(CALLBACKS);

    private final Logger logger = LoggerFactory.getLogger(Lv1ChooseInfo.class);

    //Мапа для хранения соотоношения меню с необходимыми для них колбеками. Задел на реализацию без условных операторов и привязку необходимых меню к енамам их кнопок в будущем.
    private final Map<String, KeyboardBasic> menuMap;


    public Lv1ChooseInfo(UniqueButtonCreator buttonsCreator, Lv2_1AboutShelterMenu keyboard1) {
        this.buttonsCreator = buttonsCreator;
        this.menuMap = Map.of(CALLBACKS.get(0), keyboard1);
    }

    @Override
    public void processCommands(List<Update> updates, TelegramBot telegramBot) {
        AtomicLong chatId = new AtomicLong();
        updates.forEach((update -> {
            chatId.set(update.callbackQuery().message().chat().id());
            if (menuMap.containsKey(update.callbackQuery().data())) {
                menuMap.get(update.callbackQuery().data()).processCommands(updates, telegramBot);
            }
            try {
                buttonsCreator.createButtons(chatId.get(), telegramBot, update.callbackQuery().data(), LABELS, CALLBACKS);
            } catch (IllegalArgumentException e) {
                logger.error(e.getMessage(), e);
            }
            System.err.println(menuMap);
            System.err.println(update.callbackQuery().data());


        }));
    }

}