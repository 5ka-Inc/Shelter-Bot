package ru.kaInc.shelterbot.handler;

import com.pengrad.telegrambot.model.Update;
import org.apache.log4j.Logger;
import ru.kaInc.shelterbot.bot.Bot;
import ru.kaInc.shelterbot.command.Command;
import ru.kaInc.shelterbot.command.ParsedCommand;

public class SystemHandler extends AbstractHandler {
    private static final Logger log = Logger.getLogger(SystemHandler.class);
    private final String END_LINE = "\n";

    public SystemHandler(Bot bot) {
        super(bot);
    }

    @Override
    public String operate(String chatId, ParsedCommand parsedCommand, Update update) {
        Command command = parsedCommand.getCommand();

        switch (command) {
            case START:
                bot.sendQueue.add(getMessageStart(chatId));
                break;
            case HELP:
                bot.sendQueue.add(getMessageHelp(chatId));
                break;
            case ID:
                return "Your telegramID: " + update.message().from().id();
        }
        return "";
    }
}
