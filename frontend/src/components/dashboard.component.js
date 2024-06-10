import React, { Component } from 'react';
import { Line } from 'react-chartjs-2';
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend,
    TimeScale,
} from 'chart.js';
import 'chartjs-adapter-date-fns';
import PlantService from "../services/plant.service";


ChartJS.register(
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend,
    TimeScale
);

export default class SensorCharts extends Component {
    constructor(props) {
        super(props);
        this.state = {
            sensorData: [],
            startDate: new Date().toISOString().split('T')[0],
            endDate: new Date().toISOString().split('T')[0],
            interval: 'hour',
            error: null
        };
    }

    componentDidMount() {
        this.fetchSensorData();
    }

    getPlantIdFromUrl() {
        const url = window.location.href;
        const parts = url.split('/');
        return parts[parts.length - 1];
    }

    fetchSensorData = () => {
        const plantId = this.getPlantIdFromUrl();
        const { startDate, endDate, interval } = this.state;

        PlantService.getSensorData(plantId, startDate, endDate, interval)
            .then(response => {
                this.setState({ sensorData: response.data });
            })
            .catch(error => {
                this.setState({ error: 'На даний період дані відсутні. Оберіть, будь ласка, інші межі.' });
            });
    }

    handleStartDateChange = (e) => {
        this.setState({ startDate: e.target.value });
    };

    handleEndDateChange = (e) => {
        this.setState({ endDate: e.target.value });
    };

    handleIntervalChange = (e) => {
        this.setState({ interval: e.target.value }, this.fetchSensorData);
    };

    handleFilterSubmit = (e) => {
        e.preventDefault();
        this.fetchSensorData();
    }

    getTimeUnit = () => {
        const { interval } = this.state;
        if (interval === 'hour') return 'hour';
        if (interval === 'day') return 'day';
        if (interval === 'month') return 'month';
        if (interval === 'year') return 'year';
        return 'hour';
    }

    prepareChartData = (sensorData, dataKey, label) => {
        const timestamps = sensorData.map(entry => entry.timestamp);
        const data = sensorData.map(entry => entry[dataKey]);

        return {
            labels: timestamps,
            datasets: [
                {
                    label: label,
                    data: data,
                    fill: false,
                    backgroundColor: 'rgba(75,192,192,0.4)',
                    borderColor: 'rgba(75,192,192,1)'
                }
            ]
        };
    }

    getChartOptions = (timeUnit, yLabel) => {
        return {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                x: {
                    type: 'time',
                    time: {
                        unit: timeUnit,
                        tooltipFormat: 'yyyy-MM-dd HH:mm',
                        displayFormats: {
                            hour: 'HH:mm',
                            day: 'dd-MM',
                            month: 'MM',
                            year: 'yyyy',
                        },
                    },
                    ticks: {
                        font: {
                            size: 12,
                            family: 'Arial',
                            style: 'bold',
                        },
                        color: '#333',
                    },
                },
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: yLabel,
                        font: {
                            size: 14,
                            weight: 'bold'
                        }
                    },
                    ticks: {
                        font: {
                            size: 12,
                            family: 'Arial',
                            style: 'italic',
                        },
                        color: '#333',
                    },
                }
            },
            plugins: {
                legend: {
                    labels: {
                        font: {
                            size: 20,
                            family: 'Arial',
                            weight: 'bold'
                        },
                        color: '#333',
                    }
                }
            },
        };
    }

    render() {
        const { sensorData, error, startDate, endDate, interval } = this.state;

        const timeUnit = this.getTimeUnit();
        const temperatureChartData = this.prepareChartData(sensorData, 'avgTemperature', 'Температура');
        const humidityChartData = this.prepareChartData(sensorData, 'avgHumidity', 'Вологість');
        const ds18b20TemperatureChartData = this.prepareChartData(sensorData, 'avgDs18b20Temperature', 'Температура ґрунту');
        const soilMoistureChartData = this.prepareChartData(sensorData, 'avgSoilMoisture', 'Вологість ґрунту');
        const photoresistorValueChartData = this.prepareChartData(sensorData, 'avgPhotoresistorValue', 'Рівень освітлення');

        return (
            <div>
                <button className="btn back-btn" onClick={() => window.history.back()}></button>
                <h2 className="datacharts_title">Діаграми показників навколишнього середовища</h2>

                <form onSubmit={this.handleFilterSubmit} className="filter-container">
                    <label>
                        З
                        <input type="date" value={startDate} onChange={this.handleStartDateChange}/>
                    </label>
                    <label>
                        До
                        <input type="date" value={endDate} onChange={this.handleEndDateChange}/>
                    </label>
                    <label>
                        Період
                        <div className="select-container">
                            <select value={interval} onChange={this.handleIntervalChange}>
                                <option value="hour">Година</option>
                                <option value="day">День</option>
                                <option value="month">Місяць</option>

                            </select>
                        </div>
                    </label>
                    <button type="submit">Фільтрувати</button>
                </form>
                {error && <div className="error-message">Error: {error}</div>}
                {sensorData.length === 0 ? (
                    <div className="no-data-message">Відсутні дані, обріть інший період, будь ласка</div>
                ) : (
                    <div className="chart-container">

                        <div className="chart">
                            <Line data={temperatureChartData} options={this.getChartOptions(timeUnit, "°C")}/>
                        </div>

                        <div className="chart">
                            <Line data={humidityChartData} options={this.getChartOptions(timeUnit, "%")}/>
                        </div>

                        <div className="chart">
                            <Line data={ds18b20TemperatureChartData} options={this.getChartOptions(timeUnit, "°C")}/>
                        </div>

                        <div className="chart">
                            <Line data={soilMoistureChartData} options={this.getChartOptions(timeUnit, "%")}/>
                        </div>

                        <div className="chart">
                            <Line data={photoresistorValueChartData} options={this.getChartOptions(timeUnit, "%")}/>
                        </div>
                    </div>
                )}
            </div>
        );
    }
}
