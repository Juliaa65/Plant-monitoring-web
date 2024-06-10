import React, { Component } from "react";
import PlantService from "../services/plant.service";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";


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

export default class CreatePlant extends Component {
    constructor(props) {
        super(props);
        this.handleCreatePlant = this.handleCreatePlant.bind(this);
        this.onChangePlantName = this.onChangePlantName.bind(this);
        this.onChangeType = this.onChangeType.bind(this);
        this.onChangeInfo = this.onChangeInfo.bind(this);
        this.onChangeSystemUrl = this.onChangeSystemUrl.bind(this);
        this.onChangeCameraUrl = this.onChangeCameraUrl.bind(this);
        this.onChangeLocation = this.onChangeLocation.bind(this);
        this.onChangePlantDate = this.onChangePlantDate.bind(this);

        this.state = {
            plantName: "",
            type: "",
            info: "",
            location: "",
            plantDate: new Date(),
            systemUrl: "",
            cameraUrl: "",
            successful: false,
            message: ""
        };
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

    onChangeLocation(e) {
        this.setState({
            location: e.target.value
        });
    }

    onChangePlantDate(date) {
        this.setState({
            plantDate: date
        });
    }

    handleCreatePlant(e) {
        e.preventDefault();

        this.setState({
            message: "",
            successful: false
        });

        // Validate form fields
        if (!this.state.plantName || !this.state.type || !this.state.info || !this.state.location || !this.state.plantDate) {
            this.setState({ message: "Заповніть всі поля, будь ласка." });
            return;
        }

        const ipRegex = /^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
        if ((this.state.systemUrl && !ipRegex.test(this.state.systemUrl)) || (this.state.cameraUrl && !ipRegex.test(this.state.cameraUrl))) {
            this.setState({ message: "Введіть валідну IP адресу." });
            return;
        }


        PlantService.createPlant(
            this.state.plantName,
            this.state.type,
            this.state.info,
            this.state.systemUrl,
            this.state.cameraUrl,
            this.state.location,
            this.state.plantDate
        )
            .then(response => {

                console.log("Plant created successfully:", response.data);
                this.setState({
                    message: "Нову рослину було успішно додано",
                    successful: true
                });
            })
            .catch(error => {

                console.error("Виникла помилка:", error);

                this.setState({
                    message: "Виникла помилка",
                    successful: false
                });
            });
    }


    required = value => {
        if (!value) {
            return (
                <div className="alert alert-danger" role="alert">
                    This field is required!
                </div>
            );
        }
    };

    validIP = value => {
        const ipRegex = /^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
        if (!ipRegex.test(value)) {
            return (
                <div className="alert alert-warning" role="alert">
                    Please enter a valid IP address.
                </div>
            );
        }
    };

    render() {
        return (
            <div className="container">
                <button className="btn back-btn" onClick={() => window.history.back()}></button>
                <div className="row justify-content-center">
                    <div className="col-md-6">
                        <h2>Створення нової рослини</h2>
                        {!this.state.successful && (
                            <form onSubmit={this.handleCreatePlant}>
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
                                    <label htmlFor="location">Місцезнаходження</label>
                                    <input
                                        type="text"
                                        className="form-control"
                                        name="location"
                                        value={this.state.location}
                                        onChange={this.onChangeLocation}
                                        validations={[required]}
                                    />
                                </div>
                                <div className="form-group">
                                    <label htmlFor="plantDate">Початок моніторингу</label>
                                    <DatePicker
                                        selected={this.state.plantDate}
                                        onChange={this.onChangePlantDate}
                                        dateFormat="yyyy-MM-dd"
                                        className="form-control"
                                    />
                                </div>
                                <div className="form-group">
                                    <label htmlFor="systemUrl">IP системи</label>
                                    <input
                                        type="text"
                                        className="form-control"
                                        name="systemUrl"
                                        value={this.state.systemUrl}
                                        onChange={this.onChangeSystemUrl}
                                        validations={[validIP]}
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
                                        validations={[validIP]}
                                    />
                                </div>
                                <div className="form-group">
                                    <button className="btn btn-primary btn-block">Додати рослину</button>
                                </div>
                            </form>
                        )}
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
