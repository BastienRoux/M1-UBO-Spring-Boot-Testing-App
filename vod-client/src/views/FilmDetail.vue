<template>
  <div class="film-detail" v-if="film">
    <div class="detail-header">
      <div class="poster-section">
        <img :src="getFilmPoster(film.id)" :alt="film.title" @error="handleImageError">
      </div>
      <div class="info-section">
        <h1>{{ film.title }}</h1>
        <p class="year">{{ film.year }}</p>
        <div class="metadata">
          <p><strong>Réalisateur:</strong> {{ film.director?.name }}</p>
          <p><strong>Genres:</strong> {{ film.genres?.join(', ') }}</p>
          <p><strong>Âge minimum:</strong> {{ film.minimumAge }}+</p>
          <p v-if="film.isOpenForRental"><strong>Prix:</strong> {{ film.rentalPrice }}€</p>
        </div>
        <div class="rating-display">
          <span class="stars">⭐ {{ averageRating }}/5</span>
          <span class="count">({{ evaluations.length }} évaluations)</span>
        </div>
        
        <div class="actions" v-if="isAuthenticated && film.isOpenForRental">
          <button @click="reserveFilm" class="btn btn-primary" :disabled="activeReservations >= 3">
            Réserver ce film
          </button>
        </div>
      </div>
    </div>

    <div class="actors-section" v-if="film.actors && film.actors.length">
      <h2>Acteurs</h2>
      <div class="actors-grid">
        <div v-for="actor in film.actors" :key="actor.id" class="actor-card">
          <router-link :to="`/artists/${actor.id}`">{{ actor.name }}</router-link>
        </div>
      </div>
    </div>

    <div class="evaluations-section">
      <div class="section-header">
        <h2>Évaluations</h2>
        <button 
          v-if="canEvaluate" 
          @click="showEvaluationForm = !showEvaluationForm"
          class="btn btn-primary"
        >
          {{ userEvaluation ? 'Modifier mon avis' : 'Donner mon avis' }}
        </button>
      </div>

      <div v-if="showEvaluationForm" class="evaluation-form card">
        <h3>{{ userEvaluation ? 'Modifier' : 'Ajouter' }} mon évaluation</h3>
        <form @submit.prevent="submitEvaluation">
          <div class="form-group">
            <label>Note (0-5):</label>
            <div class="stars-input">
              <span 
                v-for="n in 5" 
                :key="n"
                @click="newEvaluation.rating = n"
                class="star"
                :class="{ active: n <= newEvaluation.rating }"
              >
                ⭐
              </span>
            </div>
          </div>
          <div class="form-group">
            <label>Commentaire (optionnel):</label>
            <textarea v-model="newEvaluation.comment" rows="4"></textarea>
          </div>
          <div class="form-actions">
            <button type="submit" class="btn btn-primary">Envoyer</button>
            <button type="button" @click="showEvaluationForm = false" class="btn btn-outline">
              Annuler
            </button>
          </div>
        </form>
      </div>

      <div class="evaluations-list">
        <div v-for="evaluation in evaluations" :key="evaluation.id" class="evaluation-card card">
          <div class="evaluation-header">
            <strong>{{ evaluation.user?.pseudo }}</strong>
            <span class="rating">⭐ {{ evaluation.rating }}/5</span>
          </div>
          <p v-if="evaluation.comment" class="comment">{{ evaluation.comment }}</p>
          <div class="evaluation-date">{{ formatDate(evaluation.createdAt) }}</div>
        </div>
        <p v-if="!evaluations.length" class="no-evaluations">
          Aucune évaluation pour ce film
        </p>
      </div>
    </div>
  </div>
  
  <div v-else-if="loading" class="loading">Chargement...</div>
  <div v-else class="error">Film non trouvé</div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import { filmService, evaluationService, reservationService } from '../services/filmService'

const route = useRoute()
const authStore = useAuthStore()

const film = ref(null)
const evaluations = ref([])
const loading = ref(true)
const showEvaluationForm = ref(false)
const activeReservations = ref(0)
const userHasRentedFilm = ref(false)

const newEvaluation = ref({
  rating: 0,
  comment: ''
})

