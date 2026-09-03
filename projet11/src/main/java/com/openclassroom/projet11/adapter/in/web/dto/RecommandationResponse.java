package com.openclassroom.projet11.adapter.in.web.dto;

import com.openclassroom.projet11.application.port.in.RecommandationResultat;
import com.openclassroom.projet11.domain.model.Hopital;

/**
 * DTO pour la réponse de recommandation d'hôpital.
 * RecommandationResponse
 * @param hopitalId l'identifiant de l'hôpital recommandé   
 * @param nom le nom de l'hôpital recommandé
 * @param adresse l'adresse de l'hôpital recommandé
 * @param latitude la latitude de l'hôpital recommandé
 * @param longitude la longitude de l'hôpital recommandé
 * @param litsDisponibles le nombre de lits disponibles à l'hôpital recommandé
 * @param distanceKm la distance en kilomètres entre le patient et l'hôpital recommandé
 * @param dureeMinutes la durée en minutes du trajet entre le patient et l'hôpital recommandé
 */
public record RecommandationResponse(
        Long hopitalId,
        String nom,
        String adresse,
        double latitude,
        double longitude,
        int litsDisponibles,
        double distanceKm,
        double dureeMinutes
) {

    public static RecommandationResponse from(RecommandationResultat resultat) {
        Hopital hopital = resultat.hopital();
        return new RecommandationResponse(
                hopital.getId(),
                hopital.getNom(),
                hopital.getAdresse(),
                hopital.getLocalisation().latitude(),
                hopital.getLocalisation().longitude(),
                hopital.getLitsDisponibles(),
                arrondir(resultat.distanceKm()),
                arrondir(resultat.dureeMinutes())
        );
    }

    /**
     * Arrondit une valeur à deux décimales.
     * @param valeur
     * @return la valeur arrondie à deux décimales
     */
    private static double arrondir(double valeur) {
        return Math.round(valeur * 100.0) / 100.0;
    }
}