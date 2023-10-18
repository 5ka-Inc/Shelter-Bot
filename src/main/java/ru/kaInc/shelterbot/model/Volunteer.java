package ru.kaInc.shelterbot.model;

import jakarta.persistence.*;
import lombok.Data;
import ru.kaInc.shelterbot.ShelterBotApplication;

import java.util.Objects;
import java.util.UUID;

@Entity
@Data
@Table(name = "volunteers")
public class Volunteer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "name")
    private String name;

    @ManyToOne()
    @Column(name = "shelter_id")
    private Shelter shelter;

    public Volunteer() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Shelter getShelterId() {
        return shelter;
    }

    public void setShelterId(Shelter shelterId) {
        this.shelter = shelterId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Volunteer volunteer = (Volunteer) o;
        return Objects.equals(id, volunteer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
