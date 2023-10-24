package ru.kaInc.shelterbot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kaInc.shelterbot.model.User;
import ru.kaInc.shelterbot.model.enums.Role;

import java.util.List;


public interface UserRepo extends JpaRepository <User, Long> {

    List<User> findUsersByRole(Role role);


}
