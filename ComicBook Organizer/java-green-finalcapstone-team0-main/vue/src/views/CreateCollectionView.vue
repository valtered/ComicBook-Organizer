<template>
  <div class="create-collection">
    <h1>Create Collection</h1>

    <!-- Collection creation form -->
    <form @submit.prevent="createCollection">
      <label>*Collection Name:</label>
      <input v-model="collection.name" required />

      <label>Description:</label>
      <input v-model="collection.description" />

      <label>Privacy:</label>
      <select v-model="collection.privacy" required>
        <option value="Public">Public</option>
        <option value="Private">Private</option>
      </select>

      <button type="submit">Create Collection</button>
    </form>

    <!-- Collection display after creation -->
    <div v-if="newCollection.id">
      <h2>Your Collection: {{ newCollection.name }}</h2>
      <p>{{ newCollection.description }}</p>
      <p>Privacy: {{ newCollection.privacy }}</p>

      <!-- Display comics inside the collection -->
      <div v-if="newCollection.comics && newCollection.comics.length > 0">
        <h3>Comics in your collection:</h3>
        <ul>
          <li v-for="comic in newCollection.comics" :key="comic.comicId">
            {{ comic.title }} ({{ comic.issueNumber }}) by {{ comic.author }}
          </li>
        </ul>
      </div>

      <!-- Add comics to the collection -->
      <h3>Add Comics to Collection</h3>
      <form @submit.prevent="addComicToCollection">
        <label>*Comic Title:</label>
        <input v-model="newComic.title" required />

        <label>*Comic Author:</label>
        <input v-model="newComic.author" required />

        <label>*Comic Publisher:</label>
        <input v-model="newComic.publisher" required />

        <label>Comic Issue Number:</label>
        <input v-model="newComic.issueNumber" type="number" />

        <button type="submit">Add Comic</button>
      </form>
    </div>

    <!-- Link to view the collection list -->
    <router-link to="/collection" class="highlighted-link">View Collection List</router-link>
  </div>
</template>

<script>
export default {
  data() {
    return {
      collection: {
        name: '',
        description: '',
        privacy: 'Public',
      },
      newCollection: {}, // Store the created collection object
      newComic: {
        title: '',
        author: '',
        publisher: '',
        issueNumber: null,
      },
    };
  },
  methods: {
    // Create collection
    async createCollection() {
      try {
        const response = await fetch('http://localhost:9000/collections', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + localStorage.getItem('token'),
          },
          body: JSON.stringify(this.collection),
        });

        if (!response.ok) throw new Error('Failed to create collection');
        const data = await response.json();
        this.newCollection = data;
        this.$router.push(`collection/${data.id}`);
      } catch (error) {
        console.error('Error creating collection:', error);
      }
    },

    // Add comic to collection
    async addComicToCollection() {
      try {
        const response = await fetch(
          `http://localhost:9000/collections/${this.newCollection.id}/comics`,
          {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
              'Authorization': 'Bearer ' + localStorage.getItem('token'),
            },
            body: JSON.stringify(this.newComic),
          }
        );
        if (!response.ok) throw new Error('Failed to add comic');
        const comicData = await response.json();
        console.log('Comic added:', comicData);
        
        // Add the comic to the newCollection object for display
        if (!this.newCollection.comics) {
          this.newCollection.comics = []; // Initialize comics array if not already
        }
        this.newCollection.comics.push(comicData); // Add new comic to the collection
        
        this.newComic = {}; // Clear comic form after adding
      } catch (error) {
        console.error('Error adding comic:', error);
      }
    },
  },
};
</script>

<style scoped>
.create-collection {
  background-image: url('@/assets/create-collection-background.jpg');
  background-size: cover;
  background-position: center;
  text-align: center;
  padding: 20px;
  color: #d3d3d3; /* Light gray text */
  height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  gap: 30px;
  position: relative;
}

/* Title Styling */
h1 {
  font-size: 4em;
  color: #ffffff;
  margin-top: 20px;
  margin-bottom: 50px;
  font-family: 'Arial', sans-serif;
  background-color: rgba(0, 0, 0, 0.6);
  padding: 20px;
  border-radius: 8px;
  display: inline-block;
}

form {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background-color: rgba(0, 0, 0, 0.6);
  color: #d3d3d3;
}

/* Inputs, Select, and Buttons */
select,
input {
  width: 100%;
  padding: 12px;
  margin-top: 8px;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 1em;
  background-color: rgba(0, 0, 0, 0.6); /* Same background as the buttons */
  color: white;
}

/* Unified Button Styling */
button {
  background: rgba(0, 0, 0, 0.6);
  color: white;
  border: solid;
  padding: 15px 25px;
  border-radius: 8px;
  cursor: pointer;
  font-size: 18px;
  margin: 10px;
  transition: background 0.3s ease;
  font-family: 'Arial', sans-serif;
  width: 300px;
  box-sizing: border-box;
}

/* Hover Effect for Buttons */
button:hover {
  background: #007bff;
}

/* Link Styling */
.highlighted-link {
  display: inline-block;
  margin-top: 20px;
  text-decoration: none;
  font-size: 18px;
  color: #ffffff;
  background-color: rgba(0, 0, 0, 0.6);
  padding: 10px 20px;
  border-radius: 8px;
  font-family: 'Arial', sans-serif;
  border: solid;
  width: 300px;
  text-align: center;
}

/* Hover Effect for Links */
.highlighted-link:hover {
  background: #007bff;
  color: #ffffff;
  text-decoration: underline;
}
</style>
