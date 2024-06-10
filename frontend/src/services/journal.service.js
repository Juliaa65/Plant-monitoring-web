import axios from 'axios';
import authHeader from './auth-header';

const API_URL = 'http://localhost:8085/api/journal';

class JournalService {


    createNote(title, text, plantId) {
        return axios.post(API_URL + "/addNote", {
            title,
            text,
            plantId
        }, { headers: authHeader() });
    }

    getAllNotes(plantId){
        return axios.get(API_URL + "/" + plantId, { headers: authHeader() });
    }

    deleteNote(noteId, plantId){
        return axios.delete(API_URL + "/"+plantId+"/"+noteId, { headers: authHeader() });
    }

    updateNote(note){
        return axios.put(API_URL + "/editNote", {
            id: note.id,
            title: note.title,
            text: note.text,
            noteDate: note.noteDate,
            plantId: note.plantId,
            userId: note.userId}, { headers: authHeader() });
    }
}

export default new JournalService();