# ============================================================
# Backend - Projet 11
# ============================================================

# ------------------------------------------------------------
# Étape 1 : Build
# ------------------------------------------------------------

FROM eclipse-temurin:21-jdk AS build

WORKDIR /projet11

COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B

COPY src src

RUN ./mvnw clean package -DskipTests

# ------------------------------------------------------------
# Étape 2
# ------------------------------------------------------------

FROM eclipse-temurin:21-jre

WORKDIR /projet11

COPY --from=build /projet11/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]