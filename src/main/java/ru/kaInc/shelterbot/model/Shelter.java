package ru.kaInc.shelterbot.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kaInc.shelterbot.model.enums.ShelterInfo;
import ru.kaInc.shelterbot.model.enums.Type;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
/**
 * The Shelter class represents a shelter entity in the database. It stores information about a shelter,
 * including its name, type, and the set of users associated with the shelter.
 */
@Entity
@Data
@Table(name = "shelters")
public class Shelter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;


    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private Type type;

    @OneToMany(mappedBy = "shelter")
    @JsonManagedReference
    private Set<User> users;

    @ElementCollection
    @CollectionTable(name = "shelter_info", joinColumns = @JoinColumn(name = "shelter_id"))
    @MapKeyColumn(name = "property")
    @MapKeyEnumerated(EnumType.STRING)
    @Column(name = "info")
    private Map<ShelterInfo, String> properties;

    public Shelter() {
        properties = new EnumMap<>(ShelterInfo.class);
    }

    public String getShelterInfo(ShelterInfo info) {
        return properties.get(info);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shelter shelter = (Shelter) o;
        return Objects.equals(id, shelter.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
