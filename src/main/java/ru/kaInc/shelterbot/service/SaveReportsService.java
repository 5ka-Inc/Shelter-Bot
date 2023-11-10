package ru.kaInc.shelterbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Service;
import ru.kaInc.shelterbot.model.Photo;

import java.io.IOException;
import java.util.List;

@Service
public interface SaveReportsService {
    void saveReports(List<Update> updates, TelegramBot bot) throws IOException;

    Photo savePhoto(List<Update> updates, TelegramBot bot) throws IOException;
}
