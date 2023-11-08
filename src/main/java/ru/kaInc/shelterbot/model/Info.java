package ru.kaInc.shelterbot.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;

@Entity
@Data
@Table(name = "info")
public class Info {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "enum_info")
    private String enumInfo;

    @Column(name = "info")
    private String info;

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
