package ru.kaInc.shelterbot.service.implementation;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.kaInc.shelterbot.model.Photo;
import ru.kaInc.shelterbot.model.Report;
import ru.kaInc.shelterbot.model.Ticket;
import ru.kaInc.shelterbot.model.User;
import ru.kaInc.shelterbot.service.*;
import ru.kaInc.shelterbot.service.implementation.keyboard.UniversalKeyboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The UpdateHubServiceImpl class is an implementation of the UpdateHubService interface and is responsible for processing updates and managing user interactions in the bot's system.
 */
@Service
public class UpdateHubServiceImpl implements UpdateHubService {

    UserService userService;
    UniversalKeyboard universalKeyboard;
    TicketService ticketService;
    ReportService reportService;
    PhotoService photoService;
    private final Logger logger = LoggerFactory.getLogger(UpdateHubServiceImpl.class);

    /**
     * Хранение ID чата, который ожидает ввода описания проблемы
     */
    private final Map<Long, Boolean> awaitingDescription = new HashMap<>();

    /**
     * Хранение состояния создания отчёта для каждого пользователя
     */
    private final Map<Long, Report> reportCreationState = new HashMap<>();


    /**
     * Constructor for creating an instance of UpdateHubServiceImpl with UserService and KeyboardBasic dependencies.
     *
     * @param userService The UserService used for managing user-related operations.
     */

    public UpdateHubServiceImpl(UserService userService, UniversalKeyboard universalKeyboard, TicketService ticketService) {
        this.userService = userService;
        this.universalKeyboard = universalKeyboard;
        this.ticketService = ticketService;
    }

    /**
     * Processes a list of Telegram updates, checks for the "/start" command, and calls relevant methods based on the update content.
     *
     * @param updates     A list of Telegram Update objects representing user interactions.
     * @param telegramBot The TelegramBot instance responsible for processing updates and sending responses.
     */
    public void process(List<Update> updates, TelegramBot telegramBot) {
        if (updates == null || updates.isEmpty()) {
            logger.warn("Updates is null or empty");
            return;
        }
        updates.forEach(update -> {
            if (update.callbackQuery() != null) {
                String callbackData = update.callbackQuery().data();
                if ("CALL_VOLUNTEER".equals(callbackData)) {
                    processCallVolunteer(update, telegramBot);
                } else if ("SEND_REPORT".equals(callbackData)) {
                    startReportCreation(update.callbackQuery().message().chat().id(), telegramBot);
                } else {
                    universalKeyboard.process(updates, telegramBot);
                }
            } else if (update.message() != null && update.message().text() != null) {
                Long chatId = update.message().chat().id();
                if (awaitingDescription.getOrDefault(chatId, false)) {
                    processCallVolunteer(update, telegramBot);
                } else if (reportCreationState.containsKey(chatId)) {
                    continueReportCreation(update, telegramBot);
                } else {
                    processStart(update, updates, telegramBot);
                }
            } else {
                logger.debug("Received an update that is not a message or callbackQuery");
            }
        });
    }

    /**
     * Processes the "start" command in a user interaction, calling addUserIfNew and handling keyboard interactions.
     *
     * @param update      The Telegram Update object representing a user's interaction.
     * @param updates     A list of updates to be processed.
     * @param telegramBot The TelegramBot instance responsible for handling updates and sending responses.
     */
    @Override
    public void processStart(Update update, List<Update> updates, TelegramBot telegramBot) {
        if (update.message().text().equals("/start")) {
            universalKeyboard.process(updates, telegramBot);
        } else {
            SendMessage message = new SendMessage(update.message().chat().id(), "Айнц - цвай - драй - ничего не панимай"); // ВЕРНУТЬ В СТАТИЧЕСКУЮ ПЕРЕМЕННУЮ
            telegramBot.execute(message);
            universalKeyboard.process(updates, telegramBot);
        }
    }

    @Override
    public void processCallVolunteer(Update update, TelegramBot telegramBot) {
        logger.info("Method processCallVolunteer was invoked");
        if (update.callbackQuery() != null && "CALL_VOLUNTEER".equals(update.callbackQuery().data())) {
            Long chatId = update.callbackQuery().message().chat().id();

            // Помечаем, что ожидаем описание проблемы от этого пользователя
            awaitingDescription.put(chatId, true);
            logger.info("awaiting description true chat_id {}", chatId);

            telegramBot.execute(new SendMessage(chatId, "Опишите ваш вопрос и измените настройки приватности, чтобы волонтер смог написать вам:"));
        } else if (update.message() != null && awaitingDescription.getOrDefault(update.message().chat().id(), false)) {
            Long chatId = update.message().chat().id();
            String description = update.message().text();
            String username = update.message().chat().username();

            if (description.length() < 10) {
                logger.warn("description {} too short", description);
                telegramBot.execute(new SendMessage(chatId, "Описание вашей проблемы слишком короткое. Пожалуйста, опишите подробнее (минимум 10 символов)."));
            } else {
                Ticket ticket = ticketService.createTicket(description, username);
                sendTicketToVolunteer(ticket, telegramBot);
                logger.info("ticket {} sent to available volunteer", ticket.getIssueDescription());
                awaitingDescription.remove(chatId);
                telegramBot.execute(new SendMessage(chatId, "Ваш запрос отправлен! Свободный волонтер свяжется с вами в ближайшее время"));
            }
        }
    }

