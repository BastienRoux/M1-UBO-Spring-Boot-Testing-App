# 🔌 Configuration des Ports - Projet VOD

## Ports configurés (12080-12099)

| Service | Port | URL |
|---------|------|-----|
| **Vue.js Client** | 12080 | http://localhost:12080 |
| **API Gateway** | 12081 | http://localhost:12081 |
| **Film Service** | 12082 | http://localhost:12082 |
| **Evaluation Service** | 12083 | http://localhost:12083 |
| **Poster Service** | 12084 | http://localhost:12084 |
| **Payment Service** | 12085 | http://localhost:12085 |

## Bases de données (ports standards)

| Service | Port | Accès |
|---------|------|-------|
| **MariaDB** | 3306 | localhost:3306 |
| **MongoDB** | 27017 | localhost:27017 |

## 🚀 Démarrage de l'application

### 1. Installer les dépendances Vue.js (première fois)
```powershell
cd vod-client
npm install
cd ..
```

### 2. Démarrer les bases de données
```powershell
docker-compose -f docker-compose-full.yaml up -d mariadb mongodb
```

### 3. Démarrer le Film Service (Terminal 1)
```powershell
.\gradlew bootRun
```
✅ Service disponible sur **http://localhost:12082**

### 4. Démarrer l'API Gateway (Terminal 2)
```powershell
cd api-gateway
..\gradlew bootRun
```
✅ Gateway disponible sur **http://localhost:12081**

### 5. Démarrer le client Vue.js (Terminal 3)
```powershell
cd vod-client
npm run dev
```
✅ Application disponible sur **http://localhost:12080**

## 📱 Accéder à l'application

Ouvrez votre navigateur : **http://localhost:12080**

## 🧪 Tester les endpoints

### Via l'API Gateway (recommandé)
```powershell
# Liste des films
curl http://localhost:12081/api/films

# Liste des artistes
curl http://localhost:12081/api/artists

# Liste des reviews
curl http://localhost:12081/api/reviews
```

### Directement sur le Film Service
```powershell
# Accès direct (sans passer par la gateway)
curl http://localhost:12082/reviews
curl http://localhost:12082/artists
curl http://localhost:12082/dogs
```

## 📊 Architecture réseau

```
┌─────────────────────┐
│   Navigateur Web    │
│  localhost:12080    │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│   Vue.js Client     │
│     Port 12080      │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│   API Gateway       │
│     Port 12081      │  ← Point d'entrée unique
└──────────┬──────────┘
           │
    ┌──────┴──────┬──────────┬──────────┐
    ▼             ▼          ▼          ▼
┌────────┐  ┌─────────┐  ┌───────┐  ┌─────────┐
│ Film   │  │ Eval.   │  │Poster │  │Payment  │
│ 12082  │  │ 12083   │  │12084  │  │ 12085   │
└────┬───┘  └────┬────┘  └───┬───┘  └─────────┘
     │           │            │
     ▼           ▼            ▼
┌─────────┐  ┌──────────┐
│ MariaDB │  │ MongoDB  │
│  3306   │  │  27017   │
└─────────┘  └──────────┘
```

## ⚙️ Configuration CORS

L'API Gateway autorise les requêtes depuis :
- `http://localhost:12080` (Vue.js)

## 🛑 Arrêter l'application

```powershell
# Dans chaque terminal : Ctrl+C

# Arrêter les bases de données
docker-compose -f docker-compose-full.yaml down
```

## 📝 Notes

- Tous les ports sont dans la plage **12080-12099** comme demandé
- L'API Gateway route toutes les requêtes `/api/*` vers les services backend
- Le client Vue.js est configuré pour appeler l'API Gateway
- Les bases de données utilisent leurs ports standards (non modifiables facilement)
