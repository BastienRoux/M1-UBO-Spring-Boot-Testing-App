<template>
  <div class="login-page">
    <div class="login-card card">
      <h1>Connexion</h1>
      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label>Pseudo:</label>
          <input v-model="credentials.pseudo" type="text" required>
        </div>
        <div class="form-group">
          <label>Mot de passe:</label>
          <input v-model="credentials.password" type="password" required>
        </div>
        <div v-if="error" class="error">{{ error }}</div>
        <button type="submit" class="btn btn-primary btn-block">Se connecter</button>
      </form>
      <p class="register-link">
        Pas encore de compte? 
        <router-link to="/register">S'inscrire</router-link>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const credentials = ref({
  pseudo: '',
  password: ''
})

const error = ref(null)

async function handleLogin() {
  try {
    error.value = null
    const success = await authStore.login(credentials.value.pseudo, credentials.value.password)
    if (success) {
      router.push('/')
    } else {
      error.value = 'Identifiants incorrects'
    }
  } catch (err) {
    error.value = 'Erreur lors de la connexion'
  }
}
</script>

<style scoped>
.login-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 60vh;
}

.login-card {
  width: 100%;
  max-width: 400px;
}

.login-card h1 {
  text-align: center;
  margin-bottom: 2rem;
}

.btn-block {
  width: 100%;
  margin-top: 1rem;
}

.register-link {
  text-align: center;
  margin-top: 1.5rem;
}

.register-link a {
  color: #667eea;
  text-decoration: none;
  font-weight: 500;
}
</style>
