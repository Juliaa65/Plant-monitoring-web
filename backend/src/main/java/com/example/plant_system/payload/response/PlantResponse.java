package com.example.plant_system.payload.response;

import com.example.plant_system.dto.SensorDataDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@AllArgsConstructor
@Data
public class PlantResponse {
    private Long id;

    @NotBlank
    private String plantName;

    @NotBlank
    private String type;

    @NotBlank
    private String info;

    private String location;

    private String age;

    private String system_url;

    private String camera_url;

    private Long ownerId;

    private SensorDataDto sensorData;
}
