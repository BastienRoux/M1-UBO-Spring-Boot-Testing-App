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
    return api.get('/reservations/user/1') // TODO: Récupérer l'ID de l'utilisateur connecté
  },

  // Créer une réservation
  createReservation(filmId) {
    return api.post('/reservations', { 
      name: 'Reservation',
      movieId: filmId,
      userId: 1, // TODO: Récupérer l'ID de l'utilisateur connecté
      date: new Date().toISOString().split('T')[0],
      status: 'ACTIVE'
    })
  },

  // Terminer/Annuler une réservation
  endReservation(id) {
    return api.delete(`/reservations/${id}`)
  },

  // Obtenir le nombre de réservations actives
  getActiveReservationsCount() {
    return api.get('/reservations/user/1').then(res => {
      // Compter seulement les réservations actives
      const activeCount = res.data.filter(r => r.status === 'ACTIVE').length
      return { data: activeCount }
    }) // TODO: Utiliser l'ID de l'utilisateur connecté
  }
}

export const evaluationService = {
  // Récupérer les évaluations d'un film
  getFilmEvaluations(filmId) {
    return api.get(`/reviews/movie/${filmId}`)
  },

  // Créer une évaluation
  createEvaluation(evaluationData) {
    return api.post('/reviews', {
      rating: evaluationData.rating,
      comment: evaluationData.comment,
      movieId: evaluationData.filmId,
      userId: evaluationData.userId // À récupérer du store auth
    })
  },

  // Mettre à jour une évaluation
  updateEvaluation(id, evaluationData) {
    return api.put(`/reviews/${id}`, evaluationData)
  },

  // Supprimer une évaluation
  deleteEvaluation(id) {
    return api.delete(`/reviews/${id}`)
  },

  // Récupérer la moyenne des évaluations
  getAverageRating(filmId) {
    return api.get(`/reviews/movie/${filmId}/average`)
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
