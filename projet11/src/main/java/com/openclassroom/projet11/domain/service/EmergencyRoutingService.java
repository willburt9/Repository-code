package com.openclassroom.projet11.domain.service;

import com.openclassroom.projet11.domain.exception.HopitalNotFoundException;
import com.openclassroom.projet11.domain.model.Hopital;
import com.openclassroom.projet11.domain.model.Location;
import com.openclassroom.projet11.domain.model.Specialite;
import com.openclassroom.projet11.domain.port.out.DistanceProviderPort;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Service métier responsable de recommander
 * l'établissement hospitalier le plus adapté
 * à la prise en charge d'un patient.
 *
 * <p>
 * Les critères de sélection sont les suivants :
 * </p>
 * <ol>
 *     <li>L'hôpital prend en charge la spécialité demandée.</li>
 *     <li>L'hôpital dispose d'au moins un lit libre.</li>
 *     <li>L'hôpital est le plus proche du patient.</li>
 * </ol>
 */
public final class EmergencyRoutingService {

    private final DistanceProviderPort distanceProvider;

    /**
     * Initialise le service de routage.
     *
     * @param distanceProvider fournisseur de calcul de distance
     */
    public EmergencyRoutingService(DistanceProviderPort distanceProvider) {

        this.distanceProvider = Objects.requireNonNull(
                distanceProvider,
                "Le fournisseur de distance est obligatoire.");
    }

    /**
     * Recherche l'établissement hospitalier le plus pertinent.
     *
     * @param hopitaux établissements disponibles
     * @param specialite spécialité recherchée
     * @param localisationPatient localisation du patient
     * @return hôpital recommandé
     * @throws HopitalNotFoundException si aucun établissement ne répond
     *                                  aux critères métier
     */
    public Hopital recommanderHopital(
            List<Hopital> hopitaux,
            Specialite specialite,
            Location localisationPatient) {

        Objects.requireNonNull(hopitaux,
                "La liste des hôpitaux est obligatoire.");

        Objects.requireNonNull(specialite,
                "La spécialité est obligatoire.");

        Objects.requireNonNull(localisationPatient,
                "La localisation du patient est obligatoire.");

        return hopitaux.stream()

                .filter(hopital -> hopital.peutPrendreEnCharge(specialite))

                .min(Comparator.comparingDouble(hopital ->
                        distanceProvider.calculerDistance(
                                localisationPatient,
                                hopital.getLocalisation())))

                .orElseThrow(() ->
                        new HopitalNotFoundException(
                                specialite.toString()));
    }
}