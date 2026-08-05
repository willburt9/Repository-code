package com.openclassroom.projet11.adapter.out.distance.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Représente un itinéraire calculé par OpenRouteService.
 *
 * <p>
 * Pour les besoins de l'application, seule la propriété
 * {@code summary} est exploitée.
 * </p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Route {

    /**
     * Résumé de l'itinéraire.
     */
    @JsonProperty("summary")
    private Summary summary;

    /**
     * Retourne le résumé de l'itinéraire.
     *
     * @return résumé contenant distance et durée
     */
    public Summary getSummary() {
        return summary;
    }

    /**
     * Définit le résumé de l'itinéraire.
     *
     * @param summary résumé de l'itinéraire
     */
    public void setSummary(Summary summary) {
        this.summary = summary;
    }

}