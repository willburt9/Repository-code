package com.openclassroom.projet11.application.usecase;

import com.openclassroom.projet11.application.port.in.ReservationResultat;
import com.openclassroom.projet11.application.port.out.HopitalRepositoryPort;
import com.openclassroom.projet11.domain.exception.NoAvailableBedException;
import com.openclassroom.projet11.domain.model.GroupeSpecialite;
import com.openclassroom.projet11.domain.model.Hopital;
import com.openclassroom.projet11.domain.model.Location;
import com.openclassroom.projet11.domain.model.Specialite;
import com.openclassroom.projet11.domain.port.out.EventPublisherPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReserverLitServiceTest {

    @Mock private HopitalRepositoryPort hopitalRepository;
    @Mock private EventPublisherPort eventPublisher;

    private ReserverLitService service;

    @BeforeEach
    void setUp() {
        service = new ReserverLitService(hopitalRepository, eventPublisher);
    }

    @Test
    void decremente_les_lits_et_publie_levenement() {
        Hopital hopital = unHopitalAvecLits(9);
        when(hopitalRepository.findById(6L)).thenReturn(hopital);

        ReservationResultat resultat = service.reserver(6L);

        assertThat(resultat.litsDisponiblesRestants()).isEqualTo(8);
        assertThat(resultat.reservation().hopitalId()).isEqualTo(6L);
        assertThat(resultat.reservation().referencePatientAnonymisee()).startsWith("PAT-");

        verify(hopitalRepository).mettreAJourLitsDisponibles(6L, 8);
        verify(eventPublisher).publier(resultat.reservation());
    }

    @Test
    void leve_noAvailableBedException_si_hopital_complet() {
        Hopital hopitalComplet = unHopitalAvecLits(0);
        when(hopitalRepository.findById(1L)).thenReturn(hopitalComplet);

        assertThatThrownBy(() -> service.reserver(1L))
                .isInstanceOf(NoAvailableBedException.class);

        // Aucun effet de bord si la réservation échoue : ni persistance, ni événement.
        verify(hopitalRepository, never()).mettreAJourLitsDisponibles(anyLong(), anyInt());
        verify(eventPublisher, never()).publier(any());
    }

    private Hopital unHopitalAvecLits(int litsDisponibles) {
        GroupeSpecialite groupe = new GroupeSpecialite(5L, "Groupe de médecine générale");
        Specialite cardiologie = new Specialite(121L, "Cardiologie", groupe);
        return new Hopital(6L, "Hôpital Test", "Adresse test",
                new Location(48.85, 2.35), List.of(cardiologie), litsDisponibles);
    }
}