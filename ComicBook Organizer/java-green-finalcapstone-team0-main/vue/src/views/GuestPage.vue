<template>
  <div class="guest-page">
    
    
    <h1>Welcome to the Comic Vault!</h1>
    <p>An easy way to keep all your comics together and organized </p>
    
    <!-- Buttons container for login and sign-up -->
    <div class="auth-buttons">
      <router-link to="/Login" class="login-button">LOGIN</router-link>
      <router-link to="/Register" class="signup-button">SIGN UP</router-link>
    </div>

    <!-- Comics Section -->
    <div v-if="comics.length" class="comics">
      <h2>Comic Books to Check Out</h2>
      <ul>
        <li v-for="comic in comics" :key="comic.id" class="comic-item">
          <h3>{{ comic.title }}</h3>
          <p>{{ comic.description }}</p>
        </li>
      </ul>
    </div>
    
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      comics: [], // State to hold comic books
    };
  },
  created() {
    this.fetchComics(); // Fetch comics when the component is created
  },
  methods: {
    fetchComics() {
      
      axios.get('/collections/view')
      .then(response => {
          this.comics = response.data; // Set comics data
        })
        .catch(error => {
          console.error("There was an error fetching the comics:", error);
        });
    },
  },
};
</script>

<style scoped>



.guest-page {
  display: flex;
  background-color: rgb(233, 192, 199);
  
  flex-direction: column;
  justify-content: flex-start;  /* Align at the top */
  align-items: center;
  min-height: 100vh;
  width: 100%;
  text-align: center;
  color: black; /* Dark text for contrast */
  position: relative;
  padding-top: 20px;
}

.guest-page p {
  background-color: rgba(39, 30, 35, 0.5); /* Light background with opacity */
  color: black ; /* Dark text for contrast */
  padding: 15px;
  border-radius: 8px;
  margin: 10px 0;
  z-index: 2;
}

.guest-page h1 {
  font-size: 4rem;
  color:white; /* Vibrant orange color for header */
  margin-bottom: 10px;
}

.auth-buttons {
  position: absolute;  
  top: 20px;
  right: 20px;
  display: flex;
  gap: 10px;
}

.login-button {
  background-color: #4c4855;  /* Bright blue for login button */
  color: rgb(255, 0, 0);
  padding: 10px 20px;
  text-decoration: none;
  border-radius: 5px;
  font-weight: bold;
  transition: background-color 0.3s ease;
}

.login-button:hover {
  background-color: rgb(38, 0, 128);  /* Darker blue on hover */
}

.signup-button {
  background-color: #4c4855;  /* Fresh green for sign-up button */
  color: white;
  padding: 10px 20px;
  text-decoration: none;
  border-radius: 5px;
  font-weight: bold;
  transition: background-color 0.3s ease;
}

.signup-button:hover {
  background-color: rgb(38, 0, 128);  /* Darker green on hover */
}

.comics {
  margin-top: 20px;
  padding: 20px;
  background-color: #8d8d8d;  /* White background for comics section */
  border-radius: 10px;
  box-shadow: 0px 4px 12px rgba(0, 0, 0, 0.1);
}

.comic-item {
  background-color: #808080; /* Light gray for comic items */
  padding: 15px;
  margin-bottom: 10px;
  border-radius: 4px;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
}

.comic-item h3 {
  margin: 0 0 10px;
  color: #000000; /* Dark text for comic titles */
}

.comic-item p {
  margin: 0;
  color: #060707; /* Lighter text for comic descriptions */
}

.guest-page a {
  color: #000000; /* Blue link color */
  text-decoration: none;
  font-weight: bold;
  border-radius: 5px;
}

.guest-page a:hover {
  text-decoration: underline;
  color: #2980B9; /* Darker blue on hover */
}





</style>
