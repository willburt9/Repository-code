package com.openclassroom.projet11.adapter.in.web.dto;

import com.openclassroom.projet11.domain.model.Specialite;

/**
 * Classe de réponse pour représenter une spécialité dans les réponses de l'API.
 * SpecialiteResponse
 * @param id
 * @param nom
 */
public record SpecialiteResponse(Long id, String nom) {

    public static SpecialiteResponse from(Specialite specialite) {
        return new SpecialiteResponse(specialite.getId(), specialite.getNom());
    }
}