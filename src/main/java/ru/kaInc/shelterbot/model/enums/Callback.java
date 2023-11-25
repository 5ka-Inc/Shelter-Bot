package ru.kaInc.shelterbot.model.enums;



import java.util.List;
import java.util.Map;

public enum Callback {

    /**
     * Здесь представлены все пункты меню (кнопки, если угодно).
     * <p>
     * Что бы добавить пункт достаточно создать его колбек с названием в виде параметра, и добавить этот колбек в лист колбеков меню, из которого должен вызываться этот пункт.
     * Если нужно создать пункт меню, который вызывает еще одно меню, то пункты следующего уровня должны быть в листе колбеков после названия пункта.
     */

    START_COMMAND("Начало общения с ботом"),
    CALL_VOLUNTEER("Позвать волонтёра на помощь"),

    //Конечные пункты меню, после которых нет других меню:

    TRANSPORTATION("Транспортировка животного"),
    PUPPY_KITTEN("Обустройство дома для щенка/котёнка"),
    ADULT_PET("Обустройство дома для взрослого животного"),
    DISABLED_PET("Обустройство дома для животного с ограниченными возможностями (зрение, передвижение)"),

    REASONS_FOR_DENIAL("Почему могут отказать забрать животное из приюта"),
    CONTACT_DETAILS("Записать контактные данные для связи"),
    INVALID_REPORT("Дорогой усыновитель, мы заметили, что ты заполняешь отчет не так подробно, как необходимо." +
            " Пожалуйста, подойди ответственнее к этому занятию. В противном случае волонтеры приюта" +
            " будут обязаны самолично проверять условия содержания животного"),


    ADDRESS("Адрес"),
    TIME("Рабочее время"),
    ANIMAL_INTRODUCTION("Правила знакомства с животным"),
    PASSPORT("Паспорт"),
    SNILS("Снилс"),
    CERTIFICATE("Справка, что не дурак"),
    WHISCAS("Чек на вискас"),
    REPORT_INFO("В ежедневный отчёт входит следующая информация:" +
            "- фото животного," +
            "- рацион животного," +
            "- общее самочувствие и привыкание к новому месту," +
            "- изменение в поведении. Отказ от старых привычек и приобретение новых." +
            "Отчёт нужно присылать каждый день, ограничений в сутках по времени сдачи отчёта нет."),
    ADD_REPORT("Добавить отчёт"),

    TRAINING_TIPS("Советы кинолога по общению с собакой"),
    DOG_TRAINERS("Рекомендации по проверенным кинологам для дальнейшего обращения к ним"),

    //Пункты меню с уникальными кнопками в зависимости от вида животного

    DOCUMENTS_REQUIRED("Список документов для усыновления",
            List.of(PASSPORT, SNILS),

            Map.of(
                    Type.DOG, List.of(CERTIFICATE),
                    Type.CAT, List.of(WHISCAS))),

    INFO_ADOPTION("Информация об усыновлении",
            List.of(
                    DOCUMENTS_REQUIRED,
                    ANIMAL_INTRODUCTION,
                    TRANSPORTATION,
                    PUPPY_KITTEN,
                    ADULT_PET,
                    DISABLED_PET,
                    REASONS_FOR_DENIAL,
                    CONTACT_DETAILS),
            Map.of(Type.DOG, List.of(
                    TRAINING_TIPS,
                    DOG_TRAINERS
            ))),




    // Пункты меню, вызывающие другие меню:

    INFO_SHELTER("Информация о приюте", List.of(ADDRESS, TIME)),


    CHOOSE_INFO("Что вы хотите узнать?", List.of(INFO_ADOPTION, INFO_SHELTER)),
    SEND_REPORT("Отправить отчёт", List.of(REPORT_INFO, ADD_REPORT)),
    /*
     * Что бы добавить пункт достаточно создать его ENUM с названием в виде параметра, и добавить этот ENUM в лист
     * колбеков меню, из которого должен вызываться этот пункт.
     * Если нужно создать пункт меню, который вызывает еще одно меню, то пункты следующего уровня должны быть в листе
     * колбеков после названия пункта.
     */
    DEV("Я усыновитель", List.of(INFO_SHELTER, SEND_REPORT)),


    // Пункты меню, отвечающие за выбор приюта:

    SHELTER_DOG("Собачий приют", List.of(INFO_SHELTER, INFO_ADOPTION), Type.DOG),
    SHELTER_CAT("Кошачий приют", List.of(INFO_SHELTER, INFO_ADOPTION), Type.CAT),

    DEFAULT_MENU("Выберите приют", List.of(SHELTER_DOG, SHELTER_CAT, DEV));


    private final String text;
    private List<Callback> nextMenu;
    private Map<Type, List<Callback>> uniqueCallbacks;


    public Type getShelter() {
        return shelter;
    }

    public String getText() {
        return text;
    }

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

    Callback(String string, List<Callback> list) {
        this.text = string;
        this.nextMenu = list;
    }

    Callback(String string, Type shelter) {
        this.text = string;
        this.shelter = shelter;
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


    public List<Callback> getNextMenu() {
        if (this.nextMenu != null) {
            return nextMenu;
        } else
            return List.of(this);
    }
    }
