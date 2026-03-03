<template>
  <div class="my-reservations">
    <h1>Mes Réservations</h1>
    
    <div v-if="loading" class="loading">Chargement...</div>
    
    <div v-else-if="activeReservations.length === 0" class="no-reservations">
      <p>Vous n'avez aucune réservation active</p>
      <router-link to="/" class="btn btn-primary">Parcourir les films</router-link>
    </div>
    
    <div v-else class="reservations-grid">
      <div v-for="reservation in activeReservations" :key="reservation.id" class="reservation-card card">
        <div class="reservation-info">
          <h3>{{ reservation.name }}</h3>
          <p class="reservation-date">Réservé le {{ formatDate(reservation.date) }}</p>
          <p class="film-id">Film ID: {{ reservation.movieId }}</p>
          <div class="status">
            <span class="badge badge-success">✓ Active</span>
          </div>
        </div>
        
        <div class="actions">
          <router-link 
            :to="`/films/${reservation.movieId}`"
            class="btn btn-primary"
          >
            Voir le film
          </router-link>
          <button 
            @click="cancelReservation(reservation.id)"
            class="btn btn-danger"
          >
            Annuler
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { reservationService } from '../services/filmService'

const reservations = ref([])
const loading = ref(true)

// Filtrer seulement les réservations actives
const activeReservations = computed(() => 
  reservations.value.filter(r => r.status === 'ACTIVE')
)

onMounted(async () => {
  await loadReservations()
})

async function loadReservations() {
  try {
    loading.value = true
    const response = await reservationService.getMyReservations()
    reservations.value = response.data
  } catch (err) {
    console.error('Erreur:', err)
  } finally {
    loading.value = false
  }
}

async function cancelReservation(reservationId) {
  if (confirm('Voulez-vous vraiment annuler cette réservation ?')) {
    try {
      await reservationService.endReservation(reservationId)
      alert('Réservation annulée avec succès!')
      await loadReservations()
    } catch (err) {
      alert('Erreur: ' + (err.response?.data?.message || err.message))
    }
  }
}

function handleImageError(event) {
  event.target.src = 'https://via.placeholder.com/150x225?text=Film'
}

function formatDate(date) {
  return new Date(date).toLocaleDateString('fr-FR')
}
</script>

<style scoped>
.my-reservations h1 {
  margin-bottom: 2rem;
}

.no-reservations {
  text-align: center;
  padding: 4rem 0;
}

.no-reservations p {
  font-size: 1.2rem;
  color: #7f8c8d;
  margin-bottom: 2rem;
}

.reservations-grid {
  display: grid;
  gap: 1.5rem;
}

.reservation-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem;
}

.reservation-info h3 {
  margin-bottom: 0.5rem;
  color: #2c3e50;
}

.reservation-date, .film-id {
  color: #7f8c8d;
  margin: 0.25rem 0;
  font-size: 0.95rem;
}

.status {
  margin-top: 0.5rem;
}

.badge {
  padding: 0.25rem 0.75rem;
  border-radius: 12px;
  font-size: 0.875rem;
}

.badge-warning {
  background: #f39c12;
  color: white;
}

.badge-success {
  background: #27ae60;
  color: white;
}

.actions {
  display: flex;
  gap: 0.5rem;
}

.btn-danger {
  background-color: #e74c3c;
  color: white;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 6px;
  font-size: 0.9rem;
  cursor: pointer;
  transition: background-color 0.2s;
}

.btn-danger:hover {
  background-color: #c0392b;
}
</style>
