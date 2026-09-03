package com.openclassroom.projet11.adapter.in.web.dto;

import com.openclassroom.projet11.domain.model.Hopital;
import com.openclassroom.projet11.domain.model.Specialite;

import java.util.List;

/**
 * Classe de réponse pour représenter un hôpital dans les réponses de l'API.
 * HopitalResponse
 * @param id
 * @param nom
 * @param adresse
 * @param latitude
 * @param longitude
 * @param litsDisponibles
 * @param specialites
 */
public record HopitalResponse(
        Long id,
        String nom,
        String adresse,
        double latitude,
        double longitude,
        int litsDisponibles,
        List<String> specialites
) {

    public static HopitalResponse from(Hopital hopital) {
        return new HopitalResponse(
                hopital.getId(),
                hopital.getNom(),
                hopital.getAdresse(),
                hopital.getLocalisation().latitude(),
                hopital.getLocalisation().longitude(),
                hopital.getLitsDisponibles(),
                hopital.getSpecialites().stream().map(Specialite::getNom).toList()
        );
    }
}