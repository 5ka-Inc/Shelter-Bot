package ru.kaInc.shelterbot.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kaInc.shelterbot.model.enums.Role;

import java.util.Objects;
import java.util.Set;

/**
 * The User class represents a user entity in the database. It stores information about a user,
 * including their unique identifier (id), chat ID, name, phone number, associated shelter, adopter status, and role.
 */
@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "name")
    private String name;

    @Column(name = "username")
    private String username;

    @Column(name = "phone")
    private String phone;

    @ManyToOne()
    @JoinColumn(name = "shelter_id")
    @JsonBackReference
    private Shelter shelter;

    @Column(name = "is_adopter")
    private Boolean isAdopter;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "volunteer")
    private Set<Ticket> VolTickets;

    @OneToMany(mappedBy = "user")
    private Set<Ticket> UsrTickets;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
