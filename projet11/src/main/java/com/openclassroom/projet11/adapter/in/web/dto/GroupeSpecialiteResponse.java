package com.openclassroom.projet11.adapter.in.web.dto;

import com.openclassroom.projet11.application.port.in.GroupeAvecSpecialites;

import java.util.List;

/**
 * DTO de réponse pour un groupe de spécialités.
 * GroupeSpecialiteResponse
 * @param id
 * @param nom
 * @param specialites
 */
public record GroupeSpecialiteResponse(Long id, String nom, List<SpecialiteResponse> specialites) {

    public static GroupeSpecialiteResponse from(GroupeAvecSpecialites groupeAvecSpecialites) {
        return new GroupeSpecialiteResponse(
                groupeAvecSpecialites.groupe().getId(),
                groupeAvecSpecialites.groupe().getNom(),
                groupeAvecSpecialites.specialites().stream()
                        .map(SpecialiteResponse::from)
                        .toList()
        );
    }
}