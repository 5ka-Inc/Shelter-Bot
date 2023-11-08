package ru.kaInc.shelterbot.service.implementation.keyboards;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kaInc.shelterbot.service.KeyboardBasic;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class Lv2_1AboutShelterMenu implements KeyboardBasic {

    private UniqueButtonCreator buttonsCreator;

    private final static List<String> CALLBACKS = List.of("О приюте", "Расписание, схема проезда", "Контакты охраны", "На что наматываться, пока инженер по ТБ вышел покурить", "Отправить документы");


    private final Logger logger = LoggerFactory.getLogger(Lv1ChooseInfo.class);

    public Lv2_1AboutShelterMenu(UniqueButtonCreator buttonsCreator) {
        this.buttonsCreator = buttonsCreator;
    }

    @Override
    public void processCommands(List<Update> updates, TelegramBot telegramBot) {
        AtomicLong chatId = new AtomicLong();
        updates.forEach((update -> {
            chatId.set(update.callbackQuery().message().chat().id());
            try {
                buttonsCreator.createButtons(chatId.get(), telegramBot, update.callbackQuery().data(), CALLBACKS, CALLBACKS);
            } catch (IllegalArgumentException e) {
                logger.error(e.getMessage(), e);
            }
        }));
    }
}
