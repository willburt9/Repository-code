package com.openclassroom.projet11.application.port.in;

import com.openclassroom.projet11.domain.model.Hopital;

public record RecommandationResultat(Hopital hopital, double distanceKm, double dureeMinutes) {
}