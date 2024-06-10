import React, { Component } from 'react';
import PhotoService from "../services/photo.service";
import PlantService from "../services/plant.service";
import { BiTrash } from 'react-icons/bi';

export default class PhotoGallery extends Component {
    constructor(props) {
        super(props);
        this.state = {
            images: [],
            plantId: this.getPlantIdFromUrl(),
            startDate: new Date(),
            endDate: new Date(),
            error: null
        };
    }

    componentDidMount() {
        const plantId = this.getPlantIdFromUrl();
        PlantService.getPhotos(plantId)
            .then(response => {

                this.setState({ images: response.data });
            })
            .catch(error => {

                this.setState({ error: 'Виникла помилка завантаження зображення.' });
            });
        this.fetchPlantName();
    }

    getPlantIdFromUrl() {
        const url = window.location.href;
        const parts = url.split('/');
        return parts[parts.length - 1];
    }

    handleDelete = (imageId) => {
        PhotoService.deletePhoto(imageId)
            .then(response => {

                console.log("Фото видалено успішно:", response.data);

                this.setState(prevState => ({
                    images: prevState.images.filter(image => image.id !== imageId)
                }));

            })
            .catch(error => {

                console.error("Помилка при видаленні фото:", error);

                this.setState({
                    error: "Помилка при видаленні фото."
                });
            });
    };



    handleStartDateChange = (e) => {
        this.setState({ startDate: e.target.value });
    };

    handleEndDateChange = (e) => {
        this.setState({ endDate: e.target.value });
    };

    handleFilterSubmit = (e) => {
        e.preventDefault();
        const plantId = this.getPlantIdFromUrl();
        const { startDate, endDate } = this.state;
        PhotoService.getPhotos(plantId, startDate, endDate)
            .then(response => {
                this.setState({ images: response.data });
            })
            .catch(error => {
                this.setState({ error: 'Оберіть дату, будь ласка' });
            });
    }

    handleCreateGif = (e) => {
        e.preventDefault();
        const plantId = this.getPlantIdFromUrl();
        const { startDate, endDate } = this.state;
        PhotoService.createGif(plantId, startDate, endDate)
            .then(response => {
                console.log("Відео успішно створено:", response.data);
                this.setState({ images: response.data });
            })
            .catch(error => {
                this.setState({ error: 'Помилка створення відео.' });
            });
    }

    fetchPlantName() {
        const { plantId } = this.state;
        PlantService.getPlantName(plantId)
            .then(response => {
                this.setState({ plantName: response.data });
            })
            .catch(error => {
                console.error('Помилка отримання імені:', error);
            });
    }

    render() {
        const { images, error, startDate, endDate, plantName } = this.state;
        const chunkedImages = [];
        for (let i = 0; i < images.length; i += 3) {
            chunkedImages.push(images.slice(i, i + 3));
        }

        return (

            <div>
                <button className="btn back-btn" onClick={() => window.history.back()}></button>
                <h2 className="datacharts_title">{plantName} Галерея</h2>
                <form onSubmit={this.handleFilterSubmit} className="filter-container">
                    <label>
                        З
                        <input type="date" value={startDate} onChange={this.handleStartDateChange}/>
                    </label>
                    <label>
                        До
                        <input type="date" value={endDate} onChange={this.handleEndDateChange}/>
                    </label>
                    <button type="submit">Фільтрувати</button>
                    <button type="button" onClick={this.handleCreateGif}  style={{ width: '150px'}}>Створити відео</button>
                </form>
                {error && <div>Помилка: {error}</div>}
                <table className="table-gallery">
                    <tbody>
                    {chunkedImages.map((row, rowIndex) => (
                        <tr key={rowIndex}>
                            {row.map((image, columnIndex) => (
                                <td key={columnIndex}>
                                    <div className="image-container">
                                        <button className="delete-button-img"
                                            onClick={(e) => {
                                                e.stopPropagation();
                                                this.handleDelete(image.id);
                                            }}
                                            title="Delete">
                                            <BiTrash/>
                                        </button>
                                        <img src={`data:image/jpeg;base64,${image.imageData}`} alt={`${image.id}`}/>
                                    </div>
                                </td>
                            ))}
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        );
    }
}
