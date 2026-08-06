package com.openclassroom.projet11.adapter.in.web.dto;

import com.openclassroom.projet11.application.port.in.ReservationResultat;

import java.time.Instant;

public record ReservationResponse(
        Long hopitalId,
        String referencePatient,
        Instant horodatage,
        int litsDisponiblesRestants
) {

    public static ReservationResponse from(ReservationResultat resultat) {
        return new ReservationResponse(
                resultat.reservation().hopitalId(),
                resultat.reservation().referencePatientAnonymisee(),
                resultat.reservation().horodatage(),
                resultat.litsDisponiblesRestants()
        );
    }
}