<template>
  <div id="register" class="form-container">
    <form v-on:submit.prevent="register" class="form-box">
      <h1>Create Account</h1>
      <div role="alert" v-if="registrationErrors" class="alert">
        {{ registrationErrorMsg }}
      </div>
      <div class="form-input-group">
        <label for="username">Username</label>
        <input type="text" id="username" v-model="user.username" required autofocus />
      </div>
      <div class="form-input-group">
        <label for="password">Password</label>
        <input type="password" id="password" v-model="user.password" required />
      </div>
      <div class="form-input-group">
        <label for="confirmPassword">Confirm Password</label>
        <input type="password" id="confirmPassword" v-model="user.confirmPassword" required />
      </div>
      <button type="submit">Create Account</button>
      <p class="login-link">
        <router-link v-bind:to="{ name: 'login' }">Already have an account? Log in.</router-link>
      </p>
    </form>
  </div>
</template>

<script>
import authService from '../services/AuthService';

export default {
  data() {
    return {
      user: {
        username: '',
        password: '',
        confirmPassword: '',
        role: 'user', // Default to "Standard"
      },
      registrationErrors: false,
      registrationErrorMsg: 'There were problems registering this user.',
    };
  },
  methods: {
    register() {
      if (this.user.password !== this.user.confirmPassword) {
        this.registrationErrors = true;
        this.registrationErrorMsg = 'Password & Confirm Password do not match.';
      } else {
        authService
          .register(this.user)
          .then((response) => {
            if (response.status === 201) {
              this.$router.push({
                path: '/login',
                query: { registration: 'success' },
              });
            }
          })
          .catch((error) => {
            const response = error.response;
            this.registrationErrors = true;
            if (response.status === 400) {
              this.registrationErrorMsg = 'Bad Request: Validation Errors';
            }
          });
      }
    },
    clearErrors() {
      this.registrationErrors = false;
      this.registrationErrorMsg = 'There were problems registering this user.';
    },
  },
};
</script>

<style scoped>
/* Styling for the registration form */
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
  background-color: rgba(0, 0, 0, 0.622);
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
  background-color: hsla(0, 0%, 0%, 0.61);
  font-weight: bold;
  font-family: 'Courier New', Courier, monospace;
}

.form-input-group {
  margin-bottom: 1.5rem;
  text-align: left;
}

label {
  display: block;
  background-color: hsla(0, 0%, 0%, 0.692);
  background-clip: content-box;
  padding: 1px;
  font-size: 1rem;
  font-weight: bold;
  margin-bottom: 0.5rem;
  color: #ffffff;
  font-family: 'Courier New', Courier, monospace;
}

input, select {
  width: 100%;
  padding: 12px 16px;
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
  transition: border-color 0.3s ease;
  color: #000000;
}

input:focus, select:focus {
  outline: none;
  border-color: #4d90fe;
}

.alert {
  background-color: #f8d7da;
  color: #721c24;
  padding: 10px;
  margin-bottom: 20px;
  border-radius: 5px;
  font-size: 1rem;
  text-align: center;
}

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

.login-link {
  margin-top: 20px;
}

.login-link a {
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

.login-link a:hover {
  text-decoration: underline;
  background-color: #357ae8;
}
</style>
