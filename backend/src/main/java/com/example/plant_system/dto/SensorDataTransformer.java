package com.example.plant_system.dto;

import com.example.plant_system.model.Plant;
import com.example.plant_system.model.SensorData;

public class SensorDataTransformer {

    public static SensorData convertToEntity(SensorDataDto sensorDataDto, Plant plant){
        SensorData sensorData = new SensorData();
        sensorData.setId(sensorDataDto.getId());
        sensorData.setTemperature(sensorDataDto.getTemperature());
        sensorData.setHumidity(sensorDataDto.getHumidity());
        sensorData.setDs18b20Temperature(sensorDataDto.getDs18b20Temperature());
        sensorData.setPhotoresistorValue(sensorDataDto.getPhotoresistorValue());
        sensorData.setSoilMoisture(sensorDataDto.getSoilMoisture());
        sensorData.setDataTime(sensorDataDto.getDataTime());
        sensorData.setPlant(plant);
        return sensorData;
    }

    public static SensorDataDto convertToDto(SensorData sensorData){
        return new SensorDataDto(
                sensorData.getId(),
                sensorData.getTemperature(),
                sensorData.getHumidity(),
                sensorData.getDs18b20Temperature(),
                sensorData.getSoilMoisture(),
                sensorData.getPhotoresistorValue(),
                sensorData.getDataTime());
            }
}
