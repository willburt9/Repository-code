package com.openclassroom.projet11.application.usecase;

import com.openclassroom.projet11.application.port.in.RecommandationResultat;
import com.openclassroom.projet11.application.port.in.RecommanderHopitalUseCase;
import com.openclassroom.projet11.application.port.out.HopitalRepositoryPort;
import com.openclassroom.projet11.application.port.out.SpecialiteRepositoryPort;
import com.openclassroom.projet11.domain.model.Hopital;
import com.openclassroom.projet11.domain.model.Location;
import com.openclassroom.projet11.domain.model.Specialite;
import com.openclassroom.projet11.domain.port.out.DistanceProviderPort;
import com.openclassroom.projet11.domain.service.EmergencyRoutingService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * Service pour recommander un hôpital en fonction de la localisation du patient et de la spécialité recherchée.
 * @param hopitalRepository le port de sortie pour accéder aux données des hôpitaux
 * @param specialiteRepository le port de sortie pour accéder aux données des spécialités
 * @param emergencyRoutingService le service pour déterminer l'hôpital le plus approprié
 * @param distanceProvider le port de sortie pour calculer la distance et le temps de trajet
 * RecommanderHopitalService
 */
@Service
public class RecommanderHopitalService implements RecommanderHopitalUseCase {

    private final HopitalRepositoryPort hopitalRepository;
    private final SpecialiteRepositoryPort specialiteRepository;
    private final EmergencyRoutingService emergencyRoutingService;
    private final DistanceProviderPort distanceProvider;

    public RecommanderHopitalService(HopitalRepositoryPort hopitalRepository,
                                      SpecialiteRepositoryPort specialiteRepository,
                                      EmergencyRoutingService emergencyRoutingService,
                                      DistanceProviderPort distanceProvider) {
        this.hopitalRepository = Objects.requireNonNull(hopitalRepository);
        this.specialiteRepository = Objects.requireNonNull(specialiteRepository);
        this.emergencyRoutingService = Objects.requireNonNull(emergencyRoutingService);
        this.distanceProvider = Objects.requireNonNull(distanceProvider);
    }

    /**
     * Recommande un hôpital pour un patient en fonction de sa localisation et de la spécialité recherchée.
     * @param localisationPatient la localisation du patient
     * @param specialiteId l'identifiant de la spécialité recherchée    
     * @return le résultat de la recommandation, incluant l'hôpital recommandé, la distance et la durée du trajet     
     * */
    @Override
    public RecommandationResultat recommander(Location localisationPatient, Long specialiteId) {
        Specialite specialite = specialiteRepository.findById(specialiteId);   // 404 si id inconnu
        List<Hopital> hopitaux = hopitalRepository.findAll();

        Hopital hopitalRecommande = emergencyRoutingService.recommanderHopital(
                hopitaux, specialite, localisationPatient);                    // 404 si aucun hôpital ne convient

        double distanceKm = distanceProvider.calculerDistance(
                localisationPatient, hopitalRecommande.getLocalisation());
        double dureeMinutes = distanceProvider.calculerTempsTrajet(
                localisationPatient, hopitalRecommande.getLocalisation());

        return new RecommandationResultat(hopitalRecommande, distanceKm, dureeMinutes);
    }
}