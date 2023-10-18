package ru.kaInc.shelterbot.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;
import java.util.UUID;

/**
 * @author Эпенди
 *
 */
@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @ManyToOne()
    @JoinColumn(name = "shelterId")
    private Shelter shelterId;

    @ManyToOne()
    @JoinColumn(name = "volunteerId")
    private Volunteer volunteerId;

    @Column(name = "isAdopter")
    private Boolean isAdopter;


    public User() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(chatId, user.chatId) && Objects.equals(name, user.name) && Objects.equals(phone, user.phone) && Objects.equals(shelter_id, user.shelter_id) && Objects.equals(volunteer_id, user.volunteer_id) && Objects.equals(isAdopter, user.isAdopter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatId, name, phone, shelter_id, volunteer_id, isAdopter);
    }
}
