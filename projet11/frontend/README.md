# Projet 11 - Frontend

Frontend de l'application **Projet 11**, développé avec **Vue.js** et **Vite**.

Le frontend communique avec le backend Spring Boot via une API REST.

---

## Technologies

* Node.js 22
* npm
* React
* Vite
* Vitest
* Cypress
* Docker
* Nginx

---

# Prérequis

Les éléments suivants doivent être installés :

* Node.js 22
* npm
* Git
* Docker, si l'exécution avec Docker est souhaitée

Vérifier les versions :

```powershell
node --version
npm --version
```

---

# Structure du frontend

Le frontend se trouve dans :

```text
projet11/
└── frontend/
    ├── package.json
    ├── package-lock.json
    ├── Dockerfile
    ├── nginx.conf
    ├── index.html
    ├── src/
    ├── public/
    └── cypress/
```

---

# Installation des dépendances

Depuis le répertoire `frontend` :

```powershell
npm ci
```

`npm ci` utilise le fichier `package-lock.json` afin d'installer exactement les versions de dépendances définies par le projet.

---

# Tester le frontend

## Tests unitaires

Les tests utilisent **Vitest**.

Lancer les tests :

```powershell
npm run test:run
```

Pour lancer Vitest en mode interactif :

```powershell
npm run test
```

---

# Couverture de code

Générer le rapport de couverture :

```powershell
npm run test:coverage
```

Le rapport est généré dans :

```text
coverage/
```

Selon la configuration du projet, le rapport HTML peut être consulté dans :

```text
coverage/index.html
```

---

# Builder le frontend

Construire l'application avec Vite :

```powershell
npm run build
```

Le build est généré dans :

```text
dist/
```

La structure obtenue est généralement :

```text
dist/
├── index.html
└── assets/
```

---

# Lancer le frontend en développement

Démarrer le serveur Vite :

```powershell
npm run dev
```

Par défaut, Vite démarre sur :

```text
http://localhost:5173
```

---

# Configuration de l'API

L'URL du backend est configurée avec la variable d'environnement :

```text
VITE_API_BASE_URL
```

Exemple :

```text
VITE_API_BASE_URL=http://localhost:8080
```

Avec PowerShell :

```powershell
$env:VITE_API_BASE_URL="http://localhost:8080"
npm run dev
```

> Les variables `VITE_*` sont intégrées au frontend lors du build. Une modification de `VITE_API_BASE_URL` nécessite donc de reconstruire l'application lorsque le frontend est utilisé en production.

---

# Tester avec le backend

Pour tester le frontend avec le backend local :

### 1. Démarrer GraphHopper

```powershell
cd graphhopper && java -jar graphhopper-web-11.0.jar server config.yml
```

### 2. Démarrer le backend

Depuis le répertoire `projet11` :

```powershell
.\mvnw.cmd spring-boot:run
```

Le backend doit être accessible sur :

```text
http://localhost:8080
```

### 3. Démarrer le frontend

Depuis `frontend` :

```powershell
$env:VITE_API_BASE_URL="http://localhost:8080"
npm run dev
```

Le frontend est alors accessible sur :

```text
http://localhost:5173
```

---

# Tests E2E avec Cypress

Le projet utilise **Cypress** pour les tests end-to-end.

Le backend et le frontend doivent être démarrés avant de lancer Cypress.

Lancer Cypress :

```powershell
npx cypress run
```

Pour ouvrir l'interface graphique Cypress :

```powershell
npx cypress open
```

Les screenshots sont générés dans :

```text
cypress/screenshots/
```

Les vidéos sont générées dans :

```text
cypress/videos/
```

---

# Docker

Le frontend dispose d'un Dockerfile.

La première étape construit l'application avec Node.js et Vite.
La seconde étape utilise Nginx pour servir les fichiers statiques générés.

---

## Construire l'image Docker

Depuis le répertoire `frontend` :

```powershell
docker build `
  --build-arg VITE_API_BASE_URL=http://localhost:8080 `
  -t projet11-frontend:test `
  .
```

---

## Lancer le frontend avec Docker

```powershell
docker run -d `
  --name projet11-frontend `
  -p 5173:80 `
  projet11-frontend:test
```

Le frontend est alors accessible sur :

```text
http://localhost:5173
```

---

# Utiliser l'image publiée sur GHCR

L'image du frontend est publiée sur GitHub Container Registry.

Format :

```text
ghcr.io/willburt9/projet11-frontend:<version>
```

Récupérer une version :

```powershell
docker pull ghcr.io/willburt9/projet11-frontend:latest
```

Lancer l'image :

```powershell
docker run -d `
  --name projet11-frontend `
  -p 5173:80 `
  ghcr.io/willburt9/projet11-frontend:latest
```

---

# Vérifier le conteneur

Afficher les conteneurs actifs :

```powershell
docker ps
```

Vérifier que le frontend répond :

```powershell
curl.exe http://localhost:5173/
```

Ou ouvrir directement :

```text
http://localhost:5173
```

---

# Cycle de développement recommandé

Après une modification du frontend :

### 1. Installer les dépendances

```powershell
npm ci
```

### 2. Exécuter les tests

```powershell
npm run test:run
```

### 3. Générer la couverture

```powershell
npm run test:coverage
```

### 4. Construire le frontend

```powershell
npm run build
```

### 5. Lancer le frontend

```powershell
$env:VITE_API_BASE_URL="http://localhost:8080"
npm run dev
```

### 6. Exécuter les tests E2E

Avec le backend et le frontend démarrés :

```powershell
npx cypress run
```

---

# Résumé des commandes

| Action                    | Commande                                                                   |
| ------------------------- | -------------------------------------------------------------------------- |
| Installer les dépendances | `npm ci`                                                                   |
| Tests unitaires           | `npm run test:run`                                                         |
| Tests interactifs         | `npm run test`                                                             |
| Couverture                | `npm run test:coverage`                                                    |
| Build                     | `npm run build`                                                            |
| Serveur de développement  | `npm run dev`                                                              |
| Tests Cypress             | `npx cypress run`                                                          |
| Cypress graphique         | `npx cypress open`                                                         |
| Build Docker              | `docker build -t projet11-frontend:test .`                                 |
| Lancer Docker             | `docker run -d --name projet11-frontend -p 5173:80 projet11-frontend:test` |

