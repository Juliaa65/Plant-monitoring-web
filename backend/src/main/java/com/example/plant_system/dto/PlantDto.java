package com.example.plant_system.dto;

import com.example.plant_system.model.Photo;
import com.example.plant_system.model.Plant;
import com.example.plant_system.model.SensorData;
import com.example.plant_system.model.User;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PlantDto {

    private Long id;

    @NotBlank
    private String plantName;

    @NotBlank
    private String type;

    @NotBlank
    private String info;

    private String system_url;

    private String camera_url;

    private Long ownerId;

    private String location;

    private LocalDateTime plantDate;

    private List<SensorDataDto> sensorDataList;

    private List<PhotoDto> photoList;

}
