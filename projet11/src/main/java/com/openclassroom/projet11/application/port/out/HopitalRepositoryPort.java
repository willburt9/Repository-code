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

    /**
     * @param id identifiant unique de l'hôpital
     * @return l'hôpital correspondant à l'identifiant, ou null si aucun hôpital n'est trouvé
     */
    Hopital findById(Long id);
        
    /**
     * Met à jour le nombre de lits disponibles pour un hôpital donné.
     *
     * @param hopitalId identifiant unique de l'hôpital
     * @param nouveauNombreDeLits nouveau nombre de lits disponibles
     */
    void mettreAJourLitsDisponibles(Long hopitalId, int nouveauNombreDeLits);
      
}