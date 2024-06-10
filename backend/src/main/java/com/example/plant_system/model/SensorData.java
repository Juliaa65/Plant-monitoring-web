package com.example.plant_system.model;


import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "sensors_data")
public class SensorData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "temperature")
    private Double temperature;

    @Column(name = "humidity")
    private Double humidity;

    @Column(name="ds18b20temperature")
    private Double ds18b20Temperature;

    @Column(name = "soilmoisture")
    private Double soilMoisture;

    @Column(name="photoresistorvalue")
    private Integer photoresistorValue;

    @Column(name = "datatime")
    private LocalDateTime dataTime;

    @ManyToOne
    @JoinColumn(name = "plant_id")
    private Plant plant;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SensorData that = (SensorData) o;
        return Objects.equals(id, that.id) && Objects.equals(temperature, that.temperature) && Objects.equals(humidity, that.humidity) && Objects.equals(ds18b20Temperature, that.ds18b20Temperature) && Objects.equals(soilMoisture, that.soilMoisture) && Objects.equals(photoresistorValue, that.photoresistorValue) && Objects.equals(dataTime, that.dataTime) && Objects.equals(plant, that.plant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, temperature, humidity, ds18b20Temperature, soilMoisture, photoresistorValue, dataTime, plant);
    }
}
