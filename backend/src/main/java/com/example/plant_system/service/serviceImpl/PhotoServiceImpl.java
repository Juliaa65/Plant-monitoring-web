package com.example.plant_system.service.serviceImpl;


import com.example.plant_system.dto.PhotoDto;
import com.example.plant_system.dto.PhotoTransformer;
import com.example.plant_system.model.Photo;
import com.example.plant_system.model.Plant;
import com.example.plant_system.repository.PhotoRepository;
import com.example.plant_system.service.PhotoService;
import com.madgag.gif.fmsware.AnimatedGifEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import javax.persistence.EntityNotFoundException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;
    private final RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(PhotoServiceImpl.class);

    public PhotoServiceImpl(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
        this.restTemplate = new RestTemplate();
    }

    private static final String SAVE_DIRECTORY = ".\\src\\main\\resources\\gallery";


    private String generateFileName() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(DateTimeFormatter.ofPattern("HH-mm-ss"));
    }

    private String getCurrentDate() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    private String getCaptureUrl(String ip){
        if(ip!=null){
            return "http://"+ip+"/capture";
        }
       return "";
    }

    @Override
    public String capturePhoto(Plant plant) {

        String PHOTO_URL = getCaptureUrl(plant.getCamera_url());
        if(!PHOTO_URL.isBlank()){
        ResponseEntity<byte[]> response = restTemplate.getForEntity(PHOTO_URL, byte[].class);
        if (response.getStatusCode().is2xxSuccessful()) {
            String folderPath = Paths.get(SAVE_DIRECTORY, String.valueOf(plant.getId()),  getCurrentDate()).toString();
            String fileName = generateFileName()+".jpg";
            String filePath = Paths.get(folderPath, fileName).toString();

            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(Objects.requireNonNull(response.getBody()));
                Photo photo = new Photo();
                photo.setName(fileName);
                photo.setPath(filePath);
                photo.setDataTime(LocalDateTime.now());
                photo.setPlant(plant);
                createPhoto(photo);
                return "Photo saved successfully";
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        } else {
            return "Failed to retrieve photo. Status code: " + response.getStatusCodeValue();
        }}return "Error with camera IP";
    }

    private void createPhoto(Photo photo){
        photoRepository.save(photo);
    }

    @Override
    public List<Photo> getAll() {
        return photoRepository.findAll();
    }

    @Override
    public void deletePhoto(Long id) throws IOException {
        Photo photo = readById(id);
        Files.delete(Paths.get(photo.getPath()));
        photoRepository.delete(photo);
    }

    @Override
    public Photo readById(Long id) {
        return photoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Photo with id " + id + " not found"));
    }

    @Override
    public List<PhotoDto> getPhotoByPeriod(Plant plant, LocalDateTime start, LocalDateTime end) {
        List<Photo> photosByDate = photoRepository.findByPlantIdAndTime(plant,start, end);
        return photosByDate.stream().map(PhotoTransformer::convertToDto).collect(Collectors.toList());
    }

    @Override
    public void createAndSaveGif(Plant plant, LocalDateTime startDate, LocalDateTime endDate) {

        List<String> imagePaths = photoRepository.findByPlantIdAndTime(plant, startDate, endDate).stream().map(Photo::getPath).toList();
        String folderPath = Paths.get(SAVE_DIRECTORY, String.valueOf(plant.getId()),  getCurrentDate(), "gifs").toString();
        String fileName = generateFileName()+".gif";
        String filePath = Paths.get(folderPath, fileName).toString();

        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        try (FileOutputStream outputStream = new FileOutputStream(Paths.get(filePath).toString())) {
            AnimatedGifEncoder gifEncoder = new AnimatedGifEncoder();
            gifEncoder.start(outputStream);
            gifEncoder.setDelay(350);
            gifEncoder.setRepeat(0);

            for (String imagePath : imagePaths) {
                BufferedImage image = ImageIO.read(new File(imagePath));
                gifEncoder.addFrame(image);
            }
            gifEncoder.finish();
            Photo gif = new Photo();
            gif.setName(fileName);
            gif.setPath(filePath);
            gif.setDataTime(LocalDateTime.now());
            gif.setPlant(plant);
            createPhoto(gif);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }


}
