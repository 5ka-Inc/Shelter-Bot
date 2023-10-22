package ru.kaInc.shelterbot.model;

import jakarta.persistence.*;
import lombok.Data;
import ru.kaInc.shelterbot.model.enums.Role;

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
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;

    @ManyToOne()
    @JoinColumn(name = "volunteer_id")
    private Volunteer volunteer;

    @Column(name = "is_adopter")
    private Boolean isAdopter;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    public User() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
