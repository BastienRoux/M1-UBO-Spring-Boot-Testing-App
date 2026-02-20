# Client VOD Vue.js

Application frontend pour le projet de Vidéo à la Demande.

## Technologies

- Vue 3
- Vue Router
- Pinia (state management)
- Axios (HTTP client)
- Vite (build tool)

## Démarrage

```bash
npm install
npm run dev
```

L'application sera accessible sur http://localhost:12080

## Structure

```
src/
├── views/          # Pages de l'application
├── components/     # Composants réutilisables
├── services/       # Services API
├── stores/         # Stores Pinia (state management)
├── router/         # Configuration du routeur
└── style.css       # Styles globaux
```

## Pages disponibles

- `/` - Liste des films
- `/films/:id` - Détail d'un film
- `/artists` - Liste des artistes
- `/artists/:id` - Détail d'un artiste
- `/my-reservations` - Mes réservations
- `/login` - Connexion
- `/register` - Inscription
- `/admin` - Panneau d'administration

## Build pour la production

```bash
npm run build
```

Les fichiers seront générés dans le dossier `dist/`
