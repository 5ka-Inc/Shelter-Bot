package ru.kaInc.shelterbot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kaInc.shelterbot.model.User;
import ru.kaInc.shelterbot.model.enums.Role;

import java.util.List;

/**
 * The UserRepo interface extends JpaRepository for User entities and provides custom query methods for accessing and managing user data in the database.
 */
public interface UserRepo extends JpaRepository<User, Long> {

    /**
     * Retrieves a list of users with a specific role from the database.
     *
     * @param role The role of users to filter by (e.g., "volunteer," "admin").
     * @return A list of User objects that match the specified role.
     */
    List<User> findUsersByRole(Role role);


}
