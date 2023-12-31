package ru.kaInc.shelterbot.model;

import jakarta.persistence.*;
import lombok.Data;
import ru.kaInc.shelterbot.model.enums.Callback;

import java.util.Objects;

@Entity
@Data
@Table(name = "infos")
public class Info {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "command_info")
    private Callback command;

    @Column(name = "info")
    private String text;

    @ManyToOne
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Info info)) return false;
        return Objects.equals(id, info.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
