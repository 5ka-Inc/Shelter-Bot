package ru.kaInc.shelterbot.service.implementation;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.kaInc.shelterbot.model.User;
import ru.kaInc.shelterbot.service.KeyboardBasic;
import ru.kaInc.shelterbot.service.UpdateHubService;
import ru.kaInc.shelterbot.service.UserService;

import java.util.List;

@Service
public class UpdateHubServiceImpl implements UpdateHubService {

    UserService userService;
    KeyboardBasic keyboardBasic;
    private final Logger logger = LoggerFactory.getLogger(UpdateHubServiceImpl.class);

    public UpdateHubServiceImpl(UserService userService, KeyboardBasicIml keyboardBasic) {
        this.userService = userService;
        this.keyboardBasic = keyboardBasic;
    }

    public void process(List<Update> updates, TelegramBot telegramBot) {
        if (updates == null || updates.isEmpty()) {
            logger.warn("Updates is null or empty");
            return;
        }
        updates.forEach(update -> {
            if (update.message() != null && update.message().text() != null) {
                processStart(update, updates, telegramBot);
            } else if (update.callbackQuery() != null) {
                keyboardBasic.processCommands(updates, telegramBot);
            }
        });
    }

    @Override
    public void addUserIfNew(Update update) {
        if (!userService.isUserPresent(update.message().from().id())) {
            createNewUSer(update.message().from(), update.message().chat().id());
        }
    }

    @Override
    public User createNewUser(Long id, Long chatId, String name) {
        User newUser = userService.addNewUser(id, chatId, name);
        logger.info("Added {} {}", newUser.getName(), newUser.getId());
        return newUser;

    }

    @Override
    public User createNewUSer(com.pengrad.telegrambot.model.User user, Long chatId) {
        User newUser = userService.addNewUser(user, chatId);
        logger.info("Added {} {}", newUser.getName(), newUser.getId());
        return newUser;
    }

    @Override
    public void processStart(Update update, List<Update> updates, TelegramBot telegramBot) {
        if (update.message().text().equals("/start")) {
            keyboardBasic.processCommands(updates, telegramBot);
        } else {
            SendMessage message = new SendMessage(update.message().chat().id(), "Айнц - цвай - драй - ничего не панимай");
            telegramBot.execute(message);
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
            keyboardBasic.processCommands(updates, telegramBot);
        }
    }
}
