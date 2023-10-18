package ru.kaInc.shelterbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(BotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

    @PostConstruct
    public void initialize() {
        System.out.println(telegramBot.getToken());
        telegramBot.setUpdatesListener(this);
        logger.info("Bot's updateListener is set");
    }

    @Override
    public int process(List<Update> list) {
        if (list != null && !list.isEmpty()) {
            list.forEach(update -> {   //Placeholder code for future updates
                logger.debug("Processing update {}", update);
                if (update.message().text().startsWith("/")) {
                    SendMessage message = new SendMessage(update.message().chat().id(), "See Your command " + update.message().text());
                    telegramBot.execute(message);
                }
            });
        } else {
            throw new NullPointerException("Updates list is null or empty!");
        }

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
