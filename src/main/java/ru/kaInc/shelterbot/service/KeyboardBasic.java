package ru.kaInc.shelterbot.service;

/**
 * На основе этого интерфейса создаются все простые наборы кнопок(команд)
 * Возможно, будем делать все меню от этого интерфейса, а возможно сделаем ему более узконаправленных наследников
 */

public interface KeyboardBasic {
    void processCommands();

    void callVolunteer();
}
