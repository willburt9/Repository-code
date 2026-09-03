# Projet 11 - Backend

Backend de l'application **Projet 11**, développé avec **Java 21** et **Spring Boot**.

Le backend expose une API REST et utilise une base de données **H2**.
Il communique également avec une instance **GraphHopper** pour le calcul d'itinéraires.

---

## Technologies

* Java 21
* Spring Boot
* Maven
* H2 Database
* JUnit
* JaCoCo
* GraphHopper 11
* Docker

---

# Prérequis

Pour exécuter le backend localement, les éléments suivants doivent être installés :

* Java 21
* Git
* Docker, si l'exécution avec Docker est souhaitée

Le projet utilise le **Maven Wrapper** (`mvnw`), il n'est donc pas nécessaire d'installer Maven.

Vérifier la version de Java :

```bash
java -version
```

La version attendue est Java 21.

---

# Structure du backend

Le backend se trouve dans le répertoire :

```text
projet11/
├── pom.xml
├── mvnw
├── mvnw.cmd
├── .mvn/
└── src/
    ├── main/
    │   ├── java/
    │   └── resources/
    └── test/
        └── java/
```

---

# Tester le backend

## Windows

Depuis le répertoire `projet11` :

```powershell
.\mvnw.cmd clean test
```

## Linux / macOS

```bash
./mvnw clean test
```

Cette commande :

1. nettoie les fichiers précédemment générés ;
2. compile le projet ;
3. compile les tests ;
4. exécute les tests unitaires.

---

# Tests + JaCoCo

Pour exécuter les tests et générer le rapport de couverture JaCoCo :

### Windows

```powershell
.\mvnw.cmd clean verify
```

### Linux / macOS

```bash
./mvnw clean verify
```

Le rapport JaCoCo est généré dans :

```text
target/site/jacoco/index.html
```

Il peut être ouvert directement dans un navigateur.

---

# Builder le backend

Pour compiler et générer le fichier JAR :

### Windows

```powershell
.\mvnw.cmd clean package
```

### Linux / macOS

```bash
./mvnw clean package
```

Le JAR est généré dans :

```text
target/
```

Par exemple :

```text
target/projet11-0.0.1-SNAPSHOT.jar
```

---

## Builder sans exécuter les tests

Si les tests ont déjà été exécutés et que l'on souhaite uniquement générer le JAR :

### Windows

```powershell
.\mvnw.cmd clean package -DskipTests
```

### Linux / macOS

```bash
./mvnw clean package -DskipTests
```

> Cette commande ne doit pas remplacer l'exécution régulière des tests.

---

# Lancer le backend

## Avec Maven

### Windows

```powershell
.\mvnw.cmd spring-boot:run
```

### Linux / macOS

```bash
./mvnw spring-boot:run
```

Le backend démarre sur :

```text
http://localhost:8080
```

---

## Avec le JAR

Après avoir exécuté :

```powershell
.\mvnw.cmd clean package
```

lancer le JAR avec :

```powershell
java -jar target/*.jar
```

---

# Vérifier que le backend fonctionne

Le projet utilise **Spring Boot Actuator** avec l'endpoint `/actuator/health`.

Une fois le backend démarré :

```powershell
curl.exe http://localhost:8080/actuator/health
```

Réponse attendue :

```json
{
  "groups": [
    "liveness",
    "readiness"
  ],
  "status": "UP"
}
```

Le statut `UP` confirme que le backend est démarré et opérationnel.

---

# Dépendance de GraphHopper

GraphHopper est utilisé par le backend pour effectuer les calculs d'itinéraires.

La version utilisée dans le projet est **GraphHopper 11.0**.

GraphHopper utilise les données cartographiques de l'Île-de-France :

```text
ile-de-france-260810.osm.pbf
```

Le serveur expose son API sur le port :

```text
8989
```

---

## Structure

Le répertoire GraphHopper contient notamment :

```text
GraphHopper/
├── graphhopper-web-11.0.jar
├── config.yml
└── ile-de-france-260810.osm.pbf
```

---

## 1. Lancer GraphHopper avec le JAR

### Prérequis

Java 21 doit être installé.

Vérifier la version :

```powershell
java -version
```

### Démarrer GraphHopper

Depuis le répertoire `GraphHopper` :

```powershell
java -jar graphhopper-web-11.0.jar server config.yml
```

GraphHopper démarre alors sur :

```text
http://localhost:8989
```

> Le premier démarrage peut être plus long car GraphHopper doit importer et préparer les données cartographiques.

### Vérifier que GraphHopper fonctionne

Dans un autre terminal :

```powershell
curl.exe http://localhost:8989/info
```

Une réponse JSON doit être retournée.

Elle doit notamment contenir :

