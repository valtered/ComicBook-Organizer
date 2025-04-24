<template>
    
    <div>
      <h1>Search For Comics</h1>
      <input v-model="searchTerm" placeholder="Enter a Comic or Character" />
      
      <button @click="searchComics">Search</button>
  
      <button v-if="results.length > 0" @click="selectAll">Select All</button>
  
      <div v-if="loading">Loading...</div>
      <div v-else-if="errorMessage" class="error">{{ errorMessage }}</div>
  
      <label>*Collection:</label>
  
      <select v-model="selectedCollectionId" required>
        <option value="" disabled hidden> Select a Collection</option>
        <option v-for="collection in collections" :key="collection.id" :value="collection.id">
          {{ collection.name }}
        </option>
      </select>
  
      <ul v-if="results.length > 0">
        <li v-for="comic in results" :key="comic.id || comic.title">
          <h3>{{ comic.title }}</h3>
          <img :src="comic.imageUrl" alt="Comic Cover" v-if="comic.imageUrl" />
          
          <button @click="addComic(comic)">Add to Collection</button>
        </li>
      </ul>
  
      <!-- Added button for "Add Comic" -->
      <button @click="addComic(comic)">Add Comic</button>
           <h1>Search For Comics</h1>


           

           <input v-model="searchTerm" placeholder="Enter a Comic or Character" />

            <button @click="searchComics">Search</button>

            



            <button v-if="results.length > 0" @click="selectAll">Select All</button>

            <div v-if="loading">Loading...</div>
            <div v-else-if="errorMessage" class="error">{{ errorMessage }}</div>

            <label>*Collection:</label>
           
            <select class v-model="selectedCollectionId" required>
              <option value="" disabled hidden> Select a Collection</option>
                <option v-for="collection in collections" :key="collection.id" :value="collection.id">
                    {{ collection.name }}
                </option>
            </select>

            



                <ul v-if="results.length > 0">
                <li v-for="comic in results" v-bind:key="comic.id || comic.title">
                    <input type="checkbox" v-model="selectedComics" :value="comic" />
                    <h3>{{ comic.title }}</h3>
                    <img :src="comic.imageUrl" alt="Comic Cover" v-if="comic.imageUrl" />
                    
                    <button v-on:click="addComic(comic)">Add to Collection</button>
                </li>
            </ul>

            <button  v-on:click="addComic(comic)">Add Comic</button>
    </div>






   








</template>

<script>

import CollectionService from '../services/CollectionService.js';
import ComicVineService from '../services/ComicVineService.js';
export default {
    data() {
      return {
        searchTerm : '',
        results: [],
        loading: false,
        errorMessage:"",
      
    collections: [],
    selectedCollectionId: "",
    selectedComics: [],
    selectedCollection : ''
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
},
    async searchComics() {
        if(!this.searchTerm) {
            this.errorMessage = "Please enter something to search.";
            return;
        }

        this.loading = true;
        this.errorMessage = "";
        this.results = [];

        try {
            const response = await fetch (`http://localhost:9000/api/search/issues?searchTerm=${encodeURIComponent(this.searchTerm)}`);
            if (!response.ok) throw new Error(`API error: ${response.status}`);

            this.results = await response.json();
        } catch (error) {
            this.errorMessage = "Failed to retrieve results. Please try again.";
        }finally {
            this.loading = false;
        }
    },
    async fetchCollections() {
        try {
            const response = await fetch ("/api/collections");
            if (!response.ok) throw new Error("Failed to fetch collections");

            this.collections = await response.json();
        } catch (error) {
            console.error("Error fetching collections:", error);
        }
    },

             addComic(comicObject) {
                

               

                console.log("Received Comic Object:", comicObject);

                if (!comicObject || typeof comicObject !== "object") {
                    console.error("Comic Object undefined or missing title", comicObject);
                    alert("No comic selected or data missing");
                    return;
                }
                
    
                

                console.log("method running?");
        
                const requestBody = {
                        title: comicObject.title,
                        publisher: this.selectedPublisher,
                        issueNumber: comicObject.issueNumber,
                        character_name: comicObject.character,  // Adjust if needed for the API
                        author: comicObject.author,
                        image_url: comicObject.imageUrl
                    };

                    console.log("Request Body:", requestBody);
                    console.log("Submitting comic to collection:", this.selectedCollectionId);

                CollectionService.addComic(requestBody, this.selectedCollectionId)
                .then(() => {
                        console.log('Comic Added!')
                        // router push goes here
            
                       this.$router.push(`/collection/${this.selectedCollectionId}`);

                    }).try (
                        (err) => {
                            console.log('something happened while adding comic')
                        }
                    )
                    
                    
             },
                

mounted() {
    this.fetchCollections();
}
             
             }
}



</script>

<style scoped>
/* General Reset */
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}

