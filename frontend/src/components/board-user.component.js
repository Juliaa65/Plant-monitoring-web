import React, { Component } from "react";
import { Link } from "react-router-dom";
import UserService from "../services/user.service";
import PlantService from "../services/plant.service";
import {BiTrash} from "react-icons/bi";


export default class BoardUser extends Component {
    constructor(props) {
        super(props);

        this.state = {
            plants: [],
            error: ""
        };
    }

    componentDidMount() {
       UserService.getAllPlants().then(
            response => {
                this.setState({
                    plants: response.data
                });
            },
            error => {
                this.setState({
                    error:
                        (error.response && error.response.data && error.response.data.message) ||
                        error.message ||
                        error.toString()
                });
            }
        );
    }

    handleDeletePlant = (plantId) => {
        PlantService.deletePlant(plantId)
            .then(response => {

                console.log("Plant deleted successfully:", response.data);

                this.setState(prevState => ({
                    plants: prevState.plants.filter(plant => plant.id !== plantId)
                }));
            })
            .catch(error => {

                console.error("Error deleting plant:", error);

                this.setState({
                    error: "Помилка видалення рослини"
                });
            });
    };

    render() {
        const { plants, error } = this.state;

        return (
            <div className="container">
                <header className="jumbotron">
                    <h3>РОСЛИНИ</h3>
                </header>
                <div className="header-right">
                    <Link to="/new-plant" className="btn btn-primary">Додати рослину</Link>
                </div>
                {error && <div className="alert alert-danger">{error}</div>}
                <div className="card-container">
                    {plants.map((plant, index) => (
                        <div className="card" key={index}>
                            <button
                                onClick={(e) => {
                                    e.stopPropagation();
                                    this.handleDeletePlant(plant.id);
                                }}
                                className="delete-button"
                                title="Delete plant"
                            >
                                <BiTrash/>
                            </button>
                            <Link to={`/plant-info/${plant.id}`} className="text-decoration-none text-dark">
                                <div className="card-body">
                                    <h5 className="card-title">{plant.plantName}</h5>
                                    <p className="card-text">{plant.type}</p>
                                    <div className="d-flex justify-content-start">
                                        <Link to={`/gallery/${plant.id}`} className="btn btn-info">Галерея</Link>
                                        <Link to={`/dashboard/${plant.id}`} className="btn btn-success ">Дані</Link>
                                        <Link to={`/journal/${plant.id}`} className="btn btn-secondary">Журнал</Link>
                                    </div>
                                </div>
                            </Link>
                        </div>
                    ))}
                </div>
            </div>
        );
    }
}
