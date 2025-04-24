<template>
    <div>
        <h2>Add a Comic</h2>
        <form @submit.prevent="addComic">
            <!-- Publisher Dropdown -->
            <label>*Publisher:</label>
            <select v-model="selectedPublisher" required>
                <option value="">Select a publisher</option>
                <option v-for="collection in collections" :key="collection.id" :value="collection.id">
                    {{ collection.name }}
                </option>
            </select>

            <label>*Title:</label>
            <input v-model="comic.title" required />

            <label>Issue Number:</label>
            <input type="number" v-model="comic.issueNumber" />

            <label>Character:</label>
            <input v-model="comic.character" />

            <label>Author/Creator:</label>
            <input v-model="comic.author" />

            <button type="submit">Add Comic</button>
        </form>

        <router-link to="/collection">View Collection</router-link>
    </div>
</template>

<script>

import CollectionService from '../services/CollectionService.js';
export default {
    data() {
        return {
            comic: {
                title: '',
                issueNumber: '',
                character: '',
                author: '',
            },
            selectedPublisher: '',
            collections: [],  // Array to hold publisher list
        };
    },

    created() {
        this.loadCollections();
    },

    async mounted() {
        try {
            const response = await fetch('http://localhost:9000/collections/user', {
                headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') }
            });

            if (!response.ok) throw new Error('Failed to fetch collections');

            this.collections = await response.json();
            console.log('Collections loaded:', this.collections);
        } catch (error) {
            console.error('Error fetching collections:', error);
        }
    },

    methods: {

        loadCollections() {

            CollectionService.loadCollections().then(
                (resp ) => {
                    this.collections = resp.data;
                }
            )
            // axios.get('/collections')
            // .then(response => {
            //     this.collections = response.data;
            // })
            // .catch(error => {
            //     console.error("Error loading collections:", error);
            // });
            },

             addComic() {
         
                const collection = this.collections.find(c => c.id === this.selectedPublisher);
                if (!collection) throw new Error("Collection not found");

            console.log('selected collection objec')
            console.log(collection)

            console.log('method running?')

                const requestBody = {
                        title: this.comic.title,
                        publisher: this.selectedPublisher,
                        issueNumber: this.comic.issueNumber,
                        character_name: this.comic.character,  // Adjust if needed for the API
                        author: this.comic.author
                    }

                CollectionService.addComic(requestBody, collection.id).then(
                    (resp) => {
                        console.log('it worked?')
                        // router push goes here
                       this.$router.push(`/collection/${collection.id}`);

                    }
                );



            //     const response = await fetch(`http://localhost:9000/collections/${collection.id}/comics`, {
            //         method: 'POST',
            //         headers: {
            //             'Content-Type': 'application/json',
            //             'Authorization': 'Bearer ' + localStorage.getItem('token')
            //         },
            //         body: JSON.stringify({
            //             title: this.comic.title,
            //             publisher: this.selectedPublisher,
            //             issueNumber: this.comic.issueNumber,
            //             character_name: this.comic.character,  // Adjust if needed for the API
            //             author: this.comic.author
            //         })
            //     });

            //     if (!response.ok) throw new Error('Failed to add comic');

            //     const data = await response.json();
            //     console.log('Comic added:', data);

            //     // Clear the form on success
            //     this.comic = { title: '', issueNumber: '', character: '', author: '' };  
            //     this.selectedPublisher = '';  // Reset publisher selection

            //     // Redirect to /home after adding the comic
            //     this.$router.push(`/collection/${this.newCollection.id}`);
            // } catch (error) {
            //     console.error('Error adding comic:', error);
            // }
         }
        }
    
       
    }

</script>

<style scoped>
h2 {
    text-align: center;
    color: #ffffff;
    margin-top: 20px;
    background-color: rgba(0, 0, 0, 0.8);
}

div {
    margin-top: 20px;
    text-align: center;
    background-image: url('@/assets/the-flash-784-1.jpg');
    background-size: cover;
    background-attachment: fixed;
    background-position: center;
    background-color: #2c2c2c;
}

form {
    max-width: 600px;
    margin: 0 auto;
    padding: 20px;
    border: 1px solid #ddd;
    border-radius: 8px;
    background-color: rgba(0, 0, 0, 0.8); /* Dark background for form */
    color: #d3d3d3; /* Light gray text */
}

label {
    display: block;
    margin-top: 20px;
    font-size: 1.2em;
}

select, input {
    width: 100%;
    padding: 10px;
    margin-top: 12px;
    background-color: #2c2c2c; /* Dark background for input fields */
    color: white; /* White text for input fields */
    border: 1px solid #ddd;
    border-radius: 4px;
}

/* Updated Button to match the other buttons */
button {
  background:  rgba(0, 0, 0, 0.6);; /* Default gray color for the button */
  color: white;
  border: solid;
  padding: 15px 25px; /* Increase padding to make buttons larger */
  border-radius: 8px;
  cursor: pointer;
  font-size: 18px;
  margin: 10px;
  transition: background 0.3s ease; /* Smooth transition for hover effect */
  font-family: 'Arial', sans-serif;
  width: 300px;
}
button:hover {
    background-color: #007bff; /* Same hover effect as other buttons */
}

/* Custom style for the "View Collection" link */
.highlighted-link {
    display: inline-block;
    margin-top: 20px;
    text-decoration: none;
    font-size: 1.5em;
    color: #ffffff;
    background-color: rgba(0, 0, 0, 0.8); /* Dark background for the link */
    padding: 10px 20px;
    border-radius: 8px;
    background-color: #2c2c2c; /* Dark background for the link */
}

.highlighted-link:hover {
    background: #007bff; /* Blue background on hover */
    color: #ffffff;
    text-decoration: underline;
}
</style>