const isAuthenticated = computed(() => authStore.isAuthenticated)
const userEvaluation = computed(() => 
  evaluations.value.find(e => e.user?.pseudo === authStore.user?.pseudo)
)
const canEvaluate = computed(() => isAuthenticated.value && userHasRentedFilm.value)
const averageRating = computed(() => {
  if (!evaluations.value.length) return 'N/A'
  const sum = evaluations.value.reduce((acc, e) => acc + e.rating, 0)
  return (sum / evaluations.value.length).toFixed(1)
})

onMounted(async () => {
  await loadFilmData()
})

async function loadFilmData() {
  try {
    loading.value = true
    const filmId = route.params.id
    
    const [filmResponse, evaluationsResponse] = await Promise.all([
      filmService.getFilmById(filmId),
      evaluationService.getFilmEvaluations(filmId)
    ])
    
    film.value = filmResponse.data
    evaluations.value = evaluationsResponse.data
    
    if (isAuthenticated.value) {
      // Vérifier si l'utilisateur a loué ce film
      const reservationsResponse = await reservationService.getMyReservations()
      userHasRentedFilm.value = reservationsResponse.data.some(r => r.filmId === filmId)
      
      const countResponse = await reservationService.getActiveReservationsCount()
      activeReservations.value = countResponse.data
    }
  } catch (err) {
    console.error('Erreur:', err)
  } finally {
    loading.value = false
  }
}

async function reserveFilm() {
  try {
    await reservationService.createReservation(film.value.id)
    alert('Film réservé avec succès!')
  } catch (err) {
    alert('Erreur: ' + (err.response?.data?.message || err.message))
  }
}

async function submitEvaluation() {
  try {
    if (userEvaluation.value) {
      await evaluationService.updateEvaluation(userEvaluation.value.id, newEvaluation.value)
    } else {
      await evaluationService.createEvaluation({
        filmId: film.value.id,
        ...newEvaluation.value
      })
    }
    showEvaluationForm.value = false
    await loadFilmData()
  } catch (err) {
    alert('Erreur: ' + (err.response?.data?.message || err.message))
  }
}

function getFilmPoster(filmId) {
  return `/api/posters/${filmId}`
}

function handleImageError(event) {
  event.target.src = 'https://via.placeholder.com/400x600?text=Pas+d\'affiche'
}

function formatDate(date) {
  return new Date(date).toLocaleDateString('fr-FR')
}
</script>

<style scoped>
.detail-header {
  display: grid;
  grid-template-columns: 400px 1fr;
  gap: 2rem;
  margin-bottom: 3rem;
}

.poster-section img {
  width: 100%;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.info-section h1 {
  font-size: 2.5rem;
  margin-bottom: 0.5rem;
}

.year {
  color: #7f8c8d;
  font-size: 1.2rem;
  margin-bottom: 1.5rem;
}

.metadata p {
  margin: 0.5rem 0;
  font-size: 1.1rem;
}

.rating-display {
  margin: 1.5rem 0;
  font-size: 1.5rem;
}

.count {
  font-size: 1rem;
  color: #7f8c8d;
  margin-left: 1rem;
}

.actions {
  margin-top: 2rem;
}

.actors-section {
  margin: 3rem 0;
}

.actors-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  gap: 1rem;
  margin-top: 1rem;
}

.actor-card {
  background: white;
  padding: 1rem;
  border-radius: 8px;
  text-align: center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.stars-input {
  display: flex;
  gap: 0.5rem;
  font-size: 2rem;
}

.star {
  cursor: pointer;
  opacity: 0.3;
  transition: opacity 0.2s;
}

.star.active {
  opacity: 1;
}

.evaluations-list {
  margin-top: 2rem;
}

.evaluation-card {
  margin-bottom: 1rem;
}

.evaluation-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 0.5rem;
}

.comment {
  margin: 1rem 0;
  line-height: 1.6;
}

.evaluation-date {
  font-size: 0.9rem;
  color: #7f8c8d;
}

.no-evaluations {
  text-align: center;
  color: #7f8c8d;
  padding: 2rem;
}

.form-actions {
  display: flex;
  gap: 1rem;
}
</style>
