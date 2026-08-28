# Projet 11 - PoC

## 1. Présentation

Cette application est une Proof of Concept (PoC) composée de trois composants principaux :

* **Backend** : API REST développée avec Spring Boot et Java 21.
* **Frontend** : application web développée avec React, TypeScript et Vite.
* **GraphHopper** : moteur de calcul d'itinéraires utilisé par le backend.

L'application permet notamment de rechercher des établissements de santé et de proposer un routage vers un établissement disposant des ressources nécessaires.

L'architecture de la PoC est la suivante :

```text
                    ┌─────────────────────┐
                    │      Frontend       │
                    │ React / TypeScript  │
                    │       / Vite        │
                    │      :5173          │
                    └──────────┬──────────┘
                               │
                               │ HTTP REST
                               ▼
                    ┌─────────────────────┐
                    │       Backend       │
                    │ Spring Boot / Java  │
                    │       :8080         │
                    └──────────┬──────────┘
                               │
                               │ HTTP
                               ▼
                    ┌─────────────────────┐
                    │     GraphHopper     │
                    │     Routing API     │
                    │       :8989         │
                    └─────────────────────┘
```

---

# 2. Technologies principales

| Composant             | Technologie                      |
| --------------------- | -------------------------------- |
| Backend               | Java 21                          |
| Framework backend     | Spring Boot 4.1.0                |
| Build backend         | Maven                            |
| API                   | REST                             |
| Documentation API     | OpenAPI / Swagger                |
| Base de données       | H2                               |
| Frontend              | React 19                         |
| Langage frontend      | TypeScript                       |
| Build frontend        | Vite                             |
| Tests frontend        | Vitest + React Testing Library   |
| Tests E2E             | Cypress                          |
| Routage               | GraphHopper 11                   |
| Conteneurisation      | Docker                           |
| Orchestration locale  | Docker Compose                   |
| Registry              | GitHub Container Registry (GHCR) |
| CI/CD                 | GitHub Actions                   |
| Versionning           | Semantic Release                 |
| Convention de commits | Conventional Commits             |
| Gestion du code       | Git                              |

---

# 3. Structure du projet

```text
projet11/
│
├── src/
│   └── ...                    # Code source backend
│
├── pom.xml                   # Configuration Maven
├── mvnw                      # Maven Wrapper Linux/macOS
├── mvnw.cmd                  # Maven Wrapper Windows
│
├── README-BACKEND.md         # Documentation backend
├── Dockerfile                # Dockerfile backend
│
├── frontend/
│   ├── src/                  # Code source React
│   ├── public/               # Ressources statiques
│   ├── package.json          # Dépendances et scripts
│   ├── package-lock.json
│   ├── vite.config.ts        # Configuration Vite
│   ├── Dockerfile            # Dockerfile frontend
│   ├── README.md             # Documentation frontend
│   └── nginx.conf            # Configuration Nginx
│
├── GraphHopper/
│   ├── Dockerfile
│   ├── graphhopper-web-11.0.jar
│   ├── config.yml
│   └── ile-de-france-260810.osm.pbf
│
├── docker-compose.yml        # Orchestration locale des 3 services
│
├── .github/
│   └── workflows/
│       └── ci.yml            # Pipeline GitHub Actions
│
└── README.md
```

---

# 4. Prérequis

Pour exécuter la PoC localement, installer :

* Java 21
* Node.js 22
* npm
* Docker Desktop
* Git
* Docker Compose

Docker Compose est intégré à Docker Desktop et peut être vérifié avec :

```bash
docker compose version
```

Vérifier également :

```bash
java -version
node --version
npm --version
docker --version
docker compose version
git --version
```

Maven n'a pas besoin d'être installé manuellement car le projet utilise le **Maven Wrapper**.

---

# 5. Installation du projet

Cloner le repository :

```bash
git clone https://github.com/willburt9/Repository-code.git
```

Puis accéder au projet :

```bash
cd projet11
```

Installer les dépendances frontend :

```bash
cd frontend
npm ci
cd ..
```

---

# 6. Builder et exécuter la PoC

La PoC comporte trois services :

1. GraphHopper
2. Backend Spring Boot
3. Frontend React

