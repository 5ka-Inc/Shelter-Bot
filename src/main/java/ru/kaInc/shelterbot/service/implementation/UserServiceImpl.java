package ru.kaInc.shelterbot.service.implementation;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.kaInc.shelterbot.model.User;
import ru.kaInc.shelterbot.model.enums.Role;
import ru.kaInc.shelterbot.repo.UserRepo;
import ru.kaInc.shelterbot.service.UserService;
import java.util.List;
/**
 * The UserServiceImpl class is an implementation of the UserService interface and is responsible for managing user-related operations in the bot's system.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepo userRepo;

    /**
     * Creates a new user in the bot's database with the specified user details.
     *
     * @param id     The unique Telegram user ID to be used as the user's identifier in the bot's system.
     * @param chatId The ID of the chat associated with this user.
     * @param name   The name of the user, which can be the default Telegram username or a real name.
     * @return The created User object with a unique identifier.
     */
    @Override
    public User addNewUser(Long id, Long chatId, String name) {
        logger.debug("Creating {} {}", name, id);
        if (id == null || chatId == null) {
            logger.error("Id and chat id must not be null! Aborting.");
            return null;
        }
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setChatId(chatId);
        user.setIsAdopter(false);
        user.setRole(Role.USER);
        return userRepo.save(user);
    }
    /**
     * Creates a new user in the bot's database based on a Telegram User object and the associated chat ID.
     *
     * @param user   The Telegram User object from which the user's ID and username will be extracted and used.
     * @param chatId The ID of the chat associated with this user.
     * @return The created User object with a unique identifier.
     */
    @Override
    public User addNewUser(com.pengrad.telegrambot.model.User user, Long chatId) {
        logger.debug("Creating {} {}", user.username(), user.id());
        if (chatId == null || user.id() == null) {
            logger.error("Id and chat id must not be null! Aborting.");
            return null;
        }
        User newUser = new User();
        newUser.setId(user.id());
        newUser.setName(user.username());
        newUser.setChatId(chatId);
        newUser.setIsAdopter(false);
        newUser.setRole(Role.USER);
        return userRepo.save(newUser);
    }
    /**
     * Checks if a user with the specified ID already exists in the bot's database.
     *
     * @param id The unique Telegram user ID to be checked.
     * @return true if the user is found in the database, false otherwise.
     */
    @Override
    public boolean isUserPresent(Long id) {
        return userRepo.existsById(id);
    }
    /**
     * Retrieves a user from the bot's database based on their unique identifier.
     *
     * @param id The unique identifier of the user to be retrieved.
     * @return The User object associated with the specified identifier.
     * @throws EntityNotFoundException if the user is not found.
     */
    @Override
    public User findById(Long id) {
        return userRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id %s not found", id)));
    }
    /**
     * Retrieves a list of users based on their role.
     *
     * @param role The role of users to filter by (e.g., "volunteer," "admin").
     * @return A list of User objects that match the specified role.
     * @throws EntityNotFoundException if no users with the specified role are found.
     */
    @Override
    public List<User> findUsersByRole(Role role) {
        List<User> foundUsers = userRepo.findUsersByRole(role);
        if (foundUsers.isEmpty()) {
            throw new EntityNotFoundException(String.format("Not found users with role %s", role));
        }
        return foundUsers;
    }
    /**
     * Retrieves a list of all users available in the bot's system.
     *
     * @return A list of User objects representing all the users in the database.
     */
    @Override
    public List<User> findAll() {
        return userRepo.findAll();
    }
    /**
     * Updates the information of an existing user in the bot's database.
     *
     * @param user The User object with modified details to be updated in the database.
     * @return The updated User object.
     */
    @Override
    public User updateUser(User user) {
        User foundUser = findById(user.getId());
        foundUser.setName(user.getName());
        foundUser.setPhone(user.getPhone());
        foundUser.setRole(user.getRole());
        return userRepo.save(foundUser);
    }

    /**
     * Логика поиска волонтера с наименьшим количеством тикетов
     * @return объект пользователя с ролью VOLUNTEER
     */
    @Override
    public User findAvailableVolunteer() {
        List<User> volunteers = userRepo.findLeastBusyVolunteers(Role.VOLUNTEER, PageRequest.of(0, 1));
        return volunteers.isEmpty() ? null : volunteers.get(0);
    }

    @Override
    public User findByChatId(Long chatId) {
        User foundUser = userRepo.findByChatId(chatId);
        if (foundUser == null) {
            throw new EntityNotFoundException();
        }
        return foundUser;
    }


    /**
     * Deletes a user from the bot's database based on their unique identifier.
     *
     * @param id The unique identifier of the user to be deleted.
     * @throws EntityNotFoundException if the user is not found.
     */
    @Override
    public void deleteUser(Long id) {
        if (findById(id) == null) {
            throw new EntityNotFoundException(String.format("User with id %s not found", id));
        }
        userRepo.deleteById(id);
    }

    @Override
    public User save(User user) {
        return userRepo.save(user);
    }
}