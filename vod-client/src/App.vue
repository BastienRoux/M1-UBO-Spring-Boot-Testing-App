<template>
  <div id="app">
    <header class="app-header">
      <nav class="navbar">
        <div class="navbar-brand">
          <router-link to="/" class="logo">🎬 VOD Platform</router-link>
        </div>
        <div class="navbar-menu">
          <router-link to="/" class="nav-link">Films</router-link>
          <router-link to="/artists" class="nav-link">Artistes</router-link>
          <router-link to="/my-reservations" class="nav-link" v-if="isAuthenticated">
            Mes Réservations
          </router-link>
          <div class="nav-auth">
            <template v-if="!isAuthenticated">
              <router-link to="/login" class="btn btn-outline">Connexion</router-link>
              <router-link to="/register" class="btn btn-primary">Inscription</router-link>
            </template>
            <template v-else>
              <span class="user-info">{{ currentUser }}</span>
              <button @click="logout" class="btn btn-outline">Déconnexion</button>
            </template>
          </div>
        </div>
      </nav>
    </header>
    
    <main class="app-main">
      <router-view />
    </main>
    
    <footer class="app-footer">
      <p>&copy; 2026 VOD Platform - Master ILIADE & TIIL-A UBO</p>
    </footer>
  </div>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useAuthStore } from './stores/auth'

const authStore = useAuthStore()

const isAuthenticated = computed(() => authStore.isAuthenticated)
const currentUser = computed(() => authStore.user?.pseudo)

const logout = () => {
  authStore.logout()
}

// Charger les données utilisateur au démarrage si un token existe
onMounted(() => {
  authStore.checkAuth()
})
</script>

<style scoped>
.app-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.navbar {
  max-width: 1200px;
  margin: 0 auto;
  padding: 1rem 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo {
  font-size: 1.5rem;
  font-weight: bold;
  color: white;
  text-decoration: none;
}

.navbar-menu {
  display: flex;
  gap: 2rem;
  align-items: center;
}

.nav-link {
  color: white;
  text-decoration: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  transition: background 0.3s;
}

.nav-link:hover {
  background: rgba(255, 255, 255, 0.1);
}

.nav-link.router-link-active {
  background: rgba(255, 255, 255, 0.2);
}

.nav-auth {
  display: flex;
  gap: 1rem;
  align-items: center;
  margin-left: 2rem;
}

.user-info {
  color: white;
  font-weight: 500;
}

.app-main {
  min-height: calc(100vh - 180px);
  max-width: 1200px;
  margin: 2rem auto;
  padding: 0 2rem;
}

.app-footer {
  background: #2c3e50;
  color: white;
  text-align: center;
  padding: 2rem;
  margin-top: 4rem;
}
</style>
