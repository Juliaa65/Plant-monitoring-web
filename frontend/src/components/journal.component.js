import React, { Component } from 'react';
import JournalService from "../services/journal.service";
import PlantService from "../services/plant.service";

export default class NoteForm extends Component {
    constructor(props) {
        super(props);
        this.state = {
            title: '',
            text: '',
            noteDate: new Date(),
            plantId: this.getPlantIdFromUrl(),
            plantName: '',
            notes: [],
            message: '',
            error: '',
        };
    }

    componentDidMount() {
        this.fetchPlantName();
        this.fetchNotes();
    }

    getPlantIdFromUrl() {
        const url = window.location.href;
        const parts = url.split('/');
        return parts[parts.length - 1];
    }

    fetchPlantName() {
        const { plantId } = this.state;
       PlantService.getPlantName(plantId)
            .then(response => {
                this.setState({ plantName: response.data });
            })
            .catch(error => {
                console.error('Failed to fetch plant name:', error);
            });
    }

    fetchNotes() {
        const { plantId } = this.state;
        JournalService.getAllNotes(plantId)
            .then(response => {
                this.setState({ notes: response.data });
            })
            .catch(error => {
                console.error('Помилка отримання записів:', error);
                this.setState({ error: 'Помилка отримання записів.' });
                this.clearMessage();
            });
    }

    handleChange = (e) => {
        const { name, value } = e.target;
        this.setState({ [name]: value });
    };

    handleSubmit = (e) => {
        e.preventDefault();
        const { title, text, plantId } = this.state;

        JournalService.createNote(title, text, plantId)
            .then(response => {
                this.setState({
                    title: '',
                    text: '',
                    message: response.data.message,
                    error: ''
                });
                this.fetchNotes();
                this.clearMessage();
            })
            .catch(error => {
                this.setState({
                    message: '',
                    error: error.response.data.message || 'Помилка створення.'
                }
                );
            });
    };

    handleEditChange = (noteId, field, value) => {
        this.setState(prevState => {
            const updatedNotes = prevState.notes.map(note => {
                if (note.id === noteId) {
                    return { ...note, [field]: value };
                }
                return note;
            });
            return { notes: updatedNotes };
        });
    };

    handleEditSubmit = (noteId) => {
        const note = this.state.notes.find(note => note.id === noteId);

        JournalService.updateNote(note)
            .then(response => {
                this.setState({
                    message: response.data.message,
                    error: ''
                });
                this.fetchNotes();
                this.clearMessage();
            })
            .catch(error => {
                this.setState({
                    message: '',
                    error: error.response.data.message || 'Помилка оновлення даних.'
                });
            });
    };

    handleDelete = (noteId) => {
        const {plantId } = this.state;
        JournalService.deleteNote(noteId, plantId)
            .then(response => {
                this.setState({
                    message: response.data.message,
                    error: ''
                });
                this.fetchNotes();
            })
            .catch(error => {
                this.setState({
                    message: '',
                    error: error.response.data.message || 'Помилка видалення.'
                });
            });
    };

    clearMessage = () => {
        setTimeout(() => {
            this.setState({ message: '', error: '' });
        }, 3000);
    };

    formatDate = (dateString) => {
        const options = { year: 'numeric', month: '2-digit', day: '2-digit' };
        return new Date(dateString).toLocaleDateString('en-GB', options).replace(/\//g, '-');
    };

    render() {
        const { title, text, plantName, notes, message, error } = this.state;

        return (
            <div>
                <button className="btn back-btn" onClick={() => window.history.back()}></button>

        <div className="note-container">

            <div className="note-form-container">
                    <h2 className="sensor_title">{plantName} Журнал</h2>
                    <form onSubmit={this.handleSubmit} className="note-form">
                        <div className="form-group">
                            <label htmlFor="title">Заголовок</label>
                            <input
                                type="text"
                                id="title"
                                name="title"
                                value={title}
                                onChange={this.handleChange}
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label htmlFor="text">Основний текст</label>
                            <textarea
                                id="text"
                                name="text"
                                value={text}
                                onChange={this.handleChange}
                                required
                            />
                        </div>
                        <button type="submit">Додавання нового запису</button>
                    </form>
                    {message && <div className="success-message">{message}</div>}
                    {error && <div className="error-message">{error}</div>}
                </div>
                <div className="notes-list-container">
                    <h3 className="sensor_title">Записи</h3>
                    <div className="notes-list">
                        {notes.length === 0 ? (
                            <div className="note-item">Записи не доступні</div>
                        ) : (
                            notes.map(note => (
                                <div key={note.id} className="note-item">
                                    <label>{this.formatDate(note.noteDate)}</label>
                                    <input
                                        type="text"
                                        value={note.title}
                                        onChange={(e) => this.handleEditChange(note.id, 'title', e.target.value)}
                                    />
                                    <textarea
                                        value={note.text}
                                        onChange={(e) => this.handleEditChange(note.id, 'text', e.target.value)}
                                    />
                                    <button onClick={() => this.handleEditSubmit(note.id)}>Зберегти зміни</button>
                                    <button onClick={() => this.handleDelete(note.id)}>Видалити</button>
                                </div>
                            ))
                        )}
                    </div>
                </div>
            </div>
            </div>
        );
    }
}
