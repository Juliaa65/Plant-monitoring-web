package com.example.plant_system.dto;

import com.example.plant_system.model.Note;
import com.example.plant_system.model.Plant;
import com.example.plant_system.model.User;

import java.util.stream.Collectors;

public class NoteTransformer {

    public static NoteDto convertToDto(Note note) {
        return new NoteDto(note.getId(),
                           note.getTitle(),
                           note.getText(),
                           note.getDataTime(),
                           note.getUser().getId(),
                           note.getPlant().getId());
              }

    public static Note convertToEntity(NoteDto noteDto, User user, Plant plant){
        Note note = new Note();
        note.setId(noteDto.getId());
        note.setTitle(noteDto.getTitle());
        note.setText(noteDto.getText());
        note.setDataTime(noteDto.getNoteDate());
        note.setUser(user);
        note.setPlant(plant);
        return note;
    }
}
