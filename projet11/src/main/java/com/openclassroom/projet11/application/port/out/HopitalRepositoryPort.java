package com.openclassroom.projet11.application.port.out;

import com.openclassroom.projet11.domain.model.Hopital;

import java.util.List;

/**
 * Port de sortie pour l'accès aux données des hôpitaux.
 * HopitalRepositoryPort
 */
public interface HopitalRepositoryPort {
    /**
     * @return la liste de tous les hôpitaux
     */
    List<Hopital> findAll();
}