Les services peuvent être démarrés individuellement ou avec **Docker Compose**.

Pour une exécution locale complète et reproductible, Docker Compose est la méthode recommandée.

---

# 7. Démarrer GraphHopper

GraphHopper peut être exécuté de deux manières :

* directement avec le JAR ;
* avec Docker.

## 7.1 Exécution avec le JAR

Depuis le répertoire `GraphHopper` :

```bash
cd GraphHopper
```

Puis :

```bash
java -jar graphhopper-web-11.0.jar server config.yml
```

GraphHopper démarre sur :

```text
http://localhost:8989
```

Vérifier son fonctionnement :

```bash
curl http://localhost:8989/info
```

---

## 7.2 Exécution avec Docker

L'image GraphHopper est disponible sur GitHub Container Registry :

```bash
docker pull ghcr.io/willburt9/projet11-graphhopper:1.0.0
```

Puis :

```bash
docker run -d \
  --name graphhopper \
  -p 8989:8989 \
  ghcr.io/willburt9/projet11-graphhopper:1.0.0
```

Vérifier :

```bash
docker ps
```

Puis :

```bash
curl http://localhost:8989/info
```

---

# 8. Démarrer le backend

Le backend Spring Boot écoute sur le port `8080`.

## 8.1 Avec Maven

### Windows

```powershell
.\mvnw.cmd spring-boot:run
```

### Linux / macOS

```bash
./mvnw spring-boot:run
```

Le backend est disponible sur :

```text
http://localhost:8080
```

Vérifier son état :

```bash
curl http://localhost:8080/actuator/health
```

Une réponse contenant :

```json
{
  "status": "UP"
}
```

indique que le backend fonctionne.

---

# 9. Démarrer le frontend

Le frontend est développé avec React, TypeScript et Vite.

Depuis le répertoire `frontend` :

```bash
cd frontend
```

Installer les dépendances :

```bash
npm ci
```

Puis démarrer le serveur de développement :

```bash
npm run dev
```

Le frontend est accessible sur :

```text
http://localhost:5173
```

Le frontend communique avec le backend via :

```text
http://localhost:8080
```

La variable utilisée par Vite est :

```text
VITE_API_BASE_URL
```

---

# 10. Exécution complète avec Docker Compose

Docker Compose permet de démarrer automatiquement les trois composants de la PoC :

```text
Frontend
   │
   ▼
Backend
   │
   ▼
GraphHopper
```

Le fichier utilisé est :

```text
docker-compose.yml
```

## 10.1 Démarrer les trois services

Depuis la racine du projet :

```bash
docker compose up -d
```

Docker Compose démarre :

```text
projet11-frontend
projet11-backend
graphhopper
```

---

## 10.2 Vérifier les services

GraphHopper :

```bash
curl http://localhost:8989/info
```

Backend :

```bash
curl http://localhost:8080/actuator/health
```

Frontend :

```text
http://localhost:5173
```

---

## 10.3 Arrêter la PoC

Pour arrêter les trois services :

```bash
docker compose down
```

Cette commande arrête et supprime les conteneurs créés par Compose.

Les images Docker ne sont pas supprimées.

---

## 10.4 Reconstruire les images

Après une modification du code ou d'un Dockerfile :

```bash
docker compose build
```

Puis démarrer les services :

```bash
docker compose up -d
```

Il est également possible de reconstruire et démarrer directement :

```bash
docker compose up -d --build
```

---

## 10.5 Repartir d'une installation propre

Pour arrêter les services et supprimer également les conteneurs :

```bash
docker compose down
```

Puis reconstruire les images :

```bash
docker compose build --no-cache
```

Et redémarrer :

```bash
docker compose up -d
```

---

# 11. Exécution de la PoC avec les images Docker

Les trois composants possèdent une image Docker :

```text
projet11-backend
projet11-frontend
projet11-graphhopper
```

L'architecture Docker est :

