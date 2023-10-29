package ru.kaInc.shelterbot.handler;

import com.pengrad.telegrambot.model.Update;
import org.apache.log4j.Logger;
import ru.kaInc.shelterbot.ability.Notify;
import ru.kaInc.shelterbot.bot.Bot;
import ru.kaInc.shelterbot.command.ParsedCommand;

public class NotifyHandler extends AbstractHandler{
    private static final Logger log = Logger.getLogger(NotifyHandler.class);
    private final int MILLISEC_IN_SEC = 1000;
    private String WRONG_INPUT_MESSAGE = "Wrong input. Time must be specified as an integer greater than 0";

    public NotifyHandler(Bot bot) {
        super(bot);
    }

    @Override
    public String operate(String chatId, ParsedCommand parsedCommand, Update update) {
        String text = parsedCommand.getText();
        if ("".equals(text))
            return "You must specify the delay time. Like this:\n" +
                    "/notify 30";
        long timeInSec;
        try {
            timeInSec = Long.parseLong(text.trim());
        } catch (NumberFormatException e) {
            return WRONG_INPUT_MESSAGE;
        }
        if (timeInSec > 0) {
            Thread thread = new Thread(new Notify(bot, chatId, timeInSec * MILLISEC_IN_SEC));
            thread.start();
        } else return WRONG_INPUT_MESSAGE;
        return "";
    }
}
