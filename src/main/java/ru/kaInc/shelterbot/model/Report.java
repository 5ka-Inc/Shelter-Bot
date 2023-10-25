package ru.kaInc.shelterbot.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "reports")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "diet")
    private String diet;

    @Column(name = "health")
    private String health;

    @Column(name = "behavior")
    private String behavior;

    @Column(name = "is_report-valid")
    private boolean isReportValid;

    @OneToMany(mappedBy = "report")
    private Collection<Photo> photo;

    @ManyToOne
    @Column(name = "user_id")
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return Objects.equals(id, report.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
