package com.example.plant_system.controller;


import com.example.plant_system.config.security.CustomUserDetails;
import com.example.plant_system.dto.NoteDto;
import com.example.plant_system.dto.NoteTransformer;
import com.example.plant_system.model.Note;
import com.example.plant_system.model.Plant;
import com.example.plant_system.model.User;
import com.example.plant_system.payload.response.MessageResponse;
import com.example.plant_system.service.NoteService;
import com.example.plant_system.service.PlantService;
import com.example.plant_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/journal/")
@CrossOrigin(origins = "http://localhost:8086")
public class JournalController {

    @Autowired
    PlantService plantService;

    @Autowired
    UserService userService;

    @Autowired
    NoteService noteService;

    @PostMapping("/addNote")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createNote(@Valid @RequestBody NoteDto noteDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long user_id = ((CustomUserDetails) authentication.getPrincipal()).getId();
        User user = userService.readById(user_id);
        noteDto.setId((long) noteService.getAll().size() + 3);
        noteDto.setNoteDate(LocalDateTime.now());
        noteService.create(NoteTransformer.convertToEntity(noteDto, user, plantService.readById(noteDto.getPlantId())));
        return ResponseEntity.ok(new MessageResponse("Note created successfully!"));
    }

    @GetMapping("/{plantId}")
    public List<NoteDto> getAllPlantsByPlant(@PathVariable("plantId") Long plantId){
        Plant plant = plantService.readById(plantId);
        if(plant!=null){
            List<Note> notes = noteService.findNoteByPlant(plant);
            List<NoteDto> noteDtos = new ArrayList<>(notes.stream().map(NoteTransformer::convertToDto).toList());
            Collections.sort(noteDtos, Comparator.comparing(NoteDto::getNoteDate).reversed());
            return noteDtos;
        }
        return new ArrayList<>();
    }

    @DeleteMapping("/{plantId}/{noteId}")
    public List<NoteDto> deleteNoteById(@PathVariable(name = "noteId") Long noteId, @PathVariable("plantId") Long plantId) {
        noteService.delete(noteId);
        return getAllPlantsByPlant(plantId);
    }

    @PutMapping("/editNote")
    public ResponseEntity<?> updateNote(@Valid @RequestBody NoteDto noteDto) {
        noteService.update(NoteTransformer.convertToEntity(noteDto,
                userService.readById(noteDto.getUserId()), plantService.readById(noteDto.getPlantId())));
        return ResponseEntity.ok(new MessageResponse("Note updated successfully!"));
    }


}
