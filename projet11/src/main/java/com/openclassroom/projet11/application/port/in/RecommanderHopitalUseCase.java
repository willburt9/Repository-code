package com.openclassroom.projet11.application.port.in;

import com.openclassroom.projet11.domain.model.Location;

public interface RecommanderHopitalUseCase {
    RecommandationResultat recommander(Location localisationPatient, Long specialiteId);
}