    private void sendTicketToVolunteer(Ticket ticket, TelegramBot telegramBot) {
        String messageText = "Новый тикет получен: " + String.format(" '%s' ", ticket.getIssueDescription()) + ". \nПользователь: @" + ticket.getCreatorsUsername();
        telegramBot.execute(new SendMessage(ticket.getVolunteer().getChatId(), messageText));
    }

    private void startReportCreation(Long chatId, TelegramBot telegramBot) {
        // Создание нового объекта отчёта и сохранение его в состоянии
        Report report = new Report();
        report.setUser(new User(chatId));
        reportCreationState.put(chatId, report);

        // Запросить информацию о диете
        sendMessage(chatId, "Пожалуйста, введите информацию о диете:", telegramBot);
    }

    private void continueReportCreation(Update update, TelegramBot telegramBot) {
        Long chatId = update.message().chat().id();
        Report report = reportCreationState.get(chatId);

        // Проверяем, какая информация уже заполнена
        if (report.getDiet() == null) {
            report.setDiet(update.message().text());
            sendMessage(chatId, "Пожалуйста, введите информацию о здоровье:", telegramBot);
        } else if (report.getHealth() == null) {
            report.setHealth(update.message().text());
            sendMessage(chatId, "Пожалуйста, введите информацию о поведении:", telegramBot);
        } else if (report.getBehavior() == null) {
            report.setBehavior(update.message().text());
            // После заполнения всех текстовых полей запросить фото
            sendMessage(chatId, "Пожалуйста, загрузите фотографию:", telegramBot);
        } else if (update.message().photo() != null) {
            // Обработка получения фотографии и сохранение в БД
            Photo photo = new Photo(); // Предполагается, что класс Photo существует
            photo.setFileId(update.message().photo()[0].fileId()); // Сохраняем ID первого фото из массива
            report.setPhoto(photo);

            try {
                photoService.savePhoto(update, report);
                reportService.createReport(report);
                sendMessage(chatId, "Ваш отчёт сохранён и отправлен на проверку волонтёрам.", telegramBot);
            } catch (Exception e) {
                sendMessage(chatId, "Произошла ошибка при сохранении отчёта: " + e.getMessage(), telegramBot);
            }

            Ticket ticket = ticketService.createTicket("Отправлен новый отчет", update.message().chat().username());
            sendTicketToVolunteer(ticket, telegramBot);
            logger.info("ticket {} sent to available volunteer", ticket.getIssueDescription());
            // Очистка состояния после создания отчёта
            reportCreationState.remove(chatId);
        } else {
            sendMessage(chatId, "Не удалось распознать ввод, попробуйте снова.", telegramBot);
        }
    }

    private void sendMessage(Long chatId, String text, TelegramBot telegramBot) {
        SendMessage message = new SendMessage(chatId, text);
        telegramBot.execute(message);
    }

    /**
     * Checks if the user associated with the given Update is present in the bot's database and adds them if not.
     *
     * @param update The Telegram Update object representing a user's interaction.
     */
    @Override
    public void addUserIfNew(Update update) {
        if (update == null) {
            logger.warn("Got null update in {}", Thread.currentThread().getStackTrace()[2].getMethodName());
            return;
        }
        if (update.message().from() == null) {
            logger.warn("Got null user in {}", Thread.currentThread().getStackTrace()[2].getMethodName());
            return;
        }

        if (!userService.isUserPresent(update.message().from().id())) {
            createNewUSer(update.message().from(), update.message().chat().id());
        }
    }

    /**
     * Creates a new user in the bot's database with the specified user information.
     *
     * @param id     The unique Telegram user ID to be used as the user's identifier in the bot's system.
     * @param chatId The ID of the chat associated with this user.
     * @param name   The name of the user, which can be the default Telegram username or a real name.
     * @return The created User object with a unique identifier.
     */
    @Override
    public User createNewUser(Long id, Long chatId, String name) {
        User newUser = userService.addNewUser(id, chatId, name);
        logger.info("Added {} {}", newUser.getName(), newUser.getId());
        return newUser;

    }

    /**
     * Creates a new user in the bot's database based on a Telegram User object and the associated chat ID.
     *
     * @param user   The Telegram User object from which the user's ID and username will be extracted and used.
     * @param chatId The ID of the chat associated with this user.
     * @return The created User object with a unique identifier.
     */
    @Override
    public User createNewUSer(com.pengrad.telegrambot.model.User user, Long chatId) {
        User newUser = userService.addNewUser(user, chatId);
        logger.info("Added {} {}", newUser.getName(), newUser.getId());
        return newUser;
    }
}