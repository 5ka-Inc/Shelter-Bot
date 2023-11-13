package ru.kaInc.shelterbot.service.implementation;

import jakarta.persistence.EntityNotFoundException;
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

    private final InfoRepo infoRepo;

    @Override
    public List<Info> findInfoByShelterType(String type) {
        return null;
    }

   public Info get(Long id) {
       return infoRepo.findById(id).orElseThrow(EntityNotFoundException::new);
   }
}
