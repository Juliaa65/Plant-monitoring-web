package com.example.plant_system.dto;

import com.example.plant_system.model.Photo;

public class PhotoTransformer {

    public static PhotoDto convertToDto(Photo photo){
        return new PhotoDto(
                photo.getId(),
                photo.getName(),
                photo.getPath()

        );
    }
}
