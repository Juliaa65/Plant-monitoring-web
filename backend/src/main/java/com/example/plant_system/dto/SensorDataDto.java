package com.example.plant_system.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SensorDataDto {

    private Long id;

    @JsonProperty("temperature")
    private Double temperature;

    @JsonProperty("humidity")
    private Double humidity;

    @JsonProperty("ds18b20Temperature")
    private Double ds18b20Temperature;

    @JsonProperty("soilMoisture")
    private Double soilMoisture;

    @JsonProperty("photoresistorValue")
    private Integer photoresistorValue;

    @JsonProperty("dataTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataTime;


}
