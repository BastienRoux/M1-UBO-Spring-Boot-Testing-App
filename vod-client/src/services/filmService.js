import api from './api'

export const filmService = {
  // Récupérer tous les films
  getAllFilms() {
    return api.get('/films')
  },

  // Récupérer un film par ID
  getFilmById(id) {
    return api.get(`/films/${id}`)
  },

  // Créer un film (admin)
  createFilm(filmData) {
    return api.post('/films', filmData)
  },

  // Mettre à jour un film (admin)
  updateFilm(id, filmData) {
    return api.put(`/films/${id}`, filmData)
  },

  // Supprimer un film (admin)
  deleteFilm(id) {
    return api.delete(`/films/${id}`)
  },

  // Ouvrir/fermer à la location
  toggleRental(id, isOpen, price = null) {
    return api.patch(`/films/${id}/rental`, { isOpen, price })
  },

  // Récupérer les films par genre
  getFilmsByGenre(genre) {
    return api.get(`/films/genre/${genre}`)
  },

  // Récupérer les films par artiste
  getFilmsByArtist(artistId) {
    return api.get(`/films/artist/${artistId}`)
  }
}

export const artistService = {
  getAllArtists() {
    return api.get('/artists')
  },

  getArtistById(id) {
    return api.get(`/artists/${id}`)
  },

  createArtist(artistData) {
    return api.post('/artists', artistData)
  },

  updateArtist(id, artistData) {
    return api.put(`/artists/${id}`, artistData)
  },

  deleteArtist(id) {
    return api.delete(`/artists/${id}`)
  }
}

export const reservationService = {
  // Récupérer mes réservations
  getMyReservations() {
    return api.get('/reservations/my')
  },

  // Créer une réservation
  createReservation(filmId) {
    return api.post('/reservations', { filmId })
  },

  // Terminer une réservation
  endReservation(id) {
    return api.patch(`/reservations/${id}/end`)
  },

  // Obtenir le nombre de réservations actives
  getActiveReservationsCount() {
    return api.get('/reservations/active/count')
  }
}

export const evaluationService = {
  // Récupérer les évaluations d'un film
  getFilmEvaluations(filmId) {
    return api.get(`/evaluations/film/${filmId}`)
  },

  // Créer une évaluation
  createEvaluation(evaluationData) {
    return api.post('/evaluations', evaluationData)
  },

  // Mettre à jour une évaluation
  updateEvaluation(id, evaluationData) {
    return api.put(`/evaluations/${id}`, evaluationData)
  },

  // Supprimer une évaluation
  deleteEvaluation(id) {
    return api.delete(`/evaluations/${id}`)
  },

  // Récupérer la moyenne des évaluations
  getAverageRating(filmId) {
    return api.get(`/evaluations/film/${filmId}/average`)
  }
}

export const posterService = {
  // Récupérer l'affiche d'un film
  getFilmPoster(filmId) {
    return api.get(`/posters/${filmId}`, { responseType: 'blob' })
  },

  // Uploader une affiche
  uploadPoster(filmId, file) {
    const formData = new FormData()
    formData.append('poster', file)
    return api.post(`/posters/${filmId}`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  },

  // Supprimer une affiche
  deletePoster(filmId) {
    return api.delete(`/posters/${filmId}`)
  }
}

export const paymentService = {
  // Simuler un paiement
  processPayment(reservationId, cardData) {
    return api.post('/payments/process', {
      reservationId,
      cardNumber: cardData.cardNumber,
      expiryDate: cardData.expiryDate,
      cvv: cardData.cvv
    })
  }
}
