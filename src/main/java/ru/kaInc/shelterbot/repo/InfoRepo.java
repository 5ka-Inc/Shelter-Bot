package ru.kaInc.shelterbot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kaInc.shelterbot.model.Info;


import java.util.List;

public interface InfoRepo extends JpaRepository<Info, Long> {

    List<Info> findInfoByShelterType (String type);
}
