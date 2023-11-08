package ru.kaInc.shelterbot.service;

import ru.kaInc.shelterbot.model.Info;

import java.util.List;

public interface InfoService {

    List<Info> findInfoByShelterType (String type);
}
