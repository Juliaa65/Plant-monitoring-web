package com.example.plant_system.controller;
import com.example.plant_system.dto.PhotoDto;
import com.example.plant_system.dto.PlantDto;
import com.example.plant_system.model.Photo;
import com.example.plant_system.model.Plant;
import com.example.plant_system.payload.response.MessageResponse;
import com.example.plant_system.service.PhotoService;
import com.example.plant_system.service.PlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


@RestController
@RequestMapping("/api/camera/")
@CrossOrigin(origins = "http://localhost:8086")
public class CameraController {

    @Autowired
    PhotoService photoService;

    @Autowired
    PlantService plantService;

    @PostMapping("/capturePhoto")
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponse capturePhoto(@RequestBody PlantDto plantDto) {
        Long plantId = plantDto.getId();
        String response = "";
        if (plantId != null) {
            Plant plant = plantService.readById(plantId);
            response = photoService.capturePhoto(plant);
        }
        return new MessageResponse(response);
    }

    @GetMapping("/images/{plantId}")
    public ResponseEntity<List<PhotoDto>> getImages(@PathVariable("plantId") Long plantId) throws IOException {

        List<Photo> photoList = plantService.readById(plantId).getPhotos();
        List<PhotoDto> photoDtoList = new ArrayList<>();

        for (Photo photo : photoList) {
            Path imagePath = Path.of(photo.getPath());
            byte[] imageData = Files.readAllBytes(imagePath);
            PhotoDto photoDto = new PhotoDto();
            photoDto.setId(photo.getId());
            photoDto.setName(photo.getName());
            photoDto.setPath(imagePath.toString());
            photoDto.setDateTime(photo.getDataTime());
            photoDto.setImageData(imageData);
            photoDtoList.add(photoDto);
        }

        Collections.sort(photoDtoList, Comparator.comparing(PhotoDto::getDateTime).reversed());
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(photoDtoList);
    }


    @DeleteMapping("/{photoId}")
    public ResponseEntity<List<PhotoDto>> deletePhoto(@PathVariable("photoId") Long photoId) throws IOException {
        Long plantId = photoService.readById(photoId).getPlant().getId();
        if (photoId != null) {
            photoService.deletePhoto(photoId);
        }
        return getImages(plantId);
    }


    @GetMapping("/{plantId}/filter-images")
    public List<PhotoDto> getPhotosFilter(
            @PathVariable("plantId") Long plantId,
            @RequestParam String startDate,
            @RequestParam String endDate) throws IOException{

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate startLocalDate = LocalDate.parse(startDate, dateFormatter);
        LocalDateTime start = startLocalDate.atStartOfDay();
        LocalDate endLocalDate = LocalDate.parse(endDate, dateFormatter);
        LocalDateTime end = endLocalDate.atTime(LocalTime.MAX);
        List<PhotoDto> photoDtoList = photoService.getPhotoByPeriod(plantService.readById(plantId), start, end);
        for(PhotoDto photoDto: photoDtoList){
            photoDto.setImageData(Files.readAllBytes(Path.of(photoDto.getPath())));
        }
        return photoDtoList;
    }

    @GetMapping("/{plantId}/createGif")
    public ResponseEntity<List<PhotoDto>> createGif(@PathVariable("plantId") Long plantId, @RequestParam String startDate, @RequestParam String endDate) throws IOException {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate startLocalDate = LocalDate.parse(startDate, dateFormatter);
        LocalDateTime start = startLocalDate.atStartOfDay();
        LocalDate endLocalDate = LocalDate.parse(endDate, dateFormatter);
        LocalDateTime end = endLocalDate.atTime(LocalTime.MAX);

        photoService.createAndSaveGif(plantService.readById(plantId), start, end);

        return getImages(plantId);
    }


}

