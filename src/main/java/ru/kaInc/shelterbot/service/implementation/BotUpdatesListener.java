package ru.kaInc.shelterbot.service.implementation;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The BotUpdatesListener class is a component responsible for listening to updates from the Telegram Bot API and processing them using the UpdateHubServiceImpl.
 */
@Component
public class BotUpdatesListener implements UpdatesListener {
    @Autowired
    UpdateHubServiceImpl updateHubService;
    private final Logger logger = LoggerFactory.getLogger(BotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

    /**
     * Initializes the BotUpdatesListener by setting it as the updates listener for the TelegramBot.
     * This method is automatically called after the bean is constructed.
     */
    @PostConstruct
    public void initialize() {
        telegramBot.setUpdatesListener(this);
        logger.info("Bot's updateListener is set");
    }

    /**
     * Processes a list of updates received from the Telegram Bot API by delegating the processing to the UpdateHubServiceImpl.
     *
     * @param list A list of Telegram Update objects representing user interactions.
     * @return An integer constant UpdatesListener.CONFIRMED_UPDATES_ALL to confirm that all updates have been processed.
     */
    @Override
    public int process(List<Update> list) {
        updateHubService.process(list, telegramBot);
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

}
