# API Gateway

API Gateway pour le projet VOD utilisant Spring Cloud Gateway.

## Configuration

- **Port**: 12081
- **Framework**: Spring Cloud Gateway

## Routes configurées

- `/api/films/**` → Film Service (port 12082)
- `/api/artists/**` → Film Service (port 12082)
- `/api/reservations/**` → Film Service (port 12082)
- `/api/users/**` → Film Service (port 12082)
- `/api/evaluations/**` → Evaluation Service (port 12083)
- `/api/posters/**` → Poster Service (port 12084)
- `/api/payments/**` → Payment Service (port 12085)

## Démarrage

```bash
../gradlew bootRun
```

## CORS

Configuré pour accepter les requêtes depuis `http://localhost:12080` (Vue.js dev server)
