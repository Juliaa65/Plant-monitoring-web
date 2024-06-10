import React, {Component} from 'react';
import PlantService from '../services/plant.service';
import {Link} from "react-router-dom";


export default class PlantInfo extends Component {

    constructor(props) {
        super(props);
        this.state = {
            plant: null,
            error: null,
            message: null
        };
    }

    componentDidMount() {

        const plantId = this.getPlantIdFromUrl();
        PlantService.getPlantInfo(plantId)
            .then(response => {
                this.setState({
                    plant: response.data,
                    error: null
                });
            })
            .catch(error => {
                this.setState({
                    plant: null,
                    error: 'Failed to fetch plant information.'
                });
            });
    }

    getPlantIdFromUrl() {
        const url = window.location.href;
        const parts = url.split('/');
        return parts[parts.length - 1];
    }

    handleCapturePhoto = () => {
        const plantId = this.state.plant.id;
        PlantService.capturePhoto(plantId)
            .then(response => {
                this.setState({
                    message: response.data.message
                });
                console.log("Photo captured successfully:", response.data);
            })
            .catch(error => {
                this.setState({
                    error: "Failed to capture photo."
                });
            });
    };



    render() {
        const {plant, error, message} = this.state;

        if (error) {
            return <div>Error: {error}</div>;
        }

        if (!plant) {
            return <div>Loading...</div>;
        }

        return (
            <div className="containerMy">
                <div className="button-container">
                    <button className="btn back-btn" onClick={() => window.history.back()}></button>
                    <p></p>
                    <Link to={`/gallery/${plant.id}`} className="btn btn-info">Галерея</Link>
                    <p></p>
                    <Link to={`/dashboard/${plant.id}`} className="btn btn-success ">Панель даних</Link>
                    <p></p>
                    <Link to={`/journal/${plant.id}`} className="btn btn-secondary">Журнал</Link>
                    <p></p>
                </div>
                <div className="left-sideMy">
                    <div className="title">
                        <h2 className="title">Інформація</h2>
                        <Link to={`/plant-edit/${plant.id}`} className="edit-link">Редагувати</Link>
                    </div>
                    <div className="image-container">
                        <img src={plant.camera_url} alt="Plant"/>
                    </div>
                    <div className="info-container details">
                        <div className="detail-item">
                            <span className="sensor__label"><strong>Назва:</strong></span>
                            <span className="sensor__value">{plant.plantName}</span>
                        </div>
                        <div className="detail-item">
                            <span className="sensor__label"><strong>Вид:</strong></span>
                            <span className="sensor__value">{plant.type}</span>
                        </div>
                        <div className="detail-item">
                            <span className="sensor__label"><strong>Вік:</strong></span>
                            <span className="sensor__value">{plant.age}</span>
                        </div>
                        <div className="detail-item">
                            <span className="sensor__label"><strong>Опис:</strong></span>
                            <span className="sensor__value">{plant.info}</span>
                        </div>
                        <div className="detail-item">
                            <span className="sensor__label"><strong>Місцезнаходження:</strong></span>
                            <span className="sensor__value">{plant.location}</span>
                        </div>
                    </div>
                    <div>

                        {plant.camera_url && plant.camera_url.startsWith("http://") && (
                            <button
                                onClick={this.handleCapturePhoto}
                                className="btn btn-info">
                                Зробити фото
                            </button>
                        )}

                    </div>
                    {message && (
                        <div className="alert alert-info" role="alert">
                            {message}
                        </div>
                    )}
                </div>
                <div className="right-sideMy">
                    <h2 className="sensor_title">Дані навколишнього середовища</h2>
                    <div className="sensor-container">
                        <div className="sensor">
                            <div className="gauge">
                                <div className="gauge__body">
                                    <div className="gauge__fill"
                                         style={{transform: `rotate(${plant.sensorData.temperature / 200}turn)`}}></div>
                                    <div className="gauge__cover">{plant.sensorData.temperature} С°</div>
                                </div>
                            </div>
                            <div className="sensor__label">Температура</div>
                        </div>
                        <div className="sensor">
                            <div className="gauge">
                                <div className="gauge__body">
                                    <div className="gauge__fill"
                                         style={{transform: `rotate(${plant.sensorData.humidity / 200}turn)`}}></div>
                                    <div className="gauge__cover">{plant.sensorData.humidity}%</div>
                                </div>
                            </div>
                            <div className="sensor__label">Вологість</div>
                        </div>
                        <div className="sensor">
                            <div className="gauge">
                                <div className="gauge__body">
                                    <div className="gauge__fill"
                                         style={{transform: `rotate(${plant.sensorData.ds18b20Temperature / 200}turn)`}}></div>
                                    <div className="gauge__cover">{plant.sensorData.ds18b20Temperature} С°</div>
                                </div>
                            </div>
                            <div className="sensor__label">Температура ґрунту</div>
                        </div>
                        <div className="sensor">
                            <div className="gauge">
                                <div className="gauge__body">
                                    <div className="gauge__fill"
                                         style={{transform: `rotate(${plant.sensorData.soilMoisture / 200}turn)`}}></div>
                                    <div className="gauge__cover">{plant.sensorData.soilMoisture}%</div>
                                </div>
                            </div>
                            <div className="sensor__label">Вологість ґрунту</div>
                        </div>
                        <div className="sensor">
                            <div className="gauge">
                                <div className="gauge__body">
                                    <div className="gauge__fill"
                                         style={{transform: `rotate(${plant.sensorData.photoresistorValue / 200}turn)`}}></div>
                                    <div className="gauge__cover">{plant.sensorData.photoresistorValue}%</div>
                                </div>
                            </div>
                            <div className="sensor__label">Освітлення</div>
                        </div>
                    </div>
                </div>
            </div>
        )
            ;
    }

}


