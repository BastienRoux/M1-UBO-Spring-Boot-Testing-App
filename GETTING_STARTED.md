# Guide de Démarrage - Projet VOD

## Installation et Configuration

### 1. Installer les dépendances du client Vue.js

```powershell
cd vod-client
npm install
```

### 2. Démarrer les bases de données

```powershell
# Depuis la racine du projet
docker-compose -f docker-compose-full.yaml up -d mariadb mongodb
```

### 3. Configurer la base de données MariaDB

Vérifier la connexion dans `src/main/resources/application.properties` :

```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/vod_db
spring.datasource.username=vod_user
spring.datasource.password=vod_password
```

### 4. Démarrer le Film Service

```powershell
# Depuis la racine
.\gradlew bootRun
```

### 5. Démarrer l'API Gateway

```powershell
cd api-gateway
..\gradlew bootRun
```

### 6. Démarrer le client Vue.js

```powershell
cd vod-client
npm run dev
```

## URLs d'accès

- **Client Vue.js**: http://localhost:12080
- **API Gateway**: http://localhost:12081
- **Film Service**: http://localhost:12082
- **MariaDB**: localhost:3306
- **MongoDB**: localhost:27017

## Tester l'application

### 1. Créer un compte utilisateur

Aller sur http://localhost:12080/register

### 2. Se connecter

Aller sur http://localhost:12080/login

### 3. Parcourir les films

L'accès à la liste des films se fait sur la page d'accueil

## Commandes utiles

### Backend

```powershell
# Build le projet
.\gradlew build

# Lancer les tests
.\gradlew test

# Nettoyer et rebuild
.\gradlew clean build
```

### Frontend

```powershell
# Installer les dépendances
npm install

# Démarrer le serveur de développement
npm run dev

# Build pour la production
npm run build

# Preview du build de production
npm run preview
```

### Docker

```powershell
# Démarrer tous les services
docker-compose -f docker-compose-full.yaml up -d

# Arrêter tous les services
docker-compose -f docker-compose-full.yaml down

# Voir les logs
docker-compose -f docker-compose-full.yaml logs -f

# Redémarrer un service spécifique
docker-compose -f docker-compose-full.yaml restart mariadb
```

## Problèmes courants

### Port déjà utilisé

Si un port est déjà utilisé, vous pouvez :
- Arrêter le processus qui l'utilise
- Modifier le port dans la configuration

### Connexion à la base de données échoue

Vérifier que Docker est bien lancé et que les containers tournent :

```powershell
docker ps
```

### CORS errors

Assurez-vous que l'API Gateway est démarré et configuré correctement.

## Prochaines étapes

- Implémenter le service d'évaluations (Servlet)
- Implémenter le service d'affiches (Servlet)
- Implémenter le service de paiement
- Ajouter plus de fonctionnalités admin
- Améliorer les tests
