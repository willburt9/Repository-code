package com.openclassroom.projet11.application.usecase;

import com.openclassroom.projet11.application.port.out.HopitalRepositoryPort;
import com.openclassroom.projet11.domain.model.GroupeSpecialite;
import com.openclassroom.projet11.domain.model.Hopital;
import com.openclassroom.projet11.domain.model.Location;
import com.openclassroom.projet11.domain.model.Specialite;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires de {@link ListerHopitauxService}.
 */
@ExtendWith(MockitoExtension.class)
class ListerHopitauxServiceTest {

    @Mock
    private HopitalRepositoryPort hopitalRepository;

    @InjectMocks
    private ListerHopitauxService service;

    @Test
    void listerTous_devraitRetournerLaListeDuRepository() {
        GroupeSpecialite groupe = new GroupeSpecialite(10L, "Médecine");
        Specialite cardiologie = new Specialite(100L, "Cardiologie", groupe);
        Hopital hopital = new Hopital(1L, "Hôpital Cochin", "1 Rue de la Santé",
                new Location(48.8566, 2.3522), List.of(cardiologie), 10);

        when(hopitalRepository.findAll()).thenReturn(List.of(hopital));

        List<Hopital> resultat = service.listerTous();

        assertEquals(1, resultat.size());
        assertSame(hopital, resultat.get(0));
        verify(hopitalRepository).findAll();
    }

    @Test
    void listerTous_devraitRetournerListeVide_quandAucunHopital() {
        when(hopitalRepository.findAll()).thenReturn(Collections.emptyList());

        List<Hopital> resultat = service.listerTous();

        assertTrue(resultat.isEmpty());
    }

    @Test
    void constructeur_devraitLeverException_quandRepositoryNull() {
        assertThrows(NullPointerException.class, () -> new ListerHopitauxService(null));
    }
}