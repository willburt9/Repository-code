package com.openclassroom.projet11.adapter.out.distance.dto;

import java.util.List;

/**
 * Corps de la requête POST /v2/directions/driving-car adressée à OpenRouteService.
 */
public record DirectionsRequest(List<List<Double>> coordinates) {

    /**
     * @param departLongitude  longitude du point de départ
     * @param departLatitude   latitude du point de départ
     * @param arriveeLongitude longitude du point d'arrivée
     * @param arriveeLatitude  latitude du point d'arrivée
     */
    public static DirectionsRequest de(
            double departLongitude, double departLatitude,
            double arriveeLongitude, double arriveeLatitude) {

        return new DirectionsRequest(List.of(
                List.of(departLongitude, departLatitude),
                List.of(arriveeLongitude, arriveeLatitude)
        ));
    }
}