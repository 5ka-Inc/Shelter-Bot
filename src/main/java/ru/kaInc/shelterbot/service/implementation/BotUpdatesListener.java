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

@Component
public class BotUpdatesListener implements UpdatesListener {
    @Autowired
    UpdateHubServiceImpl updateHubService;
    private final Logger logger = LoggerFactory.getLogger(BotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

    @PostConstruct
    public void initialize() {
        telegramBot.setUpdatesListener(this);
        logger.info("Bot's updateListener is set");
    }

    @Override
    public int process(List<Update> list) {
        updateHubService.process(list, telegramBot);
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

}