/* Main Container (Take Full Page Height) */
div {
    background-image: url('@/assets/pink.jpg');
    background-color: #fff;
    background-size: cover;
    padding: 40px;
    border-radius: 10px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    
    height: 90%; /* Take up 90% of the page height */
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-items: center;
    gap: 30px;
    text-align: center;
    overflow-y: auto; /* Allow scrolling if content overflows */
    margin: 0 auto; /* Center the container horizontally */


    background-position: center; /* Ensure the background image is centered */
    height: 100vh;
}

html, body {
    height: 100%;
    margin: 0;
    background-image: url('@/assets/pink.jpg');
    background-size: cover;
    background-position: center; /* Center the background image */
    background-repeat: no-repeat; /* Ensure it doesn't repeat */
}



/* Title */
h1 {
    font-size: 48px; /* Larger font size */
    font-weight: bold;
    color: #ffffff;
    background-color: hsla(0, 0%, 0%, 0.443); /* Yellow background */
    padding: 10px; /* Add padding for better readability */
    border-radius: 5px; /* Rounded corners */
    margin-bottom: 30px; /* More space between title and input */
}

/* Form Styling */
input[type="text"], select, button {
    padding: 20px; /* Increase padding for larger input and button */
    font-size: 24px; /* Larger font size for better readability */
    width: 100%;
    max-width: 800px; /* Max width for inputs */
    border: 1px solid #ccc; /* Light gray border */
    border-radius: 5px;
    margin-bottom: 20px;
    box-sizing: border-box;
    outline: none; /* Remove focus outline */
}

/* Button Container */
.button-container {
    display: flex;
    justify-content: center; /* Center the buttons horizontally */
    align-items: center; /* Vertically center the buttons */
    gap: 20px; /* Adds space between buttons */
    flex-wrap: wrap; /* Allow buttons to wrap on smaller screens */
    width: 100%;
    max-width: 900px; /* Set a max width for the button container */
    margin-top: 30px; /* Add spacing above the buttons */
    margin-bottom: 30px; /* Add spacing below the buttons */
}

/* Button Styling */
button {
    background: rgba(0, 0, 0, 0.6); /* Default gray color for the button */
    color: white;
    border: solid;
    padding: 15px 25px; /* Increase padding to make buttons larger */
    border-radius: 8px;
    cursor: pointer;
    font-size: 18px;
    margin: 10px;
    transition: background 0.3s ease; /* Smooth transition for hover effect */
    font-family: 'Arial', sans-serif;
    width: auto; /* Allow button width to adjust based on text length */
    min-width: 150px; /* Set a minimum width for buttons */
}

/* Button Hover Effect */
button:hover {
    background-color: #555;
}

/* Error Message Styling */
.error {
    color: rgb(0, 0, 0);
    background-color: #000000; /* Light red background */
    padding: 10px; /* Add padding for better readability */
    border-radius: 5px; /* Rounded corners */
    margin-top: 10px;
    text-align: center;
}


/* Comic Grid (Flexbox/Grid) */
ul {
    list-style: none;
    padding: 0;
    display: grid;
    grid-template-columns: repeat(5, 1fr);
    gap: 20px;
    margin-top: 20px;
    width: 100%;
    justify-items: center; /* Center the items within the grid */
}

@media (max-width: 1200px) {
    ul {
        grid-template-columns: repeat(4, 1fr);
    }
}

@media (max-width: 1000px) {
    ul {
        grid-template-columns: repeat(3, 1fr);
    }
}

@media (max-width: 800px) {
    ul {
        grid-template-columns: repeat(2, 1fr);
    }
}

@media (max-width: 600px) {
    ul {
        grid-template-columns: 1fr;
    }
}

/* Comic Item Styling */
li {
    display: flex;
    flex-direction: column;
    align-items: center;
    background-color: #fff;
    border-radius: 8px;
    padding: 15px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    transition: transform 0.3s ease, box-shadow 0.3s ease;
    text-align: center;
    width: 100%;
    max-width: 250px; /* Limiting max-width */
    margin: 0 auto; /* Center the comic item */
}

/* Hover Effect for Comic Item */
li:hover {
    transform: translateY(-5px);
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.15);
}

/* Comic Image */
img {
    max-width: 100%;
    height: auto;
    border-radius: 5px;
    margin-bottom: 10px;
}

/* Comic Title */
h3 {
    font-size: 20px;
    font-weight: bold;
    color: #444;
    background-color: #e1f5fe; /* Light blue background */
    padding: 5px; /* Add padding for better readability */
    border-radius: 5px; /* Rounded corners */
    margin-bottom: 10px;
}

/* Select Dropdown Styling */
select {
    padding: 8px; /* Reduced padding to make it smaller */
    font-size: 16px; /* Smaller font size for the dropdown */
    width: 100%; /* Make it span the full width of its container */
    max-width: 500px; /* Set a max-width to limit its size */
    border-radius: 5px;
    border: 1px solid #ccc;
    margin-bottom: 20px;
    box-sizing: border-box;
    outline: none; /* Remove focus outline */
}
</style>
