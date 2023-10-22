package ru.kaInc.shelterbot.service.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.kaInc.shelterbot.model.User;
import ru.kaInc.shelterbot.repo.UserRepo;
import ru.kaInc.shelterbot.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    UserRepo userRepo;

    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public User addNewUser(Long id, long chatId, String name) {
        logger.debug("Creating {} {}", name, id);

        User user = new User();

        user.setId(id);
        user.setName(name);
        user.setChatId(chatId);
        user.setIsAdopter(false);

        return userRepo.save(user);
    }

    @Override
    public User addNewUser(com.pengrad.telegrambot.model.User user, Long chatId) {
        logger.debug("Creating {} {}", user.username(), user.id());

        User newUser = new User();

        newUser.setId(user.id());
        newUser.setName(user.username());
        newUser.setChatId(chatId);
        newUser.setIsAdopter(false);


        return userRepo.save(newUser);
    }

    @Override
    public boolean isUserPresent(Long id) {
        return userRepo.existsById(id);
    }
}
