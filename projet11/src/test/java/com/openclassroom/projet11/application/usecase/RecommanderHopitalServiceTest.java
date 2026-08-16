package com.openclassroom.projet11.application.usecase;

import com.openclassroom.projet11.application.port.in.RecommandationResultat;
import com.openclassroom.projet11.application.port.out.HopitalRepositoryPort;
import com.openclassroom.projet11.application.port.out.SpecialiteRepositoryPort;
import com.openclassroom.projet11.domain.model.GroupeSpecialite;
import com.openclassroom.projet11.domain.model.Hopital;
import com.openclassroom.projet11.domain.model.Location;
import com.openclassroom.projet11.domain.model.Specialite;
import com.openclassroom.projet11.domain.port.out.DistanceProviderPort;
import com.openclassroom.projet11.domain.service.EmergencyRoutingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * RecommanderHopitalServiceTest
 * Teste l'orchestration du use case : repositories et distance sont simulés.
 */
@ExtendWith(MockitoExtension.class)
class RecommanderHopitalServiceTest {

    @Mock private HopitalRepositoryPort hopitalRepository;
    @Mock private SpecialiteRepositoryPort specialiteRepository;
    @Mock private DistanceProviderPort distanceProvider;

    private RecommanderHopitalService service;

    private Specialite cardiologie;
    private Hopital hopital;
    private Location localisationPatient;

    @BeforeEach
    void setUp() {
        EmergencyRoutingService emergencyRoutingService = new EmergencyRoutingService(distanceProvider);
        service = new RecommanderHopitalService(
                hopitalRepository, specialiteRepository, emergencyRoutingService, distanceProvider);

        GroupeSpecialite groupe = new GroupeSpecialite(5L, "Groupe de médecine générale");
        cardiologie = new Specialite(121L, "Cardiologie", groupe);
        hopital = new Hopital(1L, "Hôpital Test", "1 rue de Test",
                new Location(48.85, 2.35), List.of(cardiologie), 3);
        localisationPatient = new Location(48.8383, 2.3651);
    }

    @Test
    void assemble_le_resultat_avec_distance_et_duree() {
        when(specialiteRepository.findById(121L)).thenReturn(cardiologie);
        when(hopitalRepository.findAll()).thenReturn(List.of(hopital));
        when(distanceProvider.calculerDistance(any(), any())).thenReturn(2.5);
        when(distanceProvider.calculerTempsTrajet(any(), any())).thenReturn(6.0);

        RecommandationResultat resultat = service.recommander(localisationPatient, 121L);

        assertThat(resultat.hopital()).isEqualTo(hopital);
        assertThat(resultat.distanceKm()).isEqualTo(2.5);
        assertThat(resultat.dureeMinutes()).isEqualTo(6.0);
    }
}