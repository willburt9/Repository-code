package com.openclassroom.projet11.domain.port.out;

import com.openclassroom.projet11.domain.model.Location;
/**
 * Port sortant de calcul de distance et de temps de trajet.
 *
 * Contrat impératif (Cahier des charges, Plan de test BDD FT-01) : la distance
 * retournée doit toujours être une distance routière réelle simulée, JAMAIS une
 * distance à vol d'oiseau.
 */
public interface DistanceProviderPort {
    /**
     * Calcule la distance routière entre deux localisations.
     *
     * @param localisationPatient localisation du patient
     * @param localisationHopital localisation de l'hôpital
     * @return distance en kilomètres
     */
    double calculerDistance(Location  localisationPatient, Location  localisationHopital);

    /**
     * Calcule le temps de trajet routier entre deux localisations.
     *
     * @param localisationPatient localisation du patient
     * @param localisationHopital localisation de l'hôpital
     * @return temps de trajet en minutes
     */
    double calculerTempsTrajet(Location  localisationPatient, Location  localisationHopital);
}