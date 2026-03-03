# Projet VOD - Vidéo à la Demande

## 📋 Description du Projet

Application Web de gestion de vidéos à la demande (VOD) permettant aux utilisateurs de réserver des films, de laisser des évaluations et de gérer leurs locations.

## 🏗️ Architecture

Le projet suit une architecture microservices avec :

### Services Backend

1. **API Gateway** (Port 9000) - Spring Cloud Gateway
   - Point d'entrée unique pour toutes les requêtes
   - Gestion du routing vers les différents services
   - Configuration CORS

2. **Film Service** (Port 8080) - Spring Boot
   - Gestion des films
   - Gestion des artistes
   - Gestion des réservations
   - Gestion des utilisateurs
   - Base de données: MariaDB

3. **Evaluation Service** (Port 8081) - Servlet
   - Gestion des évaluations (notes et commentaires)
   - Base de données: MongoDB

4. **Poster Service** (Port 8082) - Servlet
   - Gestion des affiches de films
   - Stockage des images
   - Base de données: MongoDB

5. **Payment Service** (Port 8083) - Spring Boot
   - Simulation de paiement par carte bancaire
   - Validation des transactions

### Frontend

**Vue.js Client** (Port 5173)
- Interface utilisateur responsive
- Vue Router pour la navigation
- Pinia pour la gestion d'état
- Axios pour les appels API

## 📁 Structure du Projet

```
M1-UBO-Spring-Boot-Testing-App/
├── api-gateway/              # Spring Cloud Gateway
│   └── src/
│       ├── main/
│       │   ├── java/
│       │   └── resources/
│       └── build.gradle
├── src/                      # Film Service (Spring Boot)
│   ├── main/
│   │   ├── java/com/
│   │   │   ├── controllers/
│   │   │   ├── entities/
│   │   │   ├── repositories/
│   │   │   ├── services/
│   │   │   └── dtos/
│   │   └── resources/
│   └── test/
├── vod-client/               # Application Vue.js
│   ├── src/
│   │   ├── views/
│   │   ├── components/
│   │   ├── services/
│   │   ├── stores/
│   │   └── router/
│   ├── package.json
│   └── vite.config.js
├── docker-compose-full.yaml  # Configuration Docker Compose
└── README.md
```

## 🚀 Démarrage Rapide

### Prérequis

- Java 17+
- Node.js 18+
- Docker & Docker Compose
- Gradle 8.5+

### Installation

#### 1. Cloner le repository

```bash
git clone https://github.com/votre-repo/M1-UBO-Spring-Boot-Testing-App.git
cd M1-UBO-Spring-Boot-Testing-App
```

#### 2. Démarrer les bases de données avec Docker

```bash
docker-compose -f docker-compose-full.yaml up -d mariadb mongodb
```

#### 3. Démarrer le Film Service (Spring Boot)

```bash
./gradlew bootRun
```

#### 4. Démarrer l'API Gateway

```bash
cd api-gateway
../gradlew bootRun
```

#### 5. Démarrer le client Vue.js

```bash
cd vod-client
npm install
npm run dev
```

L'application sera accessible sur :
- Frontend: http://localhost:5173
- API Gateway: http://localhost:9000
- Film Service: http://localhost:8080

## 🔌 API Endpoints

### Films

- `GET /api/films` - Liste tous les films
- `GET /api/films/{id}` - Détails d'un film
- `POST /api/films` - Créer un film (admin)
- `PUT /api/films/{id}` - Modifier un film (admin)
- `DELETE /api/films/{id}` - Supprimer un film (admin)
- `GET /api/films/genre/{genre}` - Films par genre
- `GET /api/films/artist/{artistId}` - Films par artiste
- `PATCH /api/films/{id}/rental` - Ouvrir/fermer la location (admin)

### Artistes

- `GET /api/artists` - Liste tous les artistes
- `GET /api/artists/{id}` - Détails d'un artiste
- `POST /api/artists` - Créer un artiste (admin)
- `PUT /api/artists/{id}` - Modifier un artiste (admin)
- `DELETE /api/artists/{id}` - Supprimer un artiste (admin)

### Réservations

- `GET /api/reservations/my` - Mes réservations
- `POST /api/reservations` - Créer une réservation
- `PATCH /api/reservations/{id}/end` - Terminer une réservation
- `GET /api/reservations/active/count` - Nombre de réservations actives

### Utilisateurs

- `POST /api/users/register` - Inscription
- `POST /api/users/login` - Connexion
- `GET /api/users/me` - Profil utilisateur

### Évaluations

- `GET /api/evaluations/film/{filmId}` - Évaluations d'un film
- `POST /api/evaluations` - Créer une évaluation
- `PUT /api/evaluations/{id}` - Modifier une évaluation
- `DELETE /api/evaluations/{id}` - Supprimer une évaluation
- `GET /api/evaluations/film/{filmId}/average` - Moyenne des notes

### Affiches

- `GET /api/posters/{filmId}` - Récupérer l'affiche
- `POST /api/posters/{filmId}` - Upload une affiche
- `DELETE /api/posters/{filmId}` - Supprimer une affiche

### Paiements

- `POST /api/payments/process` - Traiter un paiement

## 💾 Bases de Données

### MariaDB (Film Service)

- **Host**: localhost:3306
- **Database**: vod_db
- **User**: vod_user
- **Password**: vod_password

Tables principales:
- `films` - Informations des films
- `artists` - Acteurs et réalisateurs
- `users` - Comptes utilisateurs
- `reservations` - Réservations de films

### MongoDB (Evaluations & Posters)

- **Host**: localhost:27017
- **Database**: vod_mongo
- **User**: admin
- **Password**: admin_password

Collections:
- `evaluations` - Notes et commentaires
- `posters` - Images des affiches

## 🧪 Tests

```bash
# Tests backend
./gradlew test

# Tests frontend
cd vod-client
npm run test
```

## 📦 Déploiement avec Docker

```bash
# Build et démarrer tous les services
docker-compose -f docker-compose-full.yaml up --build

# Arrêter tous les services
docker-compose -f docker-compose-full.yaml down

# Voir les logs
docker-compose -f docker-compose-full.yaml logs -f
```

## ✨ Fonctionnalités

### Pour les Utilisateurs

- ✅ Inscription et connexion
- ✅ Parcourir le catalogue de films
- ✅ Filtrer par genre
- ✅ Voir les détails des films
- ✅ Réserver des films (max 3 simultanés)
- ✅ Payer les réservations
- ✅ Terminer une location
- ✅ Évaluer les films (note + commentaire)
- ✅ Consulter les évaluations

### Pour les Administrateurs

- ✅ Créer/modifier/supprimer des films
- ✅ Créer/modifier/supprimer des artistes
- ✅ Ouvrir/fermer les films à la location
- ✅ Modifier les prix de location
- ✅ Upload des affiches

## 🔐 Sécurité

- Authentification par token JWT
- Validation des données côté serveur
- Protection CORS configurée
- Validation de l'âge minimum pour les films
- Maximum 3 réservations simultanées par utilisateur

## 🛠️ Technologies Utilisées

**Backend:**
- Spring Boot 3.2.0
- Spring Cloud Gateway
- Spring Data JPA
- MariaDB
- MongoDB
- Jakarta Servlet API
- Gradle

**Frontend:**
- Vue.js 3
- Vue Router
- Pinia (State Management)
- Axios
- Vite

**DevOps:**
- Docker
- Docker Compose

## 👥 Auteurs

Projet réalisé dans le cadre du Master 1 ILIADE & TIIL-A - UBO
Année 2025/26

## 📝 License

Ce projet est un projet académique.
