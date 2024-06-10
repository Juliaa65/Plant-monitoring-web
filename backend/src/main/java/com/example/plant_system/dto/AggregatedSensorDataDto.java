package com.example.plant_system.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AggregatedSensorDataDto {

    @JsonProperty("timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    @JsonProperty("avgTemperature")
    private Double avgTemperature;

    @JsonProperty("avgHumidity")
    private Double avgHumidity;

    @JsonProperty("avgDs18b20Temperature")
    private Double avgDs18b20Temperature;

    @JsonProperty("avgSoilMoisture")
    private Double avgSoilMoisture;

    @JsonProperty("avgPhotoresistorValue")
    private Double avgPhotoresistorValue;

    public AggregatedSensorDataDto(LocalDateTime timestamp, Double avgTemperature, Double avgHumidity, Double avgDs18b20Temperature, Double avgSoilMoisture, Double avgPhotoresistorValue) {
        this.timestamp = timestamp;
        this.avgTemperature = avgTemperature;
        this.avgHumidity = avgHumidity;
        this.avgDs18b20Temperature = avgDs18b20Temperature;
        this.avgSoilMoisture = avgSoilMoisture;
        this.avgPhotoresistorValue = avgPhotoresistorValue;
    }

}
