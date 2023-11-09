package ru.kaInc.shelterbot.model;

import jakarta.persistence.*;
import lombok.Data;
import ru.kaInc.shelterbot.model.enums.InfoCommand;

import java.util.Objects;

@Entity
@Data
@Table(name = "info")
public class Info {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "command_info")
    private InfoCommand command;

    @Column(name = "info")
    private String infoCommand;

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
