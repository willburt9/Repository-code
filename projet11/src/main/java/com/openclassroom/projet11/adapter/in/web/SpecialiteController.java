package com.openclassroom.projet11.adapter.in.web;

import com.openclassroom.projet11.adapter.in.web.dto.GroupeSpecialiteResponse;
import com.openclassroom.projet11.application.port.in.ListerSpecialitesUseCase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller REST pour gérer les opérations liées aux spécialités.
 * SpecialiteController
 */
@RestController
public class SpecialiteController {

    private final ListerSpecialitesUseCase listerSpecialitesUseCase;

    public SpecialiteController(ListerSpecialitesUseCase listerSpecialitesUseCase) {
        this.listerSpecialitesUseCase = listerSpecialitesUseCase;
    }

    /**
     * Endpoint pour lister tous les groupes de spécialités.
     *
     * @return liste des groupes de spécialités sous forme de GroupeSpecialiteResponse
     */
    @GetMapping("/specialites")
    public List<GroupeSpecialiteResponse> lister() {
        return listerSpecialitesUseCase.listerGroupes().stream()
                .map(GroupeSpecialiteResponse::from)
                .toList();
    }
}