package com.openclassroom.projet11.adapter.out.distance;

import com.openclassroom.projet11.adapter.out.distance.dto.DirectionsResponse;
import com.openclassroom.projet11.adapter.out.distance.dto.Route;
import com.openclassroom.projet11.domain.model.Location;
import com.openclassroom.projet11.domain.port.out.DistanceProviderPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * DistanceProviderAdapter
 * Adaptateur pour le fournisseur de distance OpenRouteService.
 */
@Component
public class DistanceProviderAdapter implements DistanceProviderPort {

    private final OpenRouteServiceClient client;
    private final RouteCache cache;

    /**
     * @param client            client OpenRouteService pour calculer les itinéraires
     * @param cacheTtlSeconds   durée de validité (secondes) d'un trajet en cache,
     *                          pour limiter la consommation du quota ORS
     */
    public DistanceProviderAdapter(
            OpenRouteServiceClient client,
            @Value("${openrouteservice.cache-ttl-seconds:300}") long cacheTtlSeconds) {
        this.client = client;
        this.cache = new RouteCache(Duration.ofSeconds(cacheTtlSeconds));
    }

    /**
     * Calcule la distance entre deux localisations.
     *
     * @param localisationPatient localisation du patient
     * @param localisationHopital localisation de l'hôpital
     * @return distance en kilomètres
     */
    @Override
    public double calculerDistance(Location localisationPatient, Location localisationHopital) {

        Route route = obtenirRoute(
                localisationPatient,
                localisationHopital
        );

        /*
         * ORS retourne la distance en mètres.
         * Conversion en kilomètres.
         */
        return route
                .getSummary()
                .getDistance()
                / 1000.0;
    }

    /**
     * Calcule le temps de trajet entre deux localisations.
     *
     * @param localisationPatient localisation du patient
     * @param localisationHopital localisation de l'hôpital
     * @return durée du trajet en minutes
     */
    @Override
    public double calculerTempsTrajet(Location localisationPatient, Location localisationHopital) {

        Route route = obtenirRoute(
                localisationPatient,
                localisationHopital
        );

        /*
         * ORS retourne la durée en secondes.
         * Conversion en minutes.
         */
        return route
                .getSummary()
                .getDuration()
                / 60.0;
    }

    /**
     * Appelle OpenRouteService et récupère le premier itinéraire, en passant
     * d'abord par le cache : évite de refaire un appel externe pour une paire
     * départ/arrivée déjà résolue récemment (notamment le doublon entre
     * calculerDistance et calculerTempsTrajet sur le même hôpital retenu).
     *
     * <p>
     * Dans la majorité des cas, ORS retourne un seul itinéraire
     * optimisé. Le premier élément correspond donc au meilleur trajet.
     * </p>
     *
     * @param depart  localisation de départ
     * @param arrivee localisation d'arrivée
     * @return itinéraire calculé
     * @throws IllegalStateException si aucun itinéraire n'est disponible
     */
    private Route obtenirRoute(
            Location depart,
            Location arrivee) {

        Route enCache = cache.get(depart, arrivee);
        if (enCache != null) {
            return enCache;
        }

        DirectionsResponse response =
                client.calculerRoute(
                        depart,
                        arrivee
                );

        if (response.getRoutes() == null
                || response.getRoutes().isEmpty()) {

            throw new IllegalStateException(
                    "Aucun itinéraire trouvé par OpenRouteService."
            );
        }

        Route route = response
                .getRoutes()
                .get(0);

        cache.put(depart, arrivee, route);

        return route;
    }
}