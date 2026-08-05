package com.openclassroom.projet11.adapter.out.distance.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Représente la réponse renvoyée par l'API OpenRouteService Directions.
 *
 * <p>
 * Seule la propriété {@code routes} est utilisée par l'application.
 * Chaque élément contient un itinéraire calculé entre deux points.
 * </p>
 *
 * <p>Exemple de réponse :</p>
 *
 * <pre>
 * {
 *   "routes": [
 *     {
 *       "summary": {
 *         "distance": 15423.5,
 *         "duration": 1324.8
 *       }
 *     }
 *   ]
 * }
 * </pre>
 *
 * Les autres propriétés de la réponse JSON sont ignorées.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DirectionsResponse {

    /**
     * Liste des itinéraires calculés.
     */
    @JsonProperty("routes")
    private List<Route> routes;

    /**
     * Retourne les itinéraires calculés.
     *
     * @return liste des itinéraires
     */
    public List<Route> getRoutes() {
        return routes;
    }

    /**
     * Définit la liste des itinéraires.
     *
     * @param routes itinéraires renvoyés par l'API
     */
    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

}