<template>
  <div class="my-reservations">
    <h1>Mes Réservations</h1>
    
    <div v-if="loading" class="loading">Chargement...</div>
    
    <div v-else-if="reservations.length === 0" class="no-reservations">
      <p>Vous n'avez aucune réservation active</p>
      <router-link to="/" class="btn btn-primary">Parcourir les films</router-link>
    </div>
    
    <div v-else class="reservations-grid">
      <div v-for="reservation in reservations" :key="reservation.id" class="reservation-card card">
        <div class="film-info">
          <img :src="getFilmPoster(reservation.film.id)" :alt="reservation.film.title" @error="handleImageError">
          <div class="info">
            <h3>{{ reservation.film.title }}</h3>
            <p class="rental-date">Loué le {{ formatDate(reservation.rentalDate) }}</p>
            <p class="price">Prix: {{ reservation.film.rentalPrice }}€</p>
            <div class="status">
              <span v-if="!reservation.paid" class="badge badge-warning">En attente de paiement</span>
              <span v-else class="badge badge-success">Payé</span>
            </div>
          </div>
        </div>
        
        <div class="actions">
          <button 
            v-if="!reservation.paid"
            @click="processPayment(reservation.id)"
            class="btn btn-primary"
          >
            Payer maintenant
          </button>
          <button 
            v-else
            @click="endRental(reservation.id)"
            class="btn btn-outline"
          >
            Terminer la location
          </button>
          <router-link 
            :to="`/films/${reservation.film.id}`"
            class="btn btn-outline"
          >
            Voir le film
          </router-link>
        </div>
      </div>
    </div>
    
    <!-- Modal de paiement -->
    <div v-if="showPaymentModal" class="modal">
      <div class="modal-content card">
        <h2>Paiement</h2>
        <form @submit.prevent="submitPayment">
          <div class="form-group">
            <label>Numéro de carte:</label>
            <input v-model="paymentData.cardNumber" type="text" pattern="[0-9]{16}" required>
          </div>
          <div class="form-group">
            <label>Date d'expiration (MM/AA):</label>
            <input v-model="paymentData.expiryDate" type="text" pattern="[0-9]{2}/[0-9]{2}" required>
          </div>
          <div class="form-group">
            <label>CVV:</label>
            <input v-model="paymentData.cvv" type="text" pattern="[0-9]{3}" required>
          </div>
          <div v-if="paymentError" class="error">{{ paymentError }}</div>
          <div class="modal-actions">
            <button type="submit" class="btn btn-primary">Valider le paiement</button>
            <button type="button" @click="closePaymentModal" class="btn btn-outline">Annuler</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { reservationService, paymentService } from '../services/filmService'

const reservations = ref([])
const loading = ref(true)
const showPaymentModal = ref(false)
const currentReservationId = ref(null)
const paymentError = ref(null)

const paymentData = ref({
  cardNumber: '',
  expiryDate: '',
  cvv: ''
})

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

function processPayment(reservationId) {
  currentReservationId.value = reservationId
  showPaymentModal.value = true
  paymentError.value = null
}

async function submitPayment() {
  try {
    paymentError.value = null
    await paymentService.processPayment(currentReservationId.value, paymentData.value)
    alert('Paiement effectué avec succès!')
    closePaymentModal()
    await loadReservations()
  } catch (err) {
    paymentError.value = err.response?.data?.message || 'Erreur lors du paiement'
  }
}

function closePaymentModal() {
  showPaymentModal.value = false
  paymentData.value = { cardNumber: '', expiryDate: '', cvv: '' }
  currentReservationId.value = null
}

async function endRental(reservationId) {
  if (confirm('Voulez-vous vraiment terminer cette location?')) {
    try {
      await reservationService.endReservation(reservationId)
      await loadReservations()
    } catch (err) {
      alert('Erreur: ' + (err.response?.data?.message || err.message))
    }
  }
}

function getFilmPoster(filmId) {
  return `/api/posters/${filmId}`
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

.film-info {
  display: flex;
  gap: 1.5rem;
  align-items: center;
}

.film-info img {
  width: 100px;
  height: 150px;
  object-fit: cover;
  border-radius: 4px;
}

.info h3 {
  margin-bottom: 0.5rem;
}

.rental-date, .price {
  color: #7f8c8d;
  margin: 0.25rem 0;
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

.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  width: 100%;
  max-width: 500px;
  padding: 2rem;
}

.modal-actions {
  display: flex;
  gap: 1rem;
  margin-top: 1.5rem;
}
</style>
