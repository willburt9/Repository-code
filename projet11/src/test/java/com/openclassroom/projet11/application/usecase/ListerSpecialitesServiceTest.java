package com.openclassroom.projet11.application.usecase;

import com.openclassroom.projet11.application.port.in.GroupeAvecSpecialites;
import com.openclassroom.projet11.application.port.out.SpecialiteRepositoryPort;
import com.openclassroom.projet11.domain.model.GroupeSpecialite;
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
 * Tests unitaires de {@link ListerSpecialitesService}.
 */
@ExtendWith(MockitoExtension.class)
class ListerSpecialitesServiceTest {

    @Mock
    private SpecialiteRepositoryPort specialiteRepository;

    @InjectMocks
    private ListerSpecialitesService service;

    private final GroupeSpecialite groupeMedecine = new GroupeSpecialite(10L, "Médecine");
    private final GroupeSpecialite groupeChirurgie = new GroupeSpecialite(11L, "Chirurgie");

    @Test
    void listerGroupes_devraitRegrouperLesSpecialitesParGroupe() {
        Specialite cardiologie = new Specialite(100L, "Cardiologie", groupeMedecine);
        Specialite pneumologie = new Specialite(101L, "Pneumologie", groupeMedecine);
        Specialite chirurgieGenerale = new Specialite(102L, "Chirurgie générale", groupeChirurgie);

        when(specialiteRepository.findAll())
                .thenReturn(List.of(cardiologie, pneumologie, chirurgieGenerale));

        List<GroupeAvecSpecialites> resultat = service.listerGroupes();

        assertEquals(2, resultat.size());

        GroupeAvecSpecialites premierGroupe = resultat.get(0);
        assertEquals(groupeMedecine, premierGroupe.groupe());
        assertEquals(List.of(cardiologie, pneumologie), premierGroupe.specialites());

        GroupeAvecSpecialites secondGroupe = resultat.get(1);
        assertEquals(groupeChirurgie, secondGroupe.groupe());
        assertEquals(List.of(chirurgieGenerale), secondGroupe.specialites());
    }

    @Test
    void listerGroupes_devraitPreserverLOrdreDApparitionDesGroupes() {
        Specialite chirurgieGenerale = new Specialite(102L, "Chirurgie générale", groupeChirurgie);
        Specialite cardiologie = new Specialite(100L, "Cardiologie", groupeMedecine);

        // Chirurgie apparaît en premier dans les données source
        when(specialiteRepository.findAll())
                .thenReturn(List.of(chirurgieGenerale, cardiologie));

        List<GroupeAvecSpecialites> resultat = service.listerGroupes();

        assertEquals(groupeChirurgie, resultat.get(0).groupe());
        assertEquals(groupeMedecine, resultat.get(1).groupe());
    }

    @Test
    void listerGroupes_devraitRetournerListeVide_quandAucuneSpecialite() {
        when(specialiteRepository.findAll()).thenReturn(Collections.emptyList());

        List<GroupeAvecSpecialites> resultat = service.listerGroupes();

        assertTrue(resultat.isEmpty());
    }

    @Test
    void constructeur_devraitLeverException_quandRepositoryNull() {
        assertThrows(NullPointerException.class, () -> new ListerSpecialitesService(null));
    }
}