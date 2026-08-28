# ============================================================
# Backend - Projet 11
# ============================================================

FROM eclipse-temurin:21-jre

# Répertoire de travail dans le conteneur
WORKDIR /projet11

# Copie du JAR produit par Maven
COPY target/*.jar app.jar

# Port HTTP Spring Boot
EXPOSE 8080

# Démarrage de l'application
ENTRYPOINT ["java", "-jar", "app.jar"]