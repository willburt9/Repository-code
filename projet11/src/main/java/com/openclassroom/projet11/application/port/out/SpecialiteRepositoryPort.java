package com.openclassroom.projet11.application.port.out;

import com.openclassroom.projet11.domain.exception.SpecialiteNotFoundException;
import com.openclassroom.projet11.domain.model.Specialite;

import java.util.List;

/**
 * Port de sortie pour l'accès aux données des spécialités.
 * SpecialiteRepositoryPort
 */
public interface SpecialiteRepositoryPort {

    /**
     * @return la liste de toutes les spécialités
     */
    List<Specialite> findAll();

    /**
     * @param id l'identifiant de la spécialité à rechercher
     * @return la spécialité correspondante
     * @throws SpecialiteNotFoundException si aucune spécialité ne correspond à l'id fourni
     */
    Specialite findById(Long id);
}