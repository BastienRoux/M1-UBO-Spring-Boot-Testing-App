<template>
  <div class="register-page">
    <div class="register-card card">
      <h1>Inscription</h1>
      <form @submit.prevent="handleRegister">
        <div class="form-group">
          <label>Pseudo (identifiant):</label>
          <input v-model="formData.pseudo" type="text" required>
        </div>
        <div class="form-group">
          <label>Nom:</label>
          <input v-model="formData.lastName" type="text" required>
        </div>
        <div class="form-group">
          <label>Prénom:</label>
          <input v-model="formData.firstName" type="text" required>
        </div>
        <div class="form-group">
          <label>Âge:</label>
          <input v-model.number="formData.age" type="number" min="13" required>
        </div>
        <div class="form-group">
          <label>Adresse:</label>
          <input v-model="formData.address" type="text" required>
        </div>
        <div class="form-group">
          <label>Mot de passe:</label>
          <input v-model="formData.password" type="password" required minlength="6">
        </div>
        <div class="form-group">
          <label>Confirmer le mot de passe:</label>
          <input v-model="confirmPassword" type="password" required>
        </div>
        <div v-if="error" class="error">{{ error }}</div>
        <button type="submit" class="btn btn-primary btn-block">S'inscrire</button>
      </form>
      <p class="login-link">
        Déjà un compte? 
        <router-link to="/login">Se connecter</router-link>
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

const formData = ref({
  pseudo: '',
  firstName: '',
  lastName: '',
  age: null,
  address: '',
  password: ''
})

const confirmPassword = ref('')
const error = ref(null)

async function handleRegister() {
  try {
    error.value = null
    
    if (formData.value.password !== confirmPassword.value) {
      error.value = 'Les mots de passe ne correspondent pas'
      return
    }
    
    await authStore.register(formData.value)
    router.push('/')
  } catch (err) {
    error.value = err.response?.data?.message || 'Erreur lors de l\'inscription'
  }
}
</script>

<style scoped>
.register-page {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 2rem 0;
}

.register-card {
  width: 100%;
  max-width: 500px;
}

.register-card h1 {
  text-align: center;
  margin-bottom: 2rem;
}

.btn-block {
  width: 100%;
  margin-top: 1rem;
}

.login-link {
  text-align: center;
  margin-top: 1.5rem;
}

.login-link a {
  color: #667eea;
  text-decoration: none;
  font-weight: 500;
}
</style>
