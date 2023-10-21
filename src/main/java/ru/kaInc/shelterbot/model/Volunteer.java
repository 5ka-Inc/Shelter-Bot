package ru.kaInc.shelterbot.model;

import jakarta.persistence.*;
import lombok.Data;

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
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;

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
