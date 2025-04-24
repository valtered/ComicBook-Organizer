import axios from 'axios';

const http = axios.create({
  baseURL: import.meta.env.VITE_REMOTE_API
});

export default {

  login(user) {
    return http.post('/login', user)
  },

  register(user) {
    return http.post('/register', user)
  },

  createCollection(collection) {
    return http.post('/collections', collection);
  },

  getCollections() {
    return http.get('/collections');
  }

}
