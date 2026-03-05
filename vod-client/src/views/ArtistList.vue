<template>
  <div class="artist-list">
    <h1>Artistes</h1>
    
    <div v-if="loading" class="loading">Chargement...</div>
    
    <div v-else class="grid">
      <div v-for="artist in artists" :key="artist.id" class="artist-card card">
        <h3>{{ artist.name }}</h3>
        <p class="artist-role">{{ artist.genre }}</p>
        <router-link :to="`/artists/${artist.id}`" class="btn btn-outline">
          Voir les films
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { artistService } from '../services/filmService'

const artists = ref([])
const loading = ref(true)

onMounted(async () => {
  await loadArtists()
})

async function loadArtists() {
  try {
    loading.value = true
    const response = await artistService.getAllArtists()
    artists.value = response.data
  } catch (err) {
    console.error('Erreur:', err)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.artist-card {
  text-align: center;
  padding: 2rem;
}

.artist-card h3 {
  margin-bottom: 0.5rem;
}

.artist-role {
  color: #7f8c8d;
  margin-bottom: 1rem;
}
</style>