```text
                         Docker
                           │
             ┌─────────────┴─────────────┐
             │                           │
             ▼                           ▼
   ┌─────────────────┐         ┌─────────────────┐
   │ projet11-frontend│         │ projet11-backend│
   │      :5173       │ ──────► │      :8080      │
   └─────────────────┘         └────────┬────────┘
                                        │
                                        ▼
                               ┌─────────────────┐
                               │   graphhopper   │
                               │      :8989      │
                               └─────────────────┘
```

Les trois conteneurs peuvent être démarrés manuellement, mais **Docker Compose est recommandé** car il crée et gère automatiquement le réseau entre les services.

---

# 12. Tests

Le projet comporte plusieurs niveaux de tests :

```text
┌──────────────────────┐
│   Tests Backend      │
│   Maven + JaCoCo     │
└──────────┬───────────┘
           │
           │ parallèle
           │
┌──────────▼───────────┐
│   Tests Frontend     │
│ Vitest + RTL         │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│    Tests Cypress     │
│       E2E             │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│   Semantic Release   │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│    Docker + GHCR     │
└──────────────────────┘
```

Les tests backend et frontend sont exécutés **en parallèle dans la pipeline GitHub Actions**.

---

# 13. Tests backend

Depuis la racine :

### Windows

```powershell
.\mvnw.cmd test
```

### Linux / macOS

```bash
./mvnw test
```

Pour exécuter la chaîne complète Maven :

### Windows

```powershell
.\mvnw.cmd clean verify
```

### Linux / macOS

```bash
./mvnw clean verify
```

Cette commande :

1. nettoie les anciens artefacts ;
2. compile le projet ;
3. compile les tests ;
4. exécute les tests ;
5. génère les rapports JaCoCo.

Le rapport JaCoCo est généré dans :

```text
target/site/jacoco/
```

---

# 14. Tests frontend

Depuis `frontend/` :

```bash
npm run test:run
```

Tests en mode watch :

```bash
npm test
```

Couverture :

```bash
npm run test:coverage
```

---

# 15. Tests End-to-End avec Cypress

Les tests Cypress vérifient le fonctionnement de l'application complète.

Ils nécessitent :

```text
Frontend     : 5173
Backend      : 8080
GraphHopper  : 8989
```

Depuis `frontend/` :

```bash
npx cypress run
```

Le pipeline GitHub Actions démarre automatiquement les services nécessaires avant d'exécuter les tests E2E.

---

# 16. Build backend

Depuis la racine :

### Windows

```powershell
.\mvnw.cmd clean package
```

### Linux / macOS

```bash
./mvnw clean package
```

Le fichier JAR est généré dans :

```text
target/
```

---

# 17. Build frontend

Depuis `frontend/` :

```bash
npm run build
```

Le build produit :

```text
frontend/dist/
```

Le linting peut être exécuté avec :

```bash
npm run lint
```

---

# 18. Build des images Docker

## 18.1 Backend

Depuis la racine :

```bash
docker build \
  -t projet11-backend:test \
  .
```

---

## 18.2 Frontend

Depuis `frontend/` :

```bash
docker build \
  --build-arg VITE_API_BASE_URL=http://localhost:8080 \
  -t projet11-frontend:test \
  .
```

---

## 18.3 GraphHopper

L'image GraphHopper est construite séparément à partir du répertoire `GraphHopper`.

La version publiée utilisée par la PoC est disponible dans GHCR.

---

# 19. Pipeline GitHub Actions

Le pipeline est défini dans :

```text
.github/workflows/ci.yml
```

Il est déclenché sur :

* `push` vers `main`
* `push` vers `Develop`
* Pull Request vers `main`
* Pull Request vers `Develop`

La chaîne principale est :

```text
                  GitHub Actions
                        │
             ┌──────────┴──────────┐
             │                     │
             ▼                     ▼
        ┌─────────┐           ┌─────────┐
        │ Backend │           │ Frontend│
        │ Tests   │           │ Tests   │
        │ Build   │           │ Build   │
        │ JaCoCo  │           │         │
        └────┬────┘           └────┬────┘
             │                     │
             └──────────┬──────────┘
                        │
                        ▼
                   ┌─────────┐
                   │ Cypress │
                   │   E2E   │
                   └────┬────┘
                        │
                        ▼
               ┌─────────────────┐
               │ Semantic Release│
               └────────┬────────┘
                        │
                        ▼
                  ┌───────────┐
                  │   Docker  │
                  │ Build/Push│
                  └─────┬─────┘
                        │
                        ▼
                       GHCR
```

