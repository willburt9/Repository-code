package com.openclassroom.projet11.adapter.out.distance.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Réponse retournée par GraphHopper.
 *
 * <p>
 * GraphHopper retourne ses itinéraires dans le champ
 * JSON {@code paths}.
 * </p>
 *
 * <p>
 * Pour éviter de faire dépendre l'adaptateur du nom
 * technique "paths", on expose la collection sous le nom
 * "routes" dans notre application.
 * </p>
 */
public class DirectionsResponse {

    private List<Route> routes;

    public DirectionsResponse() {
    }

    @JsonProperty("paths")
    public List<Route> getRoutes() {
        return routes;
    }

    @JsonProperty("paths")
    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }
}