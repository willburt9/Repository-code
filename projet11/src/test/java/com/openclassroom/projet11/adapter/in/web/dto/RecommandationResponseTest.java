package com.openclassroom.projet11.adapter.in.web.dto;

import com.openclassroom.projet11.application.port.in.RecommandationResultat;
import com.openclassroom.projet11.domain.model.GroupeSpecialite;
import com.openclassroom.projet11.domain.model.Hopital;
import com.openclassroom.projet11.domain.model.Location;
import com.openclassroom.projet11.domain.model.Specialite;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RecommandationResponseTest {

    private Hopital unHopital() {
        GroupeSpecialite groupe = new GroupeSpecialite(1L, "Chirurgie");
        Specialite specialite = new Specialite(1L, "Neurochirurgie", groupe);
        return new Hopital(
                1L,
                "Hôpital Cochin",
                "12 rue de la Santé",
                new Location(48.85, 2.35),
                List.of(specialite),
                2
        );
    }

    @Test
    void from_mappe_tous_les_champs_de_lhopital() {
        Hopital hopital = unHopital();
        RecommandationResultat resultat = new RecommandationResultat(hopital, 3.4, 6.7);

        RecommandationResponse response = RecommandationResponse.from(resultat);

        assertThat(response.hopitalId()).isEqualTo(hopital.getId());
        assertThat(response.nom()).isEqualTo(hopital.getNom());
        assertThat(response.adresse()).isEqualTo(hopital.getAdresse());
        assertThat(response.latitude()).isEqualTo(hopital.getLocalisation().latitude());
        assertThat(response.longitude()).isEqualTo(hopital.getLocalisation().longitude());
        assertThat(response.litsDisponibles()).isEqualTo(hopital.getLitsDisponibles());
    }

    @Test
    void from_arrondit_la_distance_a_deux_decimales() {
        RecommandationResultat resultat = new RecommandationResultat(unHopital(), 12.344, 0.0);

        RecommandationResponse response = RecommandationResponse.from(resultat);

        assertThat(response.distanceKm()).isEqualTo(12.34);
    }

    @Test
    void from_arrondit_la_duree_a_deux_decimales() {
        RecommandationResultat resultat = new RecommandationResultat(unHopital(), 0.0, 7.876);

        RecommandationResponse response = RecommandationResponse.from(resultat);

        assertThat(response.dureeMinutes()).isEqualTo(7.88);
    }

    @Test
    void from_arrondit_correctement_a_la_valeur_superieure_sur_un_cas_pile() {
        RecommandationResultat resultat = new RecommandationResultat(unHopital(), 5.005, 0.0);

        RecommandationResponse response = RecommandationResponse.from(resultat);

        assertThat(response.distanceKm()).isEqualTo(5.01);
    }

    @Test
    void from_gere_une_distance_et_une_duree_nulles() {
        RecommandationResultat resultat = new RecommandationResultat(unHopital(), 0.0, 0.0);

        RecommandationResponse response = RecommandationResponse.from(resultat);

        assertThat(response.distanceKm()).isZero();
        assertThat(response.dureeMinutes()).isZero();
    }
}