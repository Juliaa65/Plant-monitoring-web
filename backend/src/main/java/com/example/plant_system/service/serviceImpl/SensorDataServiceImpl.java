package com.example.plant_system.service.serviceImpl;

import com.example.plant_system.dto.AggregatedSensorDataDto;
import com.example.plant_system.dto.SensorDataDto;
import com.example.plant_system.dto.SensorDataTransformer;
import com.example.plant_system.model.Plant;
import com.example.plant_system.model.SensorData;
import com.example.plant_system.repository.SensorsDataRepository;
import com.example.plant_system.service.PlantService;
import com.example.plant_system.service.SensorDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.ConnectException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class SensorDataServiceImpl implements SensorDataService {

    private final SensorsDataRepository sensorsDataRepository;

    private final RestTemplate restTemplate;

    @Autowired
    private PlantService plantService;

    private static final Logger logger = LoggerFactory.getLogger(SensorDataService.class);

    public SensorDataServiceImpl(SensorsDataRepository sensorsDataRepository) {
        this.sensorsDataRepository = sensorsDataRepository;
        this.restTemplate = new RestTemplate();
    }


    @Scheduled(fixedRate = 15 * 60 * 1000)
    public void fetchSensorDataForPlants() {
        List<Plant> plants = plantService.getAll();
        for (Plant plant : plants) {
            try {
                String sensorUrl = getValidURL(plant);
                SensorDataDto sensorDataDto = getSensorData(sensorUrl);
                if (sensorDataDto != null) {
                    sensorsDataRepository.save(SensorDataTransformer.convertToEntity(sensorDataDto, plant));
                }
            } catch (Exception e) {
                logger.error("Failed to fetch or save data for plant with ID: " + plant.getId(), e);
            }
        }
    }


    @Override
    public SensorDataDto getSensorDataForPlant(Plant plant) {
        String sensorUrl = getValidURL(plant);
        SensorDataDto sensorDataDto = getSensorData(sensorUrl);
        if (sensorDataDto != null) {
            sensorsDataRepository.save(SensorDataTransformer.convertToEntity(sensorDataDto, plant));
            return sensorDataDto;
        }
        return null;
    }

    public SensorDataDto getSensorData(String sensorUrl) {
        if (!sensorUrl.isBlank()) {
            SensorDataDto sensorDataDto = restTemplate.getForObject(sensorUrl, SensorDataDto.class);
            if (sensorDataDto != null) {
                sensorDataDto.setDataTime(LocalDateTime.now());
                return sensorDataDto;
            }
        }
        return null;
    }

    @Override
    public List<SensorData> getAll() {
        return sensorsDataRepository.findAll();
    }

    public String getValidURL(Plant plant) {
        String ip = plant.getSystem_url();
        if (ip != null && !ip.isBlank()) {
            return "http://" + ip + "/";
        }
        return "";
    }


    public List<AggregatedSensorDataDto> getAggregatedSensorData(Plant plant, LocalDateTime start, LocalDateTime end, String interval) {
        TemporalUnit groupingUnit = switch (interval.toLowerCase()) {
            case "year" -> ChronoUnit.YEARS;
            case "month" -> ChronoUnit.MONTHS;
            case "day" -> ChronoUnit.DAYS;
            case "hour" -> ChronoUnit.HOURS;
            default -> throw new IllegalArgumentException("Invalid interval: " + interval);
        };


        List<SensorData> rawData = sensorsDataRepository.findByPlantIdAndTimestampBetween(plant, start, end);


        Map<LocalDateTime, List<SensorData>> groupedData = rawData.stream()
                .collect(Collectors.groupingBy(
                        data -> truncateTo(data.getDataTime(), groupingUnit)
                ));


        return groupedData.entrySet().stream()
                .map(entry -> {
                    LocalDateTime timestamp = entry.getKey();
                    List<SensorData> dataList = entry.getValue();
                    double avgTemperature = dataList.stream().collect(Collectors.averagingDouble(SensorData::getTemperature));
                    double avgHumidity = dataList.stream().collect(Collectors.averagingDouble(SensorData::getHumidity));
                    double avgDs18b20Temperature = dataList.stream().collect(Collectors.averagingDouble(SensorData::getDs18b20Temperature));
                    double avgSoilMoisture = dataList.stream().collect(Collectors.averagingDouble(SensorData::getSoilMoisture));
                    double avgPhotoresistorValue = dataList.stream().collect(Collectors.averagingDouble(SensorData::getPhotoresistorValue));


                    avgTemperature = round(avgTemperature);
                    avgHumidity = round(avgHumidity);
                    avgDs18b20Temperature = round(avgDs18b20Temperature);
                    avgSoilMoisture = round(avgSoilMoisture);
                    avgPhotoresistorValue = round(avgPhotoresistorValue);


                    return new AggregatedSensorDataDto(
                            timestamp,
                            avgTemperature,
                            avgHumidity,
                            avgDs18b20Temperature,
                            avgSoilMoisture,
                            avgPhotoresistorValue
                    );
                })
                .sorted((d1, d2) -> d1.getTimestamp().compareTo(d2.getTimestamp()))
                .collect(Collectors.toList());
    }

    private LocalDateTime truncateTo(LocalDateTime dateTime, TemporalUnit unit) {
        if (unit == ChronoUnit.YEARS) {
            return dateTime.withDayOfYear(1).truncatedTo(ChronoUnit.MONTHS);
        } else if (unit == ChronoUnit.MONTHS) {
            return dateTime.withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);
        } else if (unit == ChronoUnit.DAYS) {
            return dateTime.truncatedTo(ChronoUnit.DAYS);
        } else if (unit == ChronoUnit.HOURS) {
            return dateTime.truncatedTo(ChronoUnit.HOURS);
        } else if (unit == ChronoUnit.MINUTES) {
            return dateTime.truncatedTo(ChronoUnit.MINUTES);
        } else {
            throw new UnsupportedOperationException("Unsupported unit: " + unit);
        }
    }

    private double round(double value) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


}



