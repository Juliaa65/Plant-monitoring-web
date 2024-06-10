import React, { Component } from "react";
import PlantService from "../services/plant.service";

const required = value => {
    if (!value) {
        return (
            <div className="alert alert-danger" role="alert">
                This field is required!
            </div>
        );
    }
};

const validIP = value => {
    const ipRegex = /^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
    if (!ipRegex.test(value)) {
        return (
            <div className="alert alert-warning" role="alert">
                Please enter a valid IP address.
            </div>
        );
    }
};

export default class EditPlant extends Component {
    constructor(props) {
        super(props);
        this.handleEditPlant = this.handleEditPlant.bind(this);
        this.onChangePlantName = this.onChangePlantName.bind(this);
        this.onChangeType = this.onChangeType.bind(this);
        this.onChangeInfo = this.onChangeInfo.bind(this);
        this.onChangeLocation = this.onChangeLocation.bind(this);
        this.onChangeSystemUrl = this.onChangeSystemUrl.bind(this);
        this.onChangeCameraUrl = this.onChangeCameraUrl.bind(this);


        this.state = {
            id: "",
            plantName: "",
            type: "",
            info: "",
            location: "",
            systemUrl: "",
            cameraUrl: "",
            successful: false,
            message: ""
        };
    }

    componentDidMount() {
        const plantId = this.getPlantIdFromUrl();
        PlantService.getPlantInfo(plantId)
            .then(response => {
                const { id, plantName, type, info, systemUrl, cameraUrl, location, plantDate } = response.data;
                this.setState({
                    id,
                    plantName,
                    type,
                    info,
                    systemUrl,
                    cameraUrl,
                    location,
                    plantDate
                });
            })
            .catch(error => {
                console.error("Виникла помилка:", error);
            });
    }

    getPlantIdFromUrl() {
        const url = window.location.href;
        const parts = url.split('/');
        return parts[parts.length - 1];
    }

    onChangePlantName(e) {
        this.setState({
            plantName: e.target.value
        });
    }

    onChangeType(e) {
        this.setState({
            type: e.target.value
        });
    }

    onChangeInfo(e) {
        this.setState({
            info: e.target.value
        });
    }

    onChangeLocation(e) {
        this.setState({
            location : e.target.value
        });
    }

    onChangeSystemUrl(e) {
        this.setState({
            systemUrl: e.target.value
        });
    }

    onChangeCameraUrl(e) {
        this.setState({
            cameraUrl: e.target.value
        });
    }

    handleEditPlant(e) {
        e.preventDefault();

        this.setState({
            message: "",
            successful: false
        });


        if (!this.state.plantName || !this.state.type || !this.state.info) {
            this.setState({ message: "Заповніть всі поля" });
            return;
        }

        const ipRegex = /^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
        if ((this.state.systemUrl && !ipRegex.test(this.state.systemUrl)) || (this.state.cameraUrl && !ipRegex.test(this.state.cameraUrl))) {
            this.setState({ message: "Введіть валідну IP адресу" });
            return;
        }


        PlantService.editPlant(
            this.state.id,
            this.state.plantName,
            this.state.type,
            this.state.info,
            this.state.systemUrl,
            this.state.cameraUrl,
            this.state.location,
            this.state.plantDate
        )
            .then(response => {
                // Handle success
                console.log("Дані було оновлено успішно:", response.data);
                this.setState({
                    message: "Дані було оновлено успішно.",
                    successful: true
                });
            })
            .catch(error => {

                console.error("Виникла помилка при оновлені даних:", error);

                this.setState({
                    message: "Виникла помилка при оновлені даних.",
                    successful: false
                });
            });
    }

    render() {
        return (
            <div className="container">
                <button className="btn back-btn" onClick={() => window.history.back()}></button>
                <div className="row justify-content-center">
                    <div className="col-md-6">
                        <h2>Редагування інформації</h2>
                        <form onSubmit={this.handleEditPlant}>
                            <div className="form-group">
                                <label htmlFor="plantName">Назва</label>
                                <input
                                    type="text"
                                    className="form-control"
                                    name="plantName"
                                    value={this.state.plantName}
                                    onChange={this.onChangePlantName}
                                    validations={[required]}
                                />
                            </div>
                            <div className="form-group">
                                <label htmlFor="type">Вид</label>
                                <input
                                    type="text"
                                    className="form-control"
                                    name="type"
                                    value={this.state.type}
                                    onChange={this.onChangeType}
                                    validations={[required]}
                                />
                            </div>
                            <div className="form-group">
                                <label htmlFor="info">Додаткова інформація</label>
                                <textarea
                                    className="form-control"
                                    name="info"
                                    value={this.state.info}
                                    onChange={this.onChangeInfo}
                                    validations={[required]}
                                ></textarea>
                            </div>
                            <div className="form-group">
                                <label htmlFor="info">Місцезнаходження</label>
                                <input
                                    className="form-control"
                                    name="location"
                                    value={this.state.location}
                                    onChange={this.onChangeLocation}
                                    validations={[required]}
                                ></input>
                            </div>
                            <div className="form-group">
                                <label htmlFor="systemUrl">IP системи</label>
                                <input
                                    type="text"
                                    className="form-control"
                                    name="systemUrl"
                                    value={this.state.systemUrl}
                                    onChange={this.onChangeSystemUrl}
                                    validations={[required, validIP]}
                                />
                            </div>
                            <div className="form-group">
                                <label htmlFor="cameraUrl">IP камери</label>
                                <input
                                    type="text"
                                    className="form-control"
                                    name="cameraUrl"
                                    value={this.state.cameraUrl}
                                    onChange={this.onChangeCameraUrl}
                                    validations={[required, validIP]}
                                />
                            </div>
                            <div className="form-group">
                                <button className="btn btn-primary btn-block">Редагувати інформацію</button>
                            </div>
                        </form>
                        {this.state.message && (
                            <div className="form-group">
                                <div
                                    className={
                                        this.state.successful
                                            ? "alert alert-success"
                                            : "alert alert-danger"
                                    }
                                    role="alert"
                                >
                                    {this.state.message}
                                </div>
                            </div>
                        )}
                    </div>
                </div>
            </div>
        );
    }
}
