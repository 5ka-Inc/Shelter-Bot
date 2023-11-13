package ru.kaInc.shelterbot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Сущность тикет может существовать автономно без связей с другими сущностями и таблицами.
 * ДЛЯ СОЗДАНИЯ ТИКЕТА ДОЛЖЕН БЫТЬ СОЗДАН ПОЛЬЗОВАТЕЛЬ!
 * Нужна для рагистрации запросов на поддержку от пользователей.
 * На данном этапе выбрана реализация дилога с поддержкой в виде директ-чата. Волонтеру просто назначается 1 из тикетов и приходит никнейм пользователя, которому нужно написать.
 */
@Entity
@Data
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "creation_time")
    private LocalDateTime creationTime;

    @Column(name = "received_time")
    private LocalDateTime receivedByVolunteerTime;

    @Column(name = "username")
    private String creatorsUsername;

    @Column(name = "issue_description")
    private String issueDescription;

    @Column(name = "is_closed")
    private Boolean isClosed;

    /**
     * Поля пользователь и волонтер, являясь полями одного класса нужны для удобства отслеживания тикетов у волонтеров и пользователей. Технически можно обойтись без них.
     *
     */
    @ManyToOne()
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "volunteer_id")
    @JsonIgnore
    private User volunteer;

    /**
     * Фиксирует время и пользователя, создавшего тикет.
     */
    @PostConstruct
    private void init() {
        this.creationTime = LocalDateTime.now();
        this.creatorsUsername = this.user.getUsername();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(id, ticket.id);
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
