package com.example.plant_system.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "photos")
@ToString
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "path")
    private String path;

    @Column(name = "datatime")
    private LocalDateTime dataTime;

    @ManyToOne
    @JoinColumn(name = "plant_id")
    private Plant plant;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Photo photo = (Photo) o;
        return Objects.equals(id, photo.id) && Objects.equals(name, photo.name) && Objects.equals(path, photo.path) && Objects.equals(dataTime, photo.dataTime) && Objects.equals(plant, photo.plant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, path, dataTime, plant);
    }
}
