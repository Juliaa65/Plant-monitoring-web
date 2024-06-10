package com.example.plant_system.controller;

import com.example.plant_system.config.security.CustomUserDetails;
import com.example.plant_system.dto.PlantDto;
import com.example.plant_system.dto.PlantTransformer;
import com.example.plant_system.dto.SensorDataDto;
import com.example.plant_system.model.Plant;
import com.example.plant_system.model.User;
import com.example.plant_system.payload.response.MessageResponse;
import com.example.plant_system.payload.response.PlantResponse;
import com.example.plant_system.service.PlantService;
import com.example.plant_system.service.SensorDataService;
import com.example.plant_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


@RestController
@RequestMapping("/api/plants")
@CrossOrigin(origins = "http://localhost:8086")
public class PlantController {

    @Autowired
    PlantService plantService;

    @Autowired
    UserService userService;


    @Autowired
    SensorDataService sensorDataService;

    @Value("${image.url}")
    private String imageUrl;

    @GetMapping("/allPlants")
    public List<PlantDto> getAllPlantsByUser() {
        return getAllPlantsByUserId(getUserId());
    }

    public List<PlantDto> getAllPlantsByUserId(Long userId) {
        List<PlantDto> plantDtoList = new ArrayList<>(plantService.getByUserId(userId)
                .stream()
                .map(PlantTransformer::convertToDto)
                .toList());
        Collections.sort(plantDtoList, Comparator.comparing(PlantDto::getPlantName));
        return plantDtoList;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createPlant(@Valid @RequestBody PlantDto plantDto) {
        User user = userService.readById(getUserId());
        plantDto.setId((long) plantService.getAll().size() + 2);
        plantService.create(PlantTransformer.convertToEntity(plantDto, user));
        return ResponseEntity.ok(new MessageResponse("Plant created successfully!"));
    }

    @GetMapping("/plant-info/{plantId}")
    public ResponseEntity<?> getPlantInfo(@PathVariable(name = "plantId") Long plantId) {
        SensorDataDto sensorDataDtoCurrent = sensorDataService.getSensorDataForPlant(plantService.readById(plantId));
        Plant plant = plantService.getPlantInfo(plantId);
        PlantDto plantDto = PlantTransformer.convertToDto(plant);
        PlantResponse plantResponse = null;
        if (!plantDto.getSensorDataList().isEmpty() && !plantDto.getCamera_url().isBlank() && sensorDataDtoCurrent!=null) {
            plantResponse = new PlantResponse(plantDto.getId(),
                    plantDto.getPlantName(),
                    plantDto.getType(),
                    plantDto.getInfo(),
                    plantDto.getLocation(),
                    plantService.getPlantAgeFormatted(plantId),
                    plantDto.getSystem_url(),
                    plantDto.getCamera_url(),
                    plantDto.getOwnerId(),
                    sensorDataDtoCurrent);
        } else {
            SensorDataDto sensorDataDto = new SensorDataDto(0L, 0.0, 0.0, 0.0, 0.0, 0, LocalDateTime.now());
            plantDto.getSensorDataList().add(sensorDataDto);
            plantDto.setCamera_url(imageUrl);
            plantResponse = new PlantResponse(
                    plantDto.getId(),
                    plantDto.getPlantName(),
                    plantDto.getType(),
                    plantDto.getInfo(),
                    plantDto.getLocation(),
                    plantService.getPlantAgeFormatted(plantId),
                    plantDto.getSystem_url(),
                    plantDto.getCamera_url(),
                    plantDto.getOwnerId(),
                    plantDto.getSensorDataList().get(0)
            );
        }
        return ResponseEntity.ok(plantResponse);
    }

    @DeleteMapping("/{plantId}")
    public List<PlantDto> deletePlantById(@PathVariable(name = "plantId") Long plantId) {
        plantService.delete(plantId);
        return getAllPlantsByUserId(getUserId());
    }

    @PutMapping
    public ResponseEntity<?> updatePlant(@Valid @RequestBody PlantDto plantDto) {
        User user = userService.readById(getUserId());
        plantService.update(PlantTransformer.convertToEntity(plantDto, user));
        return ResponseEntity.ok(new MessageResponse("Plant updated successfully!"));
    }

    @GetMapping("/getPlantName/{plantId}")
    public String getPlantName(@PathVariable("plantId") Long plantId){
        return plantService.readById(plantId).getName();
    }

    public Long getUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((CustomUserDetails) authentication.getPrincipal()).getId();
    }
}
