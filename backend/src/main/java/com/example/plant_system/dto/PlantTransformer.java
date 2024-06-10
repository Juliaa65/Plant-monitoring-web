package com.example.plant_system.dto;

import com.example.plant_system.model.Plant;
import com.example.plant_system.model.User;

import java.util.stream.Collectors;

public class PlantTransformer {

    public static PlantDto convertToDto(Plant plant) {
        return new PlantDto(plant.getId(),
                plant.getName(),
                plant.getType(),
                plant.getInfo(),
                plant.getSystem_url(),
                plant.getCamera_url(),
                plant.getOwner().getId(),
                plant.getLocation(),
                plant.getPlantDate(),
                plant.getSensorData().stream().map(SensorDataTransformer::convertToDto).collect(Collectors.toList()),
                plant.getPhotos().stream().map(PhotoTransformer::convertToDto).collect(Collectors.toList()));
    }

    public static Plant convertToEntity(PlantDto plantDto, User user){
        Plant plant = new Plant();
        plant.setId(plantDto.getId());
        plant.setName(plantDto.getPlantName());
        plant.setType(plantDto.getType());
        plant.setInfo(plantDto.getInfo());
        plant.setCamera_url(plantDto.getCamera_url());
        plant.setSystem_url(plantDto.getSystem_url());
        plant.setLocation(plantDto.getLocation());
        plant.setPlantDate(plantDto.getPlantDate());
        plant.setOwner(user);
        return plant;
    }
}
