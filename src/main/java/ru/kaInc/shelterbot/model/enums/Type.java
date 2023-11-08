package ru.kaInc.shelterbot.model.enums;

import java.util.ArrayList;
import java.util.List;



/**
 * The Type enum represents different types of entities in the system. It defines two types: "CAT" and "DOG".
 */
public enum Type {

    /**
     * The "CAT" type represents an entity related to cats.
     */
    CAT(List.of("Знакомство", "Документы", "Рекомендации по транспортировке", "Рекомендации по дому", "Рекомендации по дому для калича", "Причины отказа", "Отправить контакты")),
    /**
     * The "DOG" type represents an entity related to dogs.
     */
    DOG(List.of("Знакомство", "Документы", "Рекомендации по транспортировке", "Рекомендации по дому", "Рекомендации по дому для калича", "Советы кинолога", "Проверенные кинологи", "Причины отказа", "Отправить контакты")),
    ;

    Type(List<String> buttons) {
    }
}
