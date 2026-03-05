<template>
  <div class="film-detail" v-if="film">
    <div class="detail-header">
      <div class="poster-section">
        <img :src="getFilmPoster(film.id)" :alt="film.title" @error="handleImageError">
      </div>
      <div class="info-section">
        <h1>{{ film.title }}</h1>
        <p class="year">{{ film.releaseYear }}</p>
        <div class="metadata">
          <p><strong>Réalisateur:</strong> {{ film.director }}</p>
          <p v-if="film.artists && film.artists.length"><strong>Artistes:</strong> {{ film.artists.map(a => a.name).join(', ') }}</p>
        </div>
        <div class="rating-display">
          <span class="stars">⭐ {{ averageRating }}/5</span>
          <span class="count">({{ evaluations.length }} évaluations)</span>
        </div>
        
        <div class="reservation-status" v-if="isAuthenticated">
          <p v-if="userHasRentedFilm" class="reserved-badge">✓ Film réservé</p>
          <p v-if="activeReservations >= 3 && !userHasRentedFilm" class="warning-badge">⚠️ Limite de 3 réservations atteinte</p>
        </div>
        
        <div class="actions" v-if="isAuthenticated">
          <button 
            v-if="!userHasRentedFilm"
            @click="reserveFilm" 
            class="btn btn-primary" 
            :disabled="activeReservations >= 3"
          >
            Réserver ce film
          </button>
          <button 
            v-else
            @click="cancelReservation" 
            class="btn btn-danger"
          >
            Annuler la réservation
          </button>
        </div>
      </div>
    </div>

    <div class="actors-section" v-if="film.artists && film.artists.length">
      <h2>Artistes</h2>
      <div class="actors-grid">
        <div v-for="artist in film.artists" :key="artist.id" class="actor-card">
          <router-link :to="`/artists/${artist.id}`">{{ artist.name }}</router-link>
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
            <strong>{{ getEvaluationAuthorLabel(evaluation.userId) }}</strong>
            <span class="rating">⭐ {{ evaluation.rating }}/5</span>
          </div>
          <p v-if="evaluation.comment" class="comment">{{ evaluation.comment }}</p>
          <div class="evaluation-date" v-if="evaluation.createdAt">{{ formatDate(evaluation.createdAt) }}</div>
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
const currentReservationId = ref(null)

const newEvaluation = ref({
  rating: 0,
  comment: ''
})

const isAuthenticated = computed(() => authStore.isAuthenticated)
const currentUserId = computed(() => Number(authStore.user?.id || 1))
const userEvaluation = computed(() => 
  evaluations.value.find(e => e.userId === currentUserId.value)
)
const canEvaluate = computed(() => isAuthenticated.value && userHasRentedFilm.value) // Peut évaluer seulement si le film est réservé
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
    
    // Charger le film
    const filmResponse = await filmService.getFilmById(filmId)
    film.value = filmResponse.data
    
    // Charger les évaluations (reviews)
    try {
      const evaluationsResponse = await evaluationService.getFilmEvaluations(filmId)
      evaluations.value = evaluationsResponse.data || []
    } catch (err) {
      console.log('Pas d\'évaluations pour ce film')
      evaluations.value = []
    }
    
    if (isAuthenticated.value) {
      try {
        // Vérifier si l'utilisateur a réservé ce film
        const reservationsResponse = await reservationService.getMyReservations(currentUserId.value)
        const reservation = reservationsResponse.data.find(r => r.movieId === parseInt(filmId) && r.status === 'ACTIVE')
        userHasRentedFilm.value = !!reservation
        currentReservationId.value = reservation?.id || null
        
        const countResponse = await reservationService.getActiveReservationsCount(currentUserId.value)
        activeReservations.value = countResponse.data
      } catch (err) {
        console.log('Erreur lors du chargement des réservations:', err)
      }
    }
  } catch (err) {
    console.error('Erreur lors du chargement du film:', err)
  } finally {
    loading.value = false
  }
}

async function reserveFilm() {
  try {
    if (!isAuthenticated.value) {
      alert('Vous devez être connecté pour réserver un film')
      return
    }
    
    await reservationService.createReservation(film.value.id, currentUserId.value)
    alert('Film réservé avec succès!')
    await loadFilmData() // Recharger pour mettre à jour l'état
  } catch (err) {
    alert('Erreur: ' + (err.response?.data?.message || err.message))
  }
}

async function cancelReservation() {
  try {
    if (!currentReservationId.value) {
      alert('Aucune réservation à annuler')
      return
    }
    
    if (confirm('Voulez-vous vraiment annuler cette réservation ?')) {
      await reservationService.endReservation(currentReservationId.value)
      alert('Réservation annulée avec succès!')
      await loadFilmData() // Recharger pour mettre à jour l'état
    }
  } catch (err) {
    alert('Erreur: ' + (err.response?.data?.message || err.message))
  }
}

async function submitEvaluation() {
  try {
    if (!isAuthenticated.value) {
      alert('Vous devez être connecté pour évaluer un film')
      return
    }
    
    const evaluationData = {
      filmId: parseInt(film.value.id),
      userId: currentUserId.value,
      rating: newEvaluation.value.rating,
      comment: newEvaluation.value.comment || ''
    }
    
    if (userEvaluation.value) {
      await evaluationService.updateEvaluation(userEvaluation.value.id, evaluationData)
    } else {
      await evaluationService.createEvaluation(evaluationData)
    }
    showEvaluationForm.value = false
    await loadFilmData()
  } catch (err) {
    console.error('Erreur:', err)
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

function getEvaluationAuthorLabel(userId) {
  if (Number(userId) === currentUserId.value && authStore.user?.pseudo) {
    return authStore.user.pseudo
  }
  return `Utilisateur #${userId}`
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

.reservation-status {
  margin: 1rem 0;
}

.reserved-badge {
  background-color: #27ae60;
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 6px;
  display: inline-block;
  font-weight: 600;
  margin: 0.5rem 0;
}

.warning-badge {
  background-color: #e67e22;
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 6px;
  display: inline-block;
  font-weight: 600;
  margin: 0.5rem 0;
}

.btn-danger {
  background-color: #e74c3c;
  color: white;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 6px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.2s;
}

.btn-danger:hover {
  background-color: #c0392b;
}

.btn-danger:disabled {
  background-color: #bdc3c7;
  cursor: not-allowed;
}
</style>
