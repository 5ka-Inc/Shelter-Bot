package ru.kaInc.shelterbot.handler;

import com.pengrad.telegrambot.model.Update;
import ru.kaInc.shelterbot.bot.Bot;
import ru.kaInc.shelterbot.command.ParsedCommand;

public abstract class AbstractHandler {

    Bot bot;

    public AbstractHandler(Bot bot) {
        this.bot = bot;
    }

    public abstract String operate(String chatId, ParsedCommand parsedCommand, Update update);
}
