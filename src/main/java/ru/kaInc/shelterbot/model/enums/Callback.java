package ru.kaInc.shelterbot.model.enums;


import java.util.List;
import java.util.Map;

public enum Callback {

    /**
     * Здесь представлены все пункты меню (кнопки, если угодно).
     * Что бы добавить пункт достаточно создать его ENUM с названием в виде параметра, и добавить этот ENUM в лист колбеков меню, из которого должен вызываться этот пункт.
     * Если нужно создать пункт меню, который вызывает еще одно меню, то пункты следующего уровня должны быть в листе колбеков после названия пункта.
     */

    //Конечные пункты меню, после которых нет других меню:

    SEND_REPORT("Отправить отчёт"),
    ADDRESS("Адрес"),

    TIME("Рабочее время"),

    INTRODUCTION("Знакомство с животным"),
    KINOLOG("Советы кинолога"),

    PASSPORT("Паспорт"),
    SNILS("Снилс"),

    CERTIFICATE("Справка, что не дурак"),

    REGISTER("Срегистрация"),

    WHISCAS("Чек на вискас"),

    //Пункты меню с уникальными кнопками в зависимости от вида животного
    DOCUMENTS("Необходимые документы", List.of(PASSPORT, SNILS),
            Map.of(
                    Type.DOG, List.of(CERTIFICATE),
                    Type.CAT, List.of(WHISCAS))),


    // Пункты меню, вызывающие другие меню:

    INFO_SHELTER("Информация о приюте", List.of(ADDRESS, TIME)),
    INFO_ADOPTION("Информация об усыновлении", List.of(DOCUMENTS, INTRODUCTION)),

    CHOOSE_INFO("Что вы хотите узнать?", List.of(INFO_ADOPTION, INFO_SHELTER)),
    DEV("Dev_menu", List.of(REGISTER, SEND_REPORT)),


    // Пункты меню, отвечающие за выбор приюта:

    SHELTER_DOG("Собачий приют", List.of(INFO_SHELTER, INFO_ADOPTION), Type.DOG),
    SHELTER_CAT("Кошачий приют", List.of(INFO_SHELTER, INFO_ADOPTION), Type.CAT),


    DEFAULT_MENU("Выберите приют", List.of(SHELTER_DOG, SHELTER_CAT, DEV)),

    ;

    private final String text;
    private List<Callback> nextMenu;
    private Map<Type, List<Callback>> uniqueCallbacks;

    private Type shelter;


    public Map<Type, List<Callback>> getUniqueCallbacks() {
        if (uniqueCallbacks != null) {
            return uniqueCallbacks;
        }
        return null;
    }




    Callback(String string) {
        this.text = string;
    }


    public Type getShelter() {
        return shelter;
    }

    Callback(String string, List<Callback> list) {
        this.text = string;
        this.nextMenu = list;

    }

    Callback(String string, List<Callback> list, Type shelter) {
        this.text = string;
        this.shelter = shelter;
        this.nextMenu = list;

    }

    Callback(String string, List<Callback> list, Map<Type, List<Callback>> uniqueCallbacks) {
        this.text = string;
        this.nextMenu = list;
        this.uniqueCallbacks = uniqueCallbacks;

    }

    public String getText() {
        return text;
    }


    public List<Callback> getNextMenu() {
        if (this.nextMenu != null) {
            return nextMenu;
        } else
            return List.of(this);
    }
}
