<template>
  <div class="login-page">
    <div class="login-card card">
      <h1>Connexion</h1>
      
      <div class="test-accounts">
        <h3>📝 Comptes de test disponibles :</h3>
        <ul>
          <li><strong>john_doe</strong> - 30 ans</li>
          <li><strong>jane_smith</strong> - 25 ans</li>
          <li><strong>admin</strong> - 35 ans (Admin)</li>
          <li><strong>alice_wonder</strong> - 28 ans</li>
          <li><strong>bob_martin</strong> - 20 ans</li>
        </ul>
        <p class="note">Mot de passe : n'importe lequel</p>
      </div>
      
      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label>Pseudo:</label>
          <input v-model="credentials.pseudo" type="text" required placeholder="Ex: john_doe">
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
      error.value = 'Pseudo non reconnu. Utilisez un des pseudos ci-dessus.'
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
  padding: 2rem 1rem;
}

.login-card {
  width: 100%;
  max-width: 500px;
}

.login-card h1 {
  text-align: center;
  margin-bottom: 1.5rem;
}

.test-accounts {
  background: #f8f9fa;
  border: 1px solid #dee2e6;
  border-radius: 8px;
  padding: 1rem;
  margin-bottom: 1.5rem;
}

.test-accounts h3 {
  margin: 0 0 0.75rem 0;
  font-size: 1rem;
  color: #495057;
}

.test-accounts ul {
  list-style: none;
  padding: 0;
  margin: 0.5rem 0;
}

.test-accounts li {
  padding: 0.25rem 0;
  font-family: monospace;
}

.test-accounts .note {
  margin: 0.75rem 0 0 0;
  font-size: 0.875rem;
  color: #6c757d;
  font-style: italic;
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
