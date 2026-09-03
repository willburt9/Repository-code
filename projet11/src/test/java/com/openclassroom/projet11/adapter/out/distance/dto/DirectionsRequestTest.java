package com.openclassroom.projet11.adapter.out.distance.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DirectionsRequestTest {

    @Test
    void de_formate_le_point_de_depart_en_latitude_virgule_longitude() {
        DirectionsRequest requete = DirectionsRequest.de(2.35, 48.85, 4.83, 45.76);

        assertThat(requete.getPointDepart()).isEqualTo("48.85,2.35");
    }

    @Test
    void de_formate_le_point_darrivee_en_latitude_virgule_longitude() {
        DirectionsRequest requete = DirectionsRequest.de(2.35, 48.85, 4.83, 45.76);

        assertThat(requete.getPointArrivee()).isEqualTo("45.76,4.83");
    }

    @Test
    void de_fixe_le_profil_sur_car() {
        DirectionsRequest requete = DirectionsRequest.de(2.35, 48.85, 4.83, 45.76);

        assertThat(requete.getProfile()).isEqualTo("car");
    }

    @Test
    void de_fixe_la_locale_sur_fr() {
        DirectionsRequest requete = DirectionsRequest.de(2.35, 48.85, 4.83, 45.76);

        assertThat(requete.getLocale()).isEqualTo("fr");
    }

    @Test
    void de_gere_les_coordonnees_negatives_sans_les_alterer() {
        DirectionsRequest requete = DirectionsRequest.de(-73.99, 40.73, -0.13, 51.51);

        assertThat(requete.getPointDepart()).isEqualTo("40.73,-73.99");
        assertThat(requete.getPointArrivee()).isEqualTo("51.51,-0.13");
    }

    @Test
    void de_ne_confond_pas_depart_et_arrivee_quand_les_valeurs_sont_proches() {
        DirectionsRequest requete = DirectionsRequest.de(1.0, 2.0, 3.0, 4.0);

        assertThat(requete.getPointDepart()).isEqualTo("2.0,1.0");
        assertThat(requete.getPointArrivee()).isEqualTo("4.0,3.0");
    }
}