---

# 20. Job Backend

Le job `backend` :

1. récupère le code ;
2. installe Java 21 ;
3. vérifie la structure du projet ;
4. rend Maven Wrapper exécutable ;
5. exécute :

```bash
./mvnw clean verify
```

6. génère JaCoCo ;
7. publie le rapport comme artefact GitHub Actions.

---

# 21. Job Frontend

Le job `frontend` :

1. récupère le code ;
2. installe Node.js 22 ;
3. installe les dépendances :

```bash
npm ci
```

4. exécute les tests Vitest ;
5. génère la couverture ;
6. construit l'application :

```bash
npm run build
```

Le job Backend et le job Frontend sont exécutés **en parallèle**.

---

# 22. Job Cypress

Le job `cypress` dépend des jobs :

```text
backend
frontend
```

Une fois ces deux jobs terminés avec succès, Cypress démarre les services nécessaires :

```text
GraphHopper
     ↓
Backend
     ↓
Frontend
```

Puis exécute :

```bash
npx cypress run
```

Les screenshots et vidéos Cypress sont publiés comme artefacts en cas d'échec.

---

# 23. Semantic Release

Le job `semantic-release` dépend de :

```text
backend
frontend
cypress
```

Il est exécuté après un `push` vers :

```text
main
Develop
```

Semantic Release analyse les commits depuis la dernière version et détermine automatiquement la prochaine version.

Il peut créer :

* une nouvelle version ;
* un tag Git ;
* une GitHub Release.

---

# 24. Règles de versionning

Le projet utilise **Semantic Versioning** :

```text
MAJOR.MINOR.PATCH
```

Exemple :

```text
1.4.2
```

## MAJOR

Modification incompatible :

```text
1.4.2 → 2.0.0
```

## MINOR

Nouvelle fonctionnalité compatible :

```text
1.4.2 → 1.5.0
```

## PATCH

Correction compatible :

```text
1.4.2 → 1.4.3
```

Les versions sont générées automatiquement par Semantic Release.

Il n'est donc pas nécessaire de modifier manuellement la version dans le code.

---

# 25. Conventional Commits

Le versionning automatique repose sur les messages de commit.

Format :

```text
type: description
```

| Type       | Utilisation                      |
| ---------- | -------------------------------- |
| `feat`     | Nouvelle fonctionnalité          |
| `fix`      | Correction d'un bug              |
| `refactor` | Refactoring                      |
| `test`     | Ajout ou modification de tests   |
| `docs`     | Documentation                    |
| `build`    | Modification du système de build |
| `ci`       | Modification de la CI            |
| `chore`    | Maintenance technique            |

Exemples :

```text
feat: add hospital search
fix: correct routing
test: add routing service tests
docs: update README
ci: update docker workflow
```

---

# 26. Build et publication Docker

Après Semantic Release, le job Docker récupère la version créée par Semantic Release.

Les images suivantes sont construites :

```text
projet11-backend
projet11-frontend
```

Puis elles sont publiées dans GitHub Container Registry.

Convention :

```text
ghcr.io/<github-user>/projet11-backend:<version>
ghcr.io/<github-user>/projet11-frontend:<version>
```

Une image `latest` est également publiée :

```text
ghcr.io/<github-user>/projet11-backend:latest
ghcr.io/<github-user>/projet11-frontend:latest
```

Exemple :

```text
ghcr.io/willburt9/projet11-backend:1.0.0
ghcr.io/willburt9/projet11-frontend:1.0.0
```

GraphHopper possède sa propre image Docker :

```text
ghcr.io/willburt9/projet11-graphhopper:1.0.0
```

---

# 27. Workflow Git

Le projet utilise principalement les branches :

```text
main
Develop
```

## `main`

Branche stable contenant les versions destinées à être publiées.

## `Develop`

Branche d'intégration des développements.

Les fonctionnalités sont développées dans des branches dédiées :

```text
feature/<description>
fix/<description>
test/<description>
```

