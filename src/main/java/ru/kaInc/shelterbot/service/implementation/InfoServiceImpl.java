package ru.kaInc.shelterbot.service.implementation;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.kaInc.shelterbot.model.Info;
import ru.kaInc.shelterbot.repo.InfoRepo;
import ru.kaInc.shelterbot.service.InfoService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InfoServiceImpl implements InfoService {

    private final Logger logger = LoggerFactory.getLogger(InfoServiceImpl.class);

    InfoRepo infoRepo;

    @Override
    public List<Info> findInfoByShelterType(String type) {
        return null;
    }

    public boolean isInfoPresent(Long id) {
        return infoRepo.existsById(id);
    }


}
