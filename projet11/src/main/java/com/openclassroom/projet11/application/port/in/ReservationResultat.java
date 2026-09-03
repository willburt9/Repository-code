package com.openclassroom.projet11.application.port.in;

import com.openclassroom.projet11.domain.model.ReservationLit;

public record ReservationResultat(ReservationLit reservation, int litsDisponiblesRestants) {
}