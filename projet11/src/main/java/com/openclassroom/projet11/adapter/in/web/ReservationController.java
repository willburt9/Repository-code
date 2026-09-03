package com.openclassroom.projet11.adapter.in.web;

import com.openclassroom.projet11.adapter.in.web.dto.ReservationRequest;
import com.openclassroom.projet11.adapter.in.web.dto.ReservationResponse;
import com.openclassroom.projet11.adapter.out.logging.AuditLog;
import com.openclassroom.projet11.application.port.in.ReservationResultat;
import com.openclassroom.projet11.application.port.in.ReserverLitUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * POST /reservations — confirme la réservation d'un lit dans l'hôpital
 * recommandé (F3, Définition de l'architecture §9.c, étape 5).
 */
@RestController
public class ReservationController {

    private final ReserverLitUseCase reserverLitUseCase;

    public ReservationController(ReserverLitUseCase reserverLitUseCase) {
        this.reserverLitUseCase = reserverLitUseCase;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> reserver(@Valid @RequestBody ReservationRequest requete) {
        AuditLog.LOGGER.info("event=reservation.demande hopitalId={}", requete.hopitalId());
        
        ReservationResultat resultat = reserverLitUseCase.reserver(requete.hopitalId());

         AuditLog.LOGGER.info("event=reservation.resultat hopitalId={} referencePatient={} litsDisponiblesRestants={}",
                resultat.reservation().hopitalId(),
                resultat.reservation().referencePatientAnonymisee(),
                resultat.litsDisponiblesRestants());
                
        return ResponseEntity.status(HttpStatus.CREATED).body(ReservationResponse.from(resultat));
    }
}