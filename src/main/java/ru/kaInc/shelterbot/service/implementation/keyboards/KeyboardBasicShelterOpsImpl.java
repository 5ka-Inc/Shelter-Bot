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
public class KeyboardBasicShelterOpsImpl implements KeyboardBasic {
    @Autowired
    private UniqueButtonCreator buttonsCreator;
    //Предлагаю вынести функционал создания кнопки с вызвом волонтера в класс UniqueButtonCreation, что бы он всегда создавался даже без передачи в него этих данных.
    //UPD Это сделал. Теперь обработку колбека на вызов волонтера имеет смысл вынести в хаб, поскольку он может прилететь откуда угодно и всегда будет одинаковым. Нет смысла плодить лишнюю логику, которая может быть обработана сразу


    private final static List<String> CALLBACKS = List.of("Информация о приюте", "Как взять животное");
    //Так же в большинстве случаев коллбеки будут совпадать с надписями на кнопках, т.к. на следующем уровне меню мы используем текст именно колбека в кач-ве названия/описания меню.
    private final static List<String> LABELS = List.copyOf(CALLBACKS);

    private final Logger logger = LoggerFactory.getLogger(KeyboardBasicShelterOpsImpl.class);

    @Override
    public void processCommands(List<Update> updates, TelegramBot telegramBot) {
        AtomicLong chatId = new AtomicLong();
        updates.forEach((update -> {
            chatId.set(update.callbackQuery().message().chat().id());
            buttonsCreator.createButtons(chatId.get(), telegramBot, update.callbackQuery().data(), LABELS, CALLBACKS);
        }));
    }

    /**
     * Судя по всему этот метод уже не будет нужен исходя из моих комментов выше
     */
    @Override
    @Deprecated
    public void callVolunteer() {

    }
}