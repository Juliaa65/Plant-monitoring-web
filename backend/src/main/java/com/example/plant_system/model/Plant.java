package com.example.plant_system.model;

import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "plants")
@ToString
public class Plant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "info")
    private String info;

    @Column(name = "system_url")
    private String system_url;

    @Column(name = "camera_url")
    private String camera_url;

    @Column(name = "location")
    private String location;

    @Column(name = "plantdate")
    private LocalDateTime plantDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    @OneToMany(mappedBy = "plant", cascade = CascadeType.REMOVE)
    private List<SensorData> sensorData;

    @OneToMany(mappedBy = "plant", cascade = CascadeType.REMOVE)
    private List<Photo> photos;

    @OneToMany(mappedBy = "plant", cascade = CascadeType.REMOVE)
    private List<Note> notes;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plant plant = (Plant) o;
        return Objects.equals(id, plant.id) && Objects.equals(name, plant.name) && Objects.equals(type, plant.type) && Objects.equals(info, plant.info) && Objects.equals(system_url, plant.system_url) && Objects.equals(camera_url, plant.camera_url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, info, system_url, camera_url);
    }
}
