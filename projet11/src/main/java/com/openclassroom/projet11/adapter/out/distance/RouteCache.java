package com.openclassroom.projet11.adapter.out.distance;

import com.openclassroom.projet11.adapter.out.distance.dto.Route;
import com.openclassroom.projet11.domain.model.Location;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Cache mémoire simple pour les résultats OpenRouteService.
 * <p>
 * Protège le quota de l'API tierce : un même trajet demandé plusieurs fois
 * pendant la fenêtre de validité n'est calculé qu'une seule fois. Élimine
 * notamment le doublon d'appel entre {@code calculerDistance} et
 * {@code calculerTempsTrajet} sur la même paire départ/arrivée.
 * <p>
 * Limitation documentée : cache local à l'instance, non partagé entre
 * plusieurs instances de l'application. Suffisant pour la PoC ; à remplacer
 * par un cache distribué (Redis, par ex.) en production multi-instances.
 */
class RouteCache {

    private static final int TAILLE_MAX = 500;

    private record Entree(Route route, Instant expiration) {
        boolean estExpiree() {
            return Instant.now().isAfter(expiration);
        }
    }

    private final Duration dureeDeVie;
    private final Map<String, Entree> cache = new ConcurrentHashMap<>();

    RouteCache(Duration dureeDeVie) {
        this.dureeDeVie = dureeDeVie;
    }

    Route get(Location depart, Location arrivee) {
        Entree entree = cache.get(cle(depart, arrivee));
        if (entree == null || entree.estExpiree()) {
            return null;
        }
        return entree.route();
    }

    void put(Location depart, Location arrivee, Route route) {
        if (cache.size() >= TAILLE_MAX) {
            // Éviction grossière (on vide tout) : suffisant pour une PoC à
            // faible volume ; une vraie LRU serait nécessaire en production.
            cache.clear();
        }
        cache.put(cle(depart, arrivee), new Entree(route, Instant.now().plus(dureeDeVie)));
    }

    private String cle(Location depart, Location arrivee) {
        return depart.latitude() + "," + depart.longitude()
                + "->" + arrivee.latitude() + "," + arrivee.longitude();
    }
}