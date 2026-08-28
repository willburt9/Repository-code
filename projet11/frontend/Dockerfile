# ============================================================
# Frontend - Projet 11
# ============================================================

# ------------------------------------------------------------
# Étape 1 : Build
# ------------------------------------------------------------

FROM node:22-alpine AS build

WORKDIR /projet11

# Copie des fichiers de dépendances
COPY package*.json ./

# Installation reproductible des dépendances
RUN npm ci

# Copie du code frontend
COPY . .

# Construction de l'application
RUN npm run build


# ------------------------------------------------------------
# Étape 2 : Serveur HTTP
# ------------------------------------------------------------

FROM nginx:alpine

# Suppression de la configuration Nginx par défaut
RUN rm /etc/nginx/conf.d/default.conf

# Configuration Nginx pour l'application
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copie du build Vite
COPY --from=build /projet11/dist /usr/share/nginx/html

# Port HTTP
EXPOSE 80

# Nginx au premier plan
CMD ["nginx", "-g", "daemon off;"]