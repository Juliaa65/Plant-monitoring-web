import axios from 'axios';
import authHeader from './auth-header';

const API_URL = 'http://localhost:8085/api/';

class UserService {

  getAllPlants() {

    return axios.get(API_URL + 'plants/allPlants', { headers: authHeader() });
  }
}

export default new UserService();
