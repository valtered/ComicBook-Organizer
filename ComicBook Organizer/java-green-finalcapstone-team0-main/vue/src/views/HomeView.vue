<template>
  <div class="home">
    <!-- Title with highlighted background -->
    <h1 class="highlighted-title">Comic Vault</h1>
    <div class="button-container">
    <button @click="goToCreateCollection">Create a New Collection</button>
    <button @click="goToAddComic">Add a Comic</button>
    <button @click="goToComicSearch">Search For Comics</button>
    

     <!--View Collection link with highlighted background -->

    <!-- <router-link to="/view-collection" class="highlighted-link">View Collection</router-link> -->
      


       
      <form @submit.prevent="goToViewCollection">
      

    <label class="Collection Selection">Select a Collection:</label>
    <select v-model="selectedCollectionId" required>
                <option value="">Select a collection</option>
                <option v-for="collection in collections" :key="collection.id" :value="collection.id">
                    {{ collection.name }}
                </option>
            </select>



      <button type="submit">GO!</button>
    </form>

   
    <router-link to="/collection" class="highlighted-link">View All Collections</router-link>
    </div>
        
    
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
           collections: [],  // to hold publisher list
           selectedCollectionId: null,
          };
       },
         

        
    created() {
        this.loadCollections();
    },
 
  methods: {
     
     loadCollections() {

CollectionService.loadCollections().then(
    (resp ) => {
        this.collections = resp.data;
    }
)
  },


    goToCreateCollection() {
      this.$router.push("/create-collection-view");
    },
    goToAddComic() {
      this.$router.push('/add-comic');
    },

    
    
    
    goToViewCollection() {
      // to a collection with the given ID
      if (this.selectedCollectionId) {
      this.$router.push({
        name: 'view-collection',
        params: { id: this.selectedCollectionId }  // users selects collection ID from drop down
      });
      
    } 
  },









    goToComicSearch() {
      this.$router.push("/comic-search");
    }
  }
  
};
</script>

<style scoped>
/* Apply the background image */
.home {
  background-image: url('@/assets/colors-background.jpg'); /* Adjusted path for assets folder */
  background-size: cover;
  background-position: center;
  text-align: center;
  padding: 20px;
  color: #d3d3d3; /* Light gray text */
  height: 100vh; /* Make sure it takes up full viewport height */
  display: flex;
  flex-direction: column;
  justify-content: flex-start; /* Keep everything aligned to the top */
  align-items: center; /* Center content horizontally */
  gap: 30px; /* Add some space between the elements */
  position: relative; /* Ensure overlay can be placed on top of background */
}

/* Title Styling with background for better visibility */
.highlighted-title {
  font-size: 6em; /* Make it twice as big */
  color: #ffffff; /* White color for better contrast */
  margin-top: 20px; /* Top margin for spacing */
  margin-bottom: 50px; /* Space between title and buttons */
  font-family: 'Arial', sans-serif;
  background-color: rgba(0, 0, 0, 0.6); /* Semi-transparent background to highlight text */
  padding: 20px; /* Padding around the text for better visibility */
  border-radius: 8px; /* Optional: round the corners */
  display: inline-block; /* So it only wraps the title text */
}

/* Button Styling */
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

label {
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

.button:nth-child(4) {
    background:  red;; /* Default gray color for the button */
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

/* Hover Effect for Buttons */
button:hover {
  background: #007bff; /* Blue background when hovering */
}

/* Styling for the router link */
.highlighted-link {
  display: inline-block;
  margin-top: 20px;
  text-decoration: none;
  font-size: 18px;
  color: #ffffff; /* White color for better contrast */
  background-color: rgba(0, 0, 0, 0.6); /* Semi-transparent background */
  padding: 10px 20px;
  border-radius: 8px; /* Optional: round the corners */
  font-family: 'Arial', sans-serif;
  border: solid;
  width: 300px;
}

/* Hover effect for router-link */
.highlighted-link:hover {
  background: #007bff; /* Blue background when hovering */
  color: #ffffff; /* Ensure text stays white */
  text-decoration: underline; /* Add underline on hover */
}


</style>
