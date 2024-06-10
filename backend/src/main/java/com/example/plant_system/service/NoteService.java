package com.example.plant_system.service;

import com.example.plant_system.model.Note;
import com.example.plant_system.model.Plant;
import com.example.plant_system.model.User;

import java.util.List;

public interface NoteService {

    Note create(Note note);

    Note readById(long id);
    Note update(Note note);
    void delete(Long id);

    List<Note> getAll();

    List<Note> findNoteByPlant(Plant plant);
    List<Note> findNoteByUser (User user);
}