```json
{
  "profiles": [
    {
      "name": "car"
    }
  ],
  "version": "11.0"
}
```

---

## 2. Lancer GraphHopper avec Docker

L'image Docker de GraphHopper est publiée sur GitHub Container Registry.

Image :

```text
ghcr.io/willburt9/projet11-graphhopper:1.0.0
```

### Récupérer l'image

```powershell
docker pull ghcr.io/willburt9/projet11-graphhopper:1.0.0
```
L'image Docker reste disponible localement et n'a pas besoin d'être téléchargée à nouveau.

### Démarrer le conteneur

```powershell
docker run -d `
  --name graphhopper `
  -p 8989:8989 `
  ghcr.io/willburt9/projet11-graphhopper:1.0.0
```

Le port Docker est alors mappé :

```text
localhost:8989 → conteneur:8989
```

### Vérifier le conteneur

```powershell
docker ps
```

Une ligne similaire doit apparaître :

```text
graphhopper   ...   0.0.0.0:8989->8989/tcp
```

## 3. Vérification complète

Tester GraphHopper depuis la machine :

```powershell
curl.exe http://localhost:8989/info
```

Si GraphHopper est lancé dans Docker, il est également possible de vérifier son API directement depuis le conteneur :

```powershell
docker exec graphhopper sh -c "curl -s http://127.0.0.1:8989/info"
```

Lorsque le backend est lui aussi lancé dans Docker, tester la communication entre les deux conteneurs :

```powershell
docker exec projet11-backend sh -c "curl -s http://graphhopper:8989/info"
```

La dernière commande permet de vérifier que le **backend peut réellement communiquer avec GraphHopper via le réseau Docker**.

---

## Résumé

| Mode   | Commande                                                      | Adresse          |
| ------ | ------------------------------------------------------------- | ---------------- |
| JAR    | `java -jar graphhopper-web-11.0.jar server config.yml`        | `localhost:8989` |
| Docker | `docker run -d --name graphhopper -p 8989:8989 ...`           | `localhost:8989` |

Pour un environnement de développement simple, le JAR est pratique. Pour reproduire l'environnement de déploiement et la CI/CD, l'image Docker est préférable.


---

# Utilisation avec Docker

Le backend dispose également d'un `Dockerfile`.

Depuis le répertoire `projet11` :

```powershell
docker build -t projet11-backend:test .
```

Puis lancer le conteneur :

```powershell
docker run -d `
  --name projet11-backend `
  -p 8080:8080 `
  projet11-backend:test
```

Puis tester l'API :

```powershell
curl.exe http://localhost:8080/actuator/health
```

---

# Image Docker publiée sur GHCR

L'image officielle du backend est publiée sur **GitHub Container Registry (GHCR)**.

Format :

```text
ghcr.io/willburt9/projet11-backend:<version>
```

Par exemple :

```powershell
docker pull ghcr.io/willburt9/projet11-backend:latest
```

Pour lancer cette image :

```powershell
docker run -d `
  --name projet11-backend `
  -p 8080:8080 `
  ghcr.io/willburt9/projet11-backend:latest
```

---

# Configuration

Les principales propriétés du backend sont définies dans :

```text
src/main/resources/application.properties
```

Configuration de la base H2 :

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.hibernate.ddl-auto=update
```

Configuration GraphHopper :

```properties
graphhopper.api.url=http://localhost:8989
graphhopper.connect-timeout-ms=3000
graphhopper.read-timeout-ms=5000
graphhopper.cache-ttl-seconds=300
```

---

# Cycle de développement recommandé

Avant de créer un commit :

```powershell
.\mvnw.cmd clean verify
```

Cette commande permet de vérifier que :

* le projet compile ;
* les tests passent ;
* la couverture JaCoCo est générée ;
* le build Maven est valide.

Pour lancer ensuite l'application :

```powershell
.\mvnw.cmd spring-boot:run
```

Puis vérifier :

```powershell
curl.exe http://localhost:8080/actuator/health
```

---

# Résumé des commandes

| Action             | Commande                                           |
| ------------------ | -------------------------------------------------- |
| Installer/compiler | `./mvnw clean compile`                             |
| Tests              | `./mvnw clean test`                                |
| Tests + JaCoCo     | `./mvnw clean verify`                              |
| Build JAR          | `./mvnw clean package`                             |
| Build sans tests   | `./mvnw clean package -DskipTests`                 |
| Lancer Spring Boot | `./mvnw spring-boot:run`                           |
| Health check       | `curl http://localhost:8080/actuator/health`       |
| Build Docker       | `docker build -t projet11-backend:test .`          |
| Lancer Docker      | `docker run -d -p 8080:8080 projet11-backend:test` |
