package ru.kaInc.shelterbot.model.enums;

import java.util.List;

public enum Callback {

    ADDRESS("Адрес"),

    TIME("Рабочее время"),

    DOCUMENTS("Необходимые документы"),

    INTRODUCTION("Знакомство с животным"),
    KINOLOG("Советы кинолога"),

    INFO_SHELTER("Информация о приюте", List.of(ADDRESS, TIME)),
    INFO_ADOPTION("Информация об усыновлении", List.of(DOCUMENTS, INTRODUCTION)),


    DEFAULT_MENU("Главное меню", List.of(INFO_ADOPTION, INFO_SHELTER)),


    ;

    private String text;
    private List<Callback> nextMenu;


    Callback(String string) {
        this.text = string;
    }


    Callback(String string, List<Callback> list) {
        this.text = string;
        this.nextMenu = list;

    }

    public String getText() {
        return text;
    }


    public List<Callback> getNextMenu() {
        return nextMenu;
    }
}
