package com.openclassroom.projet11.adapter.in.web;

import com.openclassroom.projet11.adapter.in.web.dto.RecommandationResponse;
import com.openclassroom.projet11.application.port.in.RecommandationResultat;
import com.openclassroom.projet11.application.port.in.RecommanderHopitalUseCase;
import com.openclassroom.projet11.domain.model.Location;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller REST pour gérer les recommandations d'hôpitaux.
 * RecommandationController
 * @param recommanderHopitalUseCase le cas d'utilisation pour recommander un hôpital
 */
@RestController
public class RecommandationController {

    private final RecommanderHopitalUseCase recommanderHopitalUseCase;

    public RecommandationController(RecommanderHopitalUseCase recommanderHopitalUseCase) {
        this.recommanderHopitalUseCase = recommanderHopitalUseCase;
    }

    /**
     * Endpoint pour recommander un hôpital en fonction de la localisation du patient et de la spécialité recherchée.
     * 400 si coordonnées invalides
     * @param latitude
     * @param longitude
     * @param specialiteId
     * @return
     */
    @GetMapping("/recommandations")
    public RecommandationResponse recommander(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam Long specialiteId) {

        Location localisationPatient = new Location(latitude, longitude);   // 400 si coordonnées invalides
        RecommandationResultat resultat = recommanderHopitalUseCase.recommander(localisationPatient, specialiteId);
        return RecommandationResponse.from(resultat);
    }
}