---

# 28. Pull Request

Une Pull Request doit :

* avoir un objectif clairement identifié ;
* contenir uniquement les modifications nécessaires ;
* respecter les conventions de commit ;
* inclure les tests nécessaires ;
* passer le pipeline CI ;
* être relue avant fusion.

Le pipeline doit être entièrement vert avant intégration.

---

# 29. Chaîne de validation locale

Avant de créer une Pull Request, il est recommandé d'exécuter :

## Backend

### Windows

```powershell
.\mvnw.cmd clean verify
```

### Linux / macOS

```bash
./mvnw clean verify
```

## Frontend

```bash
cd frontend

npm run lint
npm run test:run
npm run build
```

## Tests E2E

Après démarrage de GraphHopper, du backend et du frontend :

```bash
npx cypress run
```

---

# 30. Docker Compose pour la validation locale

Pour vérifier l'ensemble de l'application dans un environnement conteneurisé :

```bash
docker compose up -d --build
```

Puis vérifier :

```bash
docker compose ps
```

Tester les services :

```bash
curl http://localhost:8989/info
curl http://localhost:8080/actuator/health
```

Puis ouvrir :

```text
http://localhost:5173
```

Pour arrêter l'environnement :

```bash
docker compose down
```

Cette méthode permet de valider l'intégration :

```text
React
  │
  ▼
Spring Boot
  │
  ▼
GraphHopper
```

sans avoir à démarrer manuellement les trois composants dans différents terminaux.

---

# 31. Vérification Docker

Lister les images :

```bash
docker images
```

Lister les conteneurs :

```bash
docker ps
```

Avec Docker Compose :

```bash
docker compose ps
```

Afficher les logs :

```bash
docker compose logs -f
```

---

# 32. Bonnes pratiques

Avant toute livraison :

* ne pas committer les dépendances générées ;
* ne pas committer les secrets ;
* maintenir les tests associés aux fonctionnalités ;
* maintenir les README à jour ;
* utiliser Conventional Commits ;
* conserver des commits courts et explicites ;
* vérifier le build complet avant fusion ;
* éviter les modifications non liées au sujet de la branche ;
* ne pas modifier manuellement les versions générées par Semantic Release ;
* vérifier que les images Docker sont correctement publiées dans GHCR ;
* utiliser Docker Compose pour valider l'intégration des trois services.

---

# 33. Résumé de la chaîne de livraison

La chaîne complète de livraison est :

```text
                         Développement
                               │
                               ▼
                         Feature branch
                               │
                               ▼
                          Pull Request
                               │
                               ▼
                    ┌──────────┴──────────┐
                    │                     │
                    ▼                     ▼
               BACKEND               FRONTEND
               Tests                 Tests
               Build                 Build
               JaCoCo
                    │                     │
                    └──────────┬──────────┘
                               │
                               ▼
                            CYPRESS
                              E2E
                               │
                               ▼
                       SEMANTIC RELEASE
                               │
                    ┌──────────┼──────────┐
                    ▼          ▼          ▼
                 Version      Tag     GitHub Release
                               │
                               ▼
                            DOCKER
                               │
                       ┌───────┴───────┐
                       ▼               ▼
                    Backend         Frontend
                       │               │
                       └───────┬───────┘
                               │
                               ▼
                              GHCR
```

---

# 34. Documentation complémentaire

* [README Backend](./README-BACKEND.md)
* [README Frontend](./frontend/README.md)
* [Documentation Spring Boot](https://spring.io/projects/spring-boot)
* [Documentation React](https://react.dev/)
* [Documentation Vite](https://vite.dev/)
* [Documentation Vitest](https://vitest.dev/)
* [Documentation Cypress](https://www.cypress.io/)
* [Documentation GraphHopper](https://www.graphhopper.com/)
* [Documentation Docker](https://docs.docker.com/)
* [Documentation Docker Compose](https://docs.docker.com/compose/)
* [Documentation GitHub Actions](https://docs.github.com/actions)
* [Documentation Semantic Release](https://semantic-release.gitbook.io/)
* [Documentation Conventional Commits](https://www.conventionalcommits.org/)
