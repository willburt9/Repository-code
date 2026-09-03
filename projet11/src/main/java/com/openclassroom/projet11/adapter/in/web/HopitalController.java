package com.openclassroom.projet11.adapter.in.web;

import com.openclassroom.projet11.adapter.in.web.dto.HopitalResponse;
import com.openclassroom.projet11.application.port.in.ListerHopitauxUseCase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller REST pour gérer les opérations liées aux hôpitaux.
 * HopitalController
 */
@RestController
public class HopitalController {

    private final ListerHopitauxUseCase listerHopitauxUseCase;

    public HopitalController(ListerHopitauxUseCase listerHopitauxUseCase) {
        this.listerHopitauxUseCase = listerHopitauxUseCase;
    }

    /**
     * Endpoint pour lister tous les hôpitaux.
     *
     * @return liste des hôpitaux sous forme de HopitalResponse
     */
    @GetMapping("/hopitaux")
    public List<HopitalResponse> lister() {
        return listerHopitauxUseCase.listerTous().stream()
                .map(HopitalResponse::from)
                .toList();
    }
}