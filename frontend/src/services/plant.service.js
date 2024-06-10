import axios from 'axios';
import authHeader from './auth-header';

const API_URL = 'http://localhost:8085/api/plants';

class PlantService {

    createPlant(plantName, type, info, system_url, camera_url, location, plantDate) {
        return axios.post(API_URL, {
            plantName,
            type,
            info,
            system_url,
            camera_url,
            location,
            plantDate
        }, { headers: authHeader() });
    }

    getPlantName(plantId){
        return axios.get(API_URL + "/getPlantName/" + plantId, {headers: authHeader() });
    }

    deletePlant(plantId) {
        return axios.delete(API_URL + "/" + plantId, { headers: authHeader() });
    }

    getPlantInfo(plantId){
        return axios.get(API_URL + "/plant-info/"+plantId, { headers: authHeader() });
    }

    editPlant(id, plantName, type, info, system_url, camera_url, location, plantDate) {
        return axios.put(API_URL, {
            id,
            plantName,
            type,
            info,
            system_url,
            camera_url,
            location,
            plantDate
        }, { headers: authHeader() });
    }

    capturePhoto(id){
        return axios.post("http://localhost:8085/api/camera/capturePhoto", {
             id},
            { headers: authHeader()}
        );
    }

    getPhotos(plantId){
        return axios.get("http://localhost:8085/api/camera/images/"+plantId, { headers: authHeader() });
    }

    getSensorData(plantId, startDate, endDate, interval) {
        const params = { plantId, startDate, endDate, interval};
        return axios.get("http://localhost:8085/api/sensor-data",{ params },{ headers: authHeader() });
    }


}

export default new PlantService();