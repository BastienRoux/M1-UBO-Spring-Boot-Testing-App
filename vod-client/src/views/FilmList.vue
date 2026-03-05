<template>
  <div class="film-list">
    <div class="page-header">
      <h1>Catalogue de Films</h1>
      <div class="filters">
        <select v-model="selectedGenre" @change="filterByGenre" class="filter-select">
          <option value="">Tous les genres</option>
          <option value="Action">Action</option>
          <option value="Comédie">Comédie</option>
          <option value="Drame">Drame</option>
          <option value="Science-Fiction">Science-Fiction</option>
          <option value="Horreur">Horreur</option>
          <option value="Romance">Romance</option>
        </select>
      </div>
    </div>

    <div v-if="loading" class="loading">Chargement des films...</div>
    
    <div v-else-if="error" class="error">{{ error }}</div>
    
    <div v-else class="grid">
      <div v-for="film in films" :key="film.id" class="film-card">
        <div class="film-poster">
          <img :src="getFilmPoster(film.id)" :alt="film.title" @error="handleImageError">
        </div>
        <div class="film-info">
          <h3>{{ film.title }}</h3>
          <p class="film-year">{{ film.releaseYear }}</p>
          <p class="film-director">Réalisé par {{ film.director }}</p>
          <div class="film-rating">
            <span class="stars">⭐ {{ film.averageRating || 'N/A' }}</span>
          </div>
          <div class="film-actions">
            <router-link :to="`/films/${film.id}`" class="btn btn-primary">
              Voir détails
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import { filmService, reservationService } from '../services/filmService'

const router = useRouter()
const authStore = useAuthStore()

const films = ref([])
const loading = ref(true)
const error = ref(null)
const selectedGenre = ref('')
const activeReservations = ref(0)

const isAuthenticated = computed(() => authStore.isAuthenticated)
const currentUserId = computed(() => Number(authStore.user?.id || 1))

onMounted(async () => {
  await loadFilms()
  if (isAuthenticated.value) {
    await loadActiveReservations()
  }
})

async function loadFilms() {
  try {
    loading.value = true
    const response = await filmService.getAllFilms()
    films.value = response.data
  } catch (err) {
    error.value = 'Erreur lors du chargement des films'
    console.error(err)
  } finally {
    loading.value = false
  }
}

async function loadActiveReservations() {
  try {
    const response = await reservationService.getActiveReservationsCount(currentUserId.value)
    activeReservations.value = response.data
  } catch (err) {
    console.error('Erreur lors du chargement des réservations:', err)
  }
}

async function filterByGenre() {
  if (!selectedGenre.value) {
    await loadFilms()
  } else {
    try {
      loading.value = true
      const response = await filmService.getFilmsByGenre(selectedGenre.value)
      films.value = response.data
    } catch (err) {
      error.value = 'Erreur lors du filtrage'
    } finally {
      loading.value = false
    }
  }
}

async function reserveFilm(filmId) {
  if (activeReservations.value >= 3) {
    alert('Vous avez déjà 3 films en réservation')
    return
  }
  
  try {
    await reservationService.createReservation(filmId, currentUserId.value)
    alert('Film réservé avec succès!')
    router.push('/my-reservations')
  } catch (err) {
    alert('Erreur lors de la réservation: ' + (err.response?.data?.message || err.message))
  }
}

function getFilmPoster(filmId) {
  return `/api/posters/${filmId}`
}

function handleImageError(event) {
  event.target.src = 'https://via.placeholder.com/300x450?text=Pas+d\'affiche'
}
</script>

<style scoped>
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.filters {
  display: flex;
  gap: 1rem;
}

.filter-select {
  padding: 0.5rem 1rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.film-card {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s, box-shadow 0.3s;
}

.film-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.film-poster {
  width: 100%;
  height: 400px;
  overflow: hidden;
  background: #f0f0f0;
}

.film-poster img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.film-info {
  padding: 1.5rem;
}

.film-info h3 {
  margin-bottom: 0.5rem;
  color: #2c3e50;
}

.film-year {
  color: #7f8c8d;
  font-size: 0.9rem;
}

.film-director {
  color: #555;
  font-size: 0.95rem;
  margin: 0.5rem 0;
}

.film-rating {
  margin: 1rem 0;
}

.stars {
  font-size: 1.1rem;
}

.film-price {
  margin: 1rem 0;
}

.price {
  font-size: 1.5rem;
  font-weight: bold;
  color: #667eea;
}

.film-actions {
  display: flex;
  gap: 0.5rem;
  margin-top: 1rem;
}

.film-actions .btn {
  flex: 1;
  text-align: center;
}
</style>
