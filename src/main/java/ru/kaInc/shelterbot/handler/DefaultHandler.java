package ru.kaInc.shelterbot.handler;

import com.pengrad.telegrambot.model.Update;
import ru.kaInc.shelterbot.bot.Bot;
import ru.kaInc.shelterbot.command.ParsedCommand;
import org.apache.log4j.Logger;

public class DefaultHandler extends AbstractHandler{

    private static final Logger log = Logger.getLogger(DefaultHandler.class);

    public DefaultHandler(Bot bot) {
        super(bot);
    }

    @Override
    public String operate(String chatId, ParsedCommand parsedCommand, Update update) {
        return "";
    }
}
