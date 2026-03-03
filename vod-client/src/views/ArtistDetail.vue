<template>
  <div class="artist-detail" v-if="artist">
    <h1>{{ artist.name }}</h1>
    <p class="role">{{ artist.role }}</p>
    
    <div class="films-section">
      <h2>Films</h2>
      <div class="grid">
        <div v-for="film in films" :key="film.id" class="film-card card">
          <h3>{{ film.title }}</h3>
          <p>{{ film.year }}</p>
          <router-link :to="`/films/${film.id}`" class="btn btn-primary">
            Voir détails
          </router-link>
        </div>
      </div>
    </div>
  </div>
  
  <div v-else-if="loading" class="loading">Chargement...</div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { artistService, filmService } from '../services/filmService'

const route = useRoute()

const artist = ref(null)
const films = ref([])
const loading = ref(true)

onMounted(async () => {
  await loadArtistData()
})

async function loadArtistData() {
  try {
    loading.value = true
    const artistId = route.params.id
    
    const [artistResponse, filmsResponse] = await Promise.all([
      artistService.getArtistById(artistId),
      filmService.getFilmsByArtist(artistId)
    ])
    
    artist.value = artistResponse.data
    films.value = filmsResponse.data
  } catch (err) {
    console.error('Erreur:', err)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.artist-detail h1 {
  font-size: 2.5rem;
  margin-bottom: 0.5rem;
}

.role {
  font-size: 1.2rem;
  color: #7f8c8d;
  margin-bottom: 2rem;
}

.films-section {
  margin-top: 3rem;
}

.film-card {
  text-align: center;
  padding: 1.5rem;
}
</style>
