package com.openclassroom.projet11.adapter.out.distance;

import com.openclassroom.projet11.adapter.out.distance.dto.DirectionsResponse;
import com.openclassroom.projet11.adapter.out.distance.dto.Route;
import com.openclassroom.projet11.domain.model.Location;
import com.openclassroom.projet11.domain.port.out.DistanceProviderPort;
import org.springframework.stereotype.Component;

/**
 * Adaptateur pour le fournisseur de distance OpenRouteService.
 * DistanceProviderAdapter
 */
@Component
public class DistanceProviderAdapter implements DistanceProviderPort {

    private final OpenRouteServiceClient client;

    public DistanceProviderAdapter(OpenRouteServiceClient client) {
        this.client = client;
    }

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
     * Appelle OpenRouteService et récupère le premier itinéraire.
     *
     * <p>
     * Dans la majorité des cas, ORS retourne un seul itinéraire
     * optimisé. Le premier élément correspond donc au meilleur trajet.
     * </p>
     *
     * @param depart localisation de départ
     * @param arrivee localisation d'arrivée
     *
     * @return itinéraire calculé
     *
     * @throws IllegalStateException
     * si aucun itinéraire n'est disponible
     */
    private Route obtenirRoute(
            Location depart,
            Location arrivee) {


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


        return response
                .getRoutes()
                .get(0);
    }
}