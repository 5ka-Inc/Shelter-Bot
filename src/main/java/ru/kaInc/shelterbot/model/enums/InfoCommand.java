package ru.kaInc.shelterbot.model.enums;

public enum InfoCommand {

    ANIMAL_INTRODUCTION("Правила знакомства с животным, до того как забрать его из приюта"),
    DOCUMENTS_REQUIRED("Список документов, необходимых для того, чтобы взять животное из приюта"),
    TRANSPORTATION("Список рекомендаций по транспортировке животного"),
    PUPPY_KITTEN("Список рекомендаций по обустройству дома для щенка/котёнка"),
    ADULT_PET("Список рекомендаций по обустройству дома для взрослого животного"),
    DISABLED_PET("Список рекомендаций по обустройству дома для животного с ограниченными возможностями (зрение, передвижение)"),
    TRAINING_TIPS("Советы кинолога по первичному общению с собакой"),
    DOG_TRAINERS("Рекомендации по проверенным кинологам для дальнейшего обращения к ним"),
    REASONS_FOR_DENIAL("Список причин, почему могут отказать и не дать забрать животное из приюта"),
    CONTACT_DETAILS("Принять и записать контактные данные для связи");


    private String infoCommand;

    InfoCommand(String infoCommand){
        this.infoCommand=infoCommand;
    }

    public String getInfoCommand() {
        return infoCommand;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
