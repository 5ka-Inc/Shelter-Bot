package ru.kaInc.shelterbot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * The Report class represents a report entity in the database. It stores information about a report,
 * including its date, diet, health, behavior, validity, associated photo, and the user who created the report.
 */
@Entity
@Data
@NoArgsConstructor
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "diet")
    private String diet;

    @Column(name = "health")
    private String health;

    @Column(name = "behavior")
    private String behavior;

    @Column(name = "is_report_valid")
    private boolean isReportValid;

    @OneToOne(mappedBy = "report")
    @JoinColumn(name = "photo_id")
    @JsonIgnore
    private Photo photo;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
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
