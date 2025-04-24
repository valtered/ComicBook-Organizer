import axios from 'axios';




export default {

    addComic(comicBody, collectionId) {

        console.log('ANDY DEBUG')
        console.log(collectionId)

        console.log(comicBody)
        return axios.post(`/collections/${collectionId}/comics`, comicBody)
    },

    loadCollections() {

        return axios.get('/collections');
    },

    // For Guests to view  public collections only

    getPublicCollections() {
        return axios.get('/collections/view');  
    },

    fetchUserCollections(token) {
        return axios.get('/collections/user', {
            headers: {
                'Authorization': `Bearer ${token}`,
            }
        });
    },



}