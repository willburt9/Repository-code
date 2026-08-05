package com.openclassroom.projet11.application.port.in;

import java.util.List;

/**
 * Port d'entrée pour lister les spécialités.
 * ListerSpecialitesUseCase
 */
public interface ListerSpecialitesUseCase {

    /**
     * @return la liste de tous les groupes avec leurs spécialités
     */
    List<GroupeAvecSpecialites> listerGroupes();
}