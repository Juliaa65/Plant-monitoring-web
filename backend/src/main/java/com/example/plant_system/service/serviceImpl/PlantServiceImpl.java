package com.example.plant_system.service.serviceImpl;

import com.example.plant_system.model.Plant;
import com.example.plant_system.repository.PlantRepository;
import com.example.plant_system.service.PlantService;
import com.example.plant_system.exception.NullEntityReferenceException;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

@Service
public class PlantServiceImpl implements PlantService {

    private final PlantRepository plantRepository;


    public PlantServiceImpl(PlantRepository plantrepository) {
        this.plantRepository = plantrepository;
    }

    @Override
    public Plant create(Plant plant) {
        if (plant != null) {
            return plantRepository.save(plant);
        }
        throw new NullEntityReferenceException("Plant cannot be 'null'");
    }

    @Override
    public Plant readById(long id) {
        return plantRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Plant with id " + id + " not found"));
    }

    @Override
    public Plant getPlantInfo(long id) {
        Plant plant = readById(id);
        if (plant.getCamera_url() != null) {
           plant.setCamera_url("http://" + plant.getCamera_url() + ":81/stream");
        }
        return plant;
    }

    @Override
    public Plant update(Plant plant) {
        if (plant != null) {
            final Plant plantUpdate = readById(plant.getId());
            plantUpdate.setName(plant.getName());
            plantUpdate.setType(plant.getType());
            plantUpdate.setInfo(plant.getInfo());
            plantUpdate.setLocation(plant.getLocation());
            plantUpdate.setCamera_url(plant.getCamera_url());
            plantUpdate.setSystem_url(plant.getSystem_url());
            return plantRepository.save(plantUpdate);
        }
        throw new NullEntityReferenceException("Plant cannot be 'null'");
    }

    @Override
    public void delete(Long id) {
        Plant plant = readById(id);
        plantRepository.delete(plant);
    }

    @Override
    public List<Plant> getAll() {
        return plantRepository.findAll();
    }

    @Override
    public List<Plant> getByUserId(long userId) {
        return plantRepository.getPlantByUserId(userId);
    }

    @Override
    public String getPlantAgeFormatted(Long plantId) {
        Plant plant = plantRepository.findById(plantId).orElseThrow(() -> new IllegalArgumentException("Invalid plant ID"));
        LocalDateTime plantDate = plant.getPlantDate();
        LocalDateTime now = LocalDateTime.now();
        Period period = Period.between(plantDate.toLocalDate(), now.toLocalDate());
        int months = period.getMonths();
        int days = period.getDays();
        int years = period.getYears();
        if (years > 0) {
            return String.format("%d years, %d months, %d days", years, months, days);
        } else if (months > 0) {
            return String.format("%d months, %d days", months, days);
        } else if (days > 0) {
            return String.format("%d days", days);
        }
        return "Wrong start date!";
    }


}
