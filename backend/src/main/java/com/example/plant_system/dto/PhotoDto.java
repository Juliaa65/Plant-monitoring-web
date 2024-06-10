package com.example.plant_system.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PhotoDto {

    private Long id;

    private String name;
    @NotBlank
    private String path;

    private byte [] imageData;

    private LocalDateTime dateTime;

    public PhotoDto(Long id, String name,String path) {
        this.id = id;
        this.name = name;
        this.path = path;
    }
}
