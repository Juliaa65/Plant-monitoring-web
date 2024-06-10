package com.example.plant_system.service;


import com.example.plant_system.dto.AggregatedSensorDataDto;
import com.example.plant_system.dto.SensorDataDto;
import com.example.plant_system.model.Plant;
import com.example.plant_system.model.SensorData;

import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.util.List;

public interface SensorDataService {

    SensorDataDto getSensorDataForPlant(Plant plant);

    List<SensorData> getAll();

    List<AggregatedSensorDataDto> getAggregatedSensorData(Plant plant, LocalDateTime start, LocalDateTime end, String interval);

}
