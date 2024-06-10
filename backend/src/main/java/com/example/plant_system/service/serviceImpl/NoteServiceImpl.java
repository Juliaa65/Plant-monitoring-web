package com.example.plant_system.service.serviceImpl;

import com.example.plant_system.exception.NullEntityReferenceException;
import com.example.plant_system.model.Note;
import com.example.plant_system.model.Plant;
import com.example.plant_system.model.User;
import com.example.plant_system.repository.NoteRepository;
import com.example.plant_system.service.NoteService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {


    private final NoteRepository noteRepository;

    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }


    @Override
    public Note create(Note note) {
        if (note != null) {
            return noteRepository.save(note);
        }
        throw new NullEntityReferenceException("Note cannot be 'null'");
    }

    @Override
    public Note readById(long id) {
        return noteRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Note with id " + id + " not found"));
    }

    @Override
    public Note update(Note note) {
        if (note != null) {
            final Note noteUpdate = readById(note.getId());
            noteUpdate.setTitle(note.getTitle());
            noteUpdate.setText(note.getText());
            noteUpdate.setDataTime(note.getDataTime());
            return noteRepository.save(noteUpdate);
        }
        throw new NullEntityReferenceException("Note cannot be 'null'");
    }

    @Override
    public void delete(Long id) {
        Note note = readById(id);
        if(note!=null){
        noteRepository.delete(note);}
    }

    @Override
    public List<Note> getAll() {
        return noteRepository.findAll();
    }

    @Override
    public List<Note> findNoteByPlant(Plant plant) {
        if(plant!=null){
            return noteRepository.findNoteByPlant(plant);
        }
        throw new NullEntityReferenceException("Plant cannot be 'null'");
    }

    @Override
    public List<Note> findNoteByUser(User user) {
        if(user!=null){
            return noteRepository.findNoteByUser(user);
        }
        throw new NullEntityReferenceException("User cannot be 'null'");
    }
}
