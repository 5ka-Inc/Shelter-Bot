package ru.kaInc.shelterbot.service;

import com.pengrad.telegrambot.model.Update;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.kaInc.shelterbot.service.implementation.BotUpdatesListener;

import java.util.ArrayList;

class BotUpdatesListenerTest {

    BotUpdatesListener botUpdatesListener = new BotUpdatesListener();

    @Test
    void throwsExceptionIfNullListReceived() {
        Assertions.assertThrows(NullPointerException.class, () -> botUpdatesListener.process(null));
    }

    @Test
    void throwsExceptionIfEmptyListReceived() {
        Assertions.assertThrows(NullPointerException.class, () -> botUpdatesListener.process(new ArrayList<Update>()));
    }



}