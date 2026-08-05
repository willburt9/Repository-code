package com.openclassroom.projet11.adapter.out.distance.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Résumé d'un itinéraire OpenRouteService.
 *
 * <p>
 * Les unités utilisées par l'API sont :
 * </p>
 *
 * <ul>
 *     <li>distance : mètres</li>
 *     <li>duration : secondes</li>
 * </ul>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Summary {

    /**
     * Distance totale de l'itinéraire.
     *
     * Unité : mètres.
     */
    @JsonProperty("distance")
    private double distance;

    /**
     * Durée estimée du trajet.
     *
     * Unité : secondes.
     */
    @JsonProperty("duration")
    private double duration;

    /**
     * Retourne la distance de l'itinéraire.
     *
     * @return distance en mètres
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Définit la distance de l'itinéraire.
     *
     * @param distance distance en mètres
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * Retourne la durée estimée du trajet.
     *
     * @return durée en secondes
     */
    public double getDuration() {
        return duration;
    }

    /**
     * Définit la durée estimée du trajet.
     *
     * @param duration durée en secondes
     */
    public void setDuration(double duration) {
        this.duration = duration;
    }

}