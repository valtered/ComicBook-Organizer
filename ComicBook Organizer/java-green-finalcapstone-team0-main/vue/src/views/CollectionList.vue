<template>
  <div class="collection-list">
    <h1>Your Collections</h1>

    <!-- Display a message if there are no collections -->


    <div v-if="!collections.length" class="no-collections">
  <p>You have no collections yet. Start by creating one!</p>
  <router-link to="/create-collection-view" class="create-collection-link">Create a Collection</router-link>
</div>


    <!-- Display a grid of collections -->


    <div v-else class="collections-grid">
      <div v-for="collection in collections" :key="collection.collection_id" class="collection-card">
        <h3>{{ collection.name }}</h3>
        <p>{{ collection.description }}</p>


       
        <!-- Link to view the collection details -->

        <router-link :to="{ name: 'view-collection', params: { id: collection.id } }" class="view-collection-link">
          View Collection
        </router-link>
      </div>
    </div>
  </div>
</template>






<script>

import axios from 'axios';




  export default {
    name: 'CollectionList',
    data() {
      return {
        collections: []
      };
    },
    created() {
      this.fetchCollections();
    },
    methods: {
      fetchCollections() {
        // Get the token from localStorage for authentication
        const token = localStorage.getItem('token');
       
        // Check if the token exists
        if (token) {
          // Make the API call to the backend endpoint to fetch the user's collections
          axios.get('/collections/user', {
            headers: {
             'Authorization': `Bearer ${token}`  // Add the token to the request headers
            }
          })
           .then(response => {
              this.collections = response.data;  // Store the fetched collections
            })
            .catch(error => {
             console.error('Error fetching collections:', error);
            });
        } else {
          console.error('No token found');
        }
      }
    }
  };







</script>

<style scoped>

.collection-list {
  width: 100%; /* Ensure the container spans the full width */
  height: 100vh; /* Full viewport height */
  margin: 0; /* Remove any margin */
  padding: 40px;
  box-sizing: border-box; /* makes the image fit the size of screen*/
  text-align: center;
  background-image: url('@/assets/coler1.jpg'); /* Ensure the correct path */
  background-size: cover;
  background-position: center center;
  background-attachment: fixed;
  border-radius: 8px;
  background-repeat: no-repeat;
  height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

h1 {
  display: inline-block;
  background-color: rgba(0, 0, 0, 0.5); /* More translucent background */
  color: white;
  font-weight: bold;
  padding: 20px 40px; /* Add some padding to make it more spacious */
  font-family: 'Helvetica Neue', Arial, sans-serif; /* A more modern and elegant font */
  font-size: 3.5em; /* Increase the font size */
  text-transform: uppercase; /* Make the text uppercase for a stronger impact */
  letter-spacing: 2px; /* Slightly increase letter spacing for a cleaner look */
  margin: 0; /* Remove any default margin */
  width: 100%; /* Make the header span the entire width */
  text-align: center; /* Center the text */
  position: absolute; /* Use absolute positioning to move the header */
  top: 20px; /* Adjust the vertical position */
  left: 0; /* Align it to the left */
  right: 0; /* Align it to the right */
  box-sizing: border-box; /* Include padding in width calculation */
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.6); /* Add a subtle text shadow for depth */
}


.no-collections {
  background-color: rgba(0, 0, 0, 0.643);
  padding: 40px;
  border-radius: 12px;
  box-shadow: 0 4px 10px rgba(0, 0, 0,  0.643);
  color: #ffffff;
  font-size: 1.2em;
  text-align: center;
}

.create-collection-link {
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

.create-collection-link:hover {
  background-color: #007bff;
}

.collections-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 30px;
  margin-top: 40px;
  outline: white;
}

.collection-card {
  background-color: white;
  border-radius: 12px;
  padding: 30px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.643);
  text-align: left;
  transition: transform 0.3s ease, box-shadow 0.3s ease, background-color 0.3s ease; /* Smooth transition */
}

.collection-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.643);
}

.collection-card h3 {
  color: #2C3E50;
  font-size: 2.5em; /* Larger font size for the collection name */
  margin-bottom: 10px;
  font-weight: 600;
  text-align: center; /* Center the title horizontally */
  margin-top: 0;
}

.collection-card {
  background: rgba(220, 230, 231, 0.85); /* Soft background with some transparency */
  padding: 30px; /* Increased padding for more spacious content */
  border-radius: 15px; /* Softer, more rounded corners */
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.1); /* Larger shadow for a floating effect */
  margin-bottom: 30px;
  text-align: center; /* Center the text horizontally */
  transition: transform 0.3s ease, box-shadow 0.3s ease, background-color 0.3s ease; /* Smooth transitions */
  display: flex;
  flex-direction: column;
  justify-content: center; /* Center content vertically */
  height: 100%; /* Ensure the card stretches to fill the available space */
}

.collection-card:hover h3 {
  color: #007bff; /* Change color to blue on hover */
}

.collection-card:hover p {
  color: #555; /* Slightly darker color on hover for better readability */
}

.view-collection-link {
  display: inline-block;
  margin-top: 20px;
  text-decoration: none;
  color: #ffffff;
  background-color: rgba(0, 0, 0, 0.6);  /* Same background as other buttons */
  padding: 12px 24px;
  border-radius: 8px;
  font-size: 1em;
  transition: background-color 0.3s ease;
  width: 300px; /* Match the button size from other components */
  text-align: center;
  box-sizing: border-box;
}

.view-collection-link:hover {
  background-color: #007bff;
}

h1 {
  display: inline-block;
  background-color: rgba(0, 0, 0, 0.643); /* Solid background */
  color: rgb(255, 255, 255);
  font-weight: bold;
  padding: 10px 20px;
  font-family: 'Gill Sans', 'Gill Sans MT', Calibri, 'Trebuchet MS', sans-serif;
} 

div {
  outline-color: white;
}

</style>
