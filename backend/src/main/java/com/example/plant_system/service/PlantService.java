package com.example.plant_system.service;

import com.example.plant_system.model.Plant;

import java.net.MalformedURLException;
import java.util.List;

public interface PlantService {

    Plant create(Plant plant);
    Plant readById(long id);

    Plant getPlantInfo(long id);

    Plant update(Plant plant);
    void delete(Long id);
    List<Plant> getAll();
    List<Plant> getByUserId(long userId);

    String getPlantAgeFormatted(Long plantId);


}
