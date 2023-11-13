package ru.kaInc.shelterbot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kaInc.shelterbot.model.Photo;

import java.util.List;

@Repository
public interface PhotoRepo extends JpaRepository<Photo, Long> {
}
