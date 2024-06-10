package com.example.plant_system.controller;

import com.example.plant_system.dto.AggregatedSensorDataDto;
import com.example.plant_system.service.PlantService;
import com.example.plant_system.service.SensorDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class SensorDataController {

    @Autowired
    private SensorDataService sensorDataService;

    @Autowired
    private PlantService plantService;

    @GetMapping("/sensor-data")
    public List<AggregatedSensorDataDto> getSensorData(
            @RequestParam Long plantId,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam String interval) {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate startLocalDate = LocalDate.parse(startDate, dateFormatter);
        LocalDateTime start = startLocalDate.atStartOfDay();
        LocalDate endLocalDate = LocalDate.parse(endDate, dateFormatter);
        LocalDateTime end = endLocalDate.atTime(LocalTime.MAX);
        return sensorDataService.getAggregatedSensorData(plantService.readById(plantId), start, end, interval);
    }
}
