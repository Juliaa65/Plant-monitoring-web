import axios from 'axios';
import authHeader from './auth-header';

const API_URL = 'http://localhost:8085/api/camera';

class PhotoService {
    deletePhoto(photoId) {
        return axios.delete(API_URL +"/" + photoId, { headers: authHeader() });
    }

    getPhotos(plantId, startDate, endDate){
        const params = { startDate, endDate };
        return axios.get(API_URL+"/"+plantId+"/filter-images",{ params },{ headers: authHeader() });
    }

    createGif(plantId, startDate, endDate){
        const params = { startDate, endDate };
        return axios.get(API_URL+"/"+plantId+"/createGif",{ params },{ headers: authHeader() });
    }
}

export default new PhotoService();