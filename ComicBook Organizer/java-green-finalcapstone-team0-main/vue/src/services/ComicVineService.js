import axios from 'axios';


export default {
    comicSearch(searchTerm) {
        console.log('did it work?')
        return axios.get(`search?query=${searchTerm}`)
    }
}