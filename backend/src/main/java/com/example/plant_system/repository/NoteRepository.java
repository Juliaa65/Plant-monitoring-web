package com.example.plant_system.repository;

import com.example.plant_system.model.Note;
import com.example.plant_system.model.Plant;
import com.example.plant_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findNoteByPlant(Plant plant);
    List<Note> findNoteByUser (User user);
}
