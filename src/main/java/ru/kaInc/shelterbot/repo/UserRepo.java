package ru.kaInc.shelterbot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kaInc.shelterbot.model.User;

public interface UserRepo extends JpaRepository <User, Long> {

}
