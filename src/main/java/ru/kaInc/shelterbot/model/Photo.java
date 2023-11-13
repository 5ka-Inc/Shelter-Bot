package ru.kaInc.shelterbot.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;

/**
 * The Photo class represents a photo entity in the database. It is used to store binary image data associated with a report.
 */
@Entity
@Data
@Table(name = "photo")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "media_type")
    private String mediaType;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_id")
    private String fileId; // Добавлено поле для хранения file_id от Telegram

    @Lob
    @Column(name = "data")
    private byte[] data;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Photo photo = (Photo) o;
        return Objects.equals(id, photo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
