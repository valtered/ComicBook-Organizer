<template>
  <div id="login" class="form-container">
    <form v-on:submit.prevent="login" class="form-box">
      <h1>Please Sign In</h1>
      
      <!-- Error Alerts -->
      <div role="alert" v-if="invalidCredentials" class="alert">
        Invalid username and password!
      </div>
      <div role="alert" v-if="this.$route.query.registration" class="alert">
        Thank you for registering, please sign in.
      </div>
      
      <!-- Username Input -->
      <div class="form-input-group">
        <label for="username">Username</label>
        <input type="text" id="username" v-model="user.username" required autofocus />
      </div>
      
      <!-- Password Input -->
      <div class="form-input-group">
        <label for="password">Password</label>
        <input type="password" id="password" v-model="user.password" required />
      </div>
      
      <!-- Sign In Button -->
      <button type="submit">Sign in</button>

      <!-- Button for Anonymous Access     <button type="button" @click="continueAsGuest">Continue as Guest</button>     -->
      

      <!-- Register Link -->
      <p class="register-link">
        <router-link v-bind:to="{ name: 'register' }">Need an account? Sign up.</router-link>
      </p>
    </form>
  </div>
</template>

<script>
import authService from "../services/AuthService";

export default {
  data() {
    return {
      user: {
        username: "",
        password: "",
      },
      invalidCredentials: false,
    };
  },
  methods: {
    login() {
      authService
        .login(this.user)
        .then((response) => {
          if (response.status === 200) {
            this.$store.commit("SET_AUTH_TOKEN", response.data.token);
            this.$store.commit("SET_USER", response.data.user);
            this.$router.push("/"); // Redirect to home after login
          }
        })
        .catch((error) => {
          const response = error.response;
          if (response.status === 401) {
            this.invalidCredentials = true; // Show error if credentials are incorrect
          }
        });
    },

    // Method to continue as a guest (anonymous user)
    continueAsGuest() {
      // Redirect to a public or guest-only page
      this.$router.push("/guest-home");  // Replace with your desired route
    },
  },
};
</script>

<style scoped>
/* Same styles as before, no change needed here */
.form-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f4f7fc;
  background-image: url('@/assets/the-flash-784-1.jpg');
  background-size: cover;
  background-position: center;
  padding: 0 20px;
}

.form-box {
  background-color: rgba(0, 0, 0, 0.422);
  border-radius: 8px;
  padding: 30px;
  max-width: 400px;
  width: 100%;
  text-align: center;
  color: #ffffff;
}

.form-box h1 {
  font-size: 1.8rem;
  margin-bottom: 20px;
  color: #ffffff;
  background-color: hsla(0, 0%, 0%, 0.693);
  font-weight: bold;
  font-family: 'Courier New', Courier, monospace;
}

.form-input-group {
  margin-bottom: 1.5rem;
  text-align: left;
}

label {
  display: block;
  background-color: hsla(0, 0%, 0%, 0.691);
  background-clip: content-box;
  padding: 1px;
  font-size: 1rem;
  font-weight: bold;
  margin-bottom: 0.5rem;
  color: #ffffff;
  align-content: center;
}

input {
  width: 100%;
  padding: 12px 16px;
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
  transition: border-color 0.3s ease;
  color: #333;
}

input:focus {
  outline: none;
  border-color: #4d90fe;
}

.alert {
  background-color: #0000008a;
  color: #721c24;
  padding: 10px;
  margin-bottom: 20px;
  border-radius: 5px;
  font-size: 1rem;
  text-align: center;
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
  background-color: #357ae8;
}

.register-link {
  margin-top: 20px;
}

.register-link a {
  /* Updated Button to match the other buttons */
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


.register-link a:hover {
  text-decoration: underline;
  background-color: #357ae8;
}

</style>
