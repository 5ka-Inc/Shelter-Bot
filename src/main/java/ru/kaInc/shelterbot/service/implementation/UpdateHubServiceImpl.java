package ru.kaInc.shelterbot.service.implementation;

import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.kaInc.shelterbot.model.User;
import ru.kaInc.shelterbot.service.UpdateHubService;
import ru.kaInc.shelterbot.service.UserService;

import java.util.List;

@Service
public class UpdateHubServiceImpl implements UpdateHubService {

    UserService userService;
    private final Logger logger = LoggerFactory.getLogger(UpdateHubServiceImpl.class);

    public UpdateHubServiceImpl(UserService userService) {
        this.userService = userService;
    }

    public void process(List<Update> updates) {
        if (updates == null || updates.isEmpty()) {
            logger.warn("Updates is null or empty");
            return;
        }

        updates.forEach(update -> {
            try {
                processStart(update);
            } catch (NullPointerException e) {
                logger.error(e.getMessage());
            }
        });


    }

    @Override
    public void addUserIfNew(Update update) {
        if (!userService.isUserPresent(update.message().from().id())) {
            createNewUser(update.message().from().id(), update.message().chat().id(), update.message().from().username());
        }
    }

    @Override
    public User createNewUser(Long id, Long chatId, String name) {
        return userService.addNewUser(id, chatId, name);
    }

    @Override
    public void processStart(Update update) {
        if (update.message().text().equals("/start")) {
            addUserIfNew(update);
        }
    }
}
