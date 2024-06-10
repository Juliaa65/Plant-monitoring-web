package com.example.plant_system.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NoteDto {

    private Long id;

    @NotBlank
    private String title;

    @NotEmpty
    private String text;


    private LocalDateTime noteDate;


    private Long userId;


    private Long plantId;

}
