package com.openclassroom.projet11.domain.service;

import com.openclassroom.projet11.domain.exception.HopitalNotFoundException;
import com.openclassroom.projet11.domain.model.GroupeSpecialite;
import com.openclassroom.projet11.domain.model.Hopital;
import com.openclassroom.projet11.domain.model.Location;
import com.openclassroom.projet11.domain.model.Specialite;
import com.openclassroom.projet11.domain.port.out.DistanceProviderPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

/**
 * Tests unitaires du cœur métier de l'application : la logique de recommandation d'hôpital.
 */
@ExtendWith(MockitoExtension.class)
class EmergencyRoutingServiceTest {

    @Mock
    private DistanceProviderPort distanceProvider;

    private EmergencyRoutingService service;

    private GroupeSpecialite groupeMedecineGenerale;
    private Specialite cardiologie;
    private Location localisationPatient;

    @BeforeEach
    void setUp() {
        service = new EmergencyRoutingService(distanceProvider);
        groupeMedecineGenerale = new GroupeSpecialite(5L, "Groupe de médecine générale");
        cardiologie = new Specialite(121L, "Cardiologie", groupeMedecineGenerale);
        localisationPatient = new Location(48.8383, 2.3651);
    }

    /** 
     * Scénario 1 : parmi les hôpitaux éligibles, le service doit recommander l'hôpital le plus proche du patient.
     */
    @Test
    void recommande_lhopital_le_plus_proche_parmi_ceux_eligibles() {
       
        Hopital hopitalProche = unHopital(1L, "Hôpital Proche", new Location(48.8390, 2.3660), 2, cardiologie);
        Hopital hopitalLoin = unHopital(2L, "Hôpital Loin", new Location(48.9000, 2.4200), 5, cardiologie);

        when(distanceProvider.calculerDistance(localisationPatient, hopitalProche.getLocalisation()))
                .thenReturn(1.2);
        when(distanceProvider.calculerDistance(localisationPatient, hopitalLoin.getLocalisation()))
                .thenReturn(8.7);

        Hopital resultat = service.recommanderHopital(
                List.of(hopitalLoin, hopitalProche), cardiologie, localisationPatient);

        assertThat(resultat).isEqualTo(hopitalProche);
    }

    /**
     * Scénario 2 : l'hôpital le plus proche n'a pas de lits disponibles, donc le service doit recommander l'hôpital suivant le plus proche qui a des lits disponibles.
     */
    @Test
    void exclut_un_hopital_plus_proche_mais_sans_lit_disponible() {
       
        Hopital sansLit = unHopital(1L, "Hôpital Sans Lit", new Location(48.8390, 2.3660), 0, cardiologie);
        Hopital avecLit = unHopital(2L, "Hôpital Avec Lit", new Location(48.9000, 2.4200), 2, cardiologie);

        Hopital resultat = service.recommanderHopital(
                List.of(sansLit, avecLit), cardiologie, localisationPatient);

        assertThat(resultat).isEqualTo(avecLit);
    }

    /**
     * Scénario 3 : aucun hôpital n'est éligible pour la spécialité demandée, donc le service doit lever une exception.
     */
    @Test
    void leve_hopitalNotFoundException_si_aucun_hopital_eligible() {
        Specialite pediatrie = new Specialite(200L, "Pédiatrie", groupeMedecineGenerale);
        Hopital sansLaSpecialite = unHopital(1L, "Autre Hôpital", new Location(48.85, 2.35), 3, pediatrie);

        assertThatThrownBy(() -> service.recommanderHopital(
                List.of(sansLaSpecialite), cardiologie, localisationPatient))
                .isInstanceOf(HopitalNotFoundException.class);
    }

    private Hopital unHopital(Long id, String nom, Location localisation, int litsDisponibles, Specialite... specialites) {
        return new Hopital(id, nom, "1 rue de Test, Paris",
                localisation, List.of(specialites), litsDisponibles);
    }
}