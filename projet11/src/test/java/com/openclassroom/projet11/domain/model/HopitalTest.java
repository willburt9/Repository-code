package com.openclassroom.projet11.domain.model;

import com.openclassroom.projet11.domain.exception.NoAvailableBedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * HopitalTest
 * Tests unitaires pour la classe Hopital. 
 */
class HopitalTest {

    private final GroupeSpecialite groupe = new GroupeSpecialite(5L, "Groupe de médecine générale");
    private final Specialite cardiologie = new Specialite(121L, "Cardiologie", groupe);
    private final Specialite pediatrie = new Specialite(147L, "Pédiatrie", groupe);
    private final Location localisation = new Location(48.85, 2.35);

    // --- Construction et validation ---------------------------------------

    @Test
    void construit_un_hopital_valide_et_trim_le_nom_et_ladresse() {
        Hopital hopital = new Hopital(1L, "  Hôpital Test  ", "  1 rue de Test  ",
                localisation, List.of(cardiologie), 5);

        assertThat(hopital.getNom()).isEqualTo("Hôpital Test");
        assertThat(hopital.getAdresse()).isEqualTo("1 rue de Test");
        assertThat(hopital.getLitsDisponibles()).isEqualTo(5);
    }

    @Test
    void rejette_un_id_null() {
        assertThatThrownBy(() -> new Hopital(null, "Nom", "Adresse", localisation, List.of(cardiologie), 1))
                .isInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void rejette_un_nom_vide_ou_blanc(String nomInvalide) {
        assertThatThrownBy(() -> new Hopital(1L, nomInvalide, "Adresse", localisation, List.of(cardiologie), 1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void rejette_une_adresse_vide_ou_blanche(String adresseInvalide) {
        assertThatThrownBy(() -> new Hopital(1L, "Nom", adresseInvalide, localisation, List.of(cardiologie), 1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejette_une_localisation_null() {
        assertThatThrownBy(() -> new Hopital(1L, "Nom", "Adresse", null, List.of(cardiologie), 1))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void rejette_une_liste_de_specialites_vide() {
        assertThatThrownBy(() -> new Hopital(1L, "Nom", "Adresse", localisation, List.of(), 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("au moins une spécialité");
    }

    @Test
    void rejette_un_nombre_de_lits_negatif() {
        assertThatThrownBy(() -> new Hopital(1L, "Nom", "Adresse", localisation, List.of(cardiologie), -1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void la_liste_de_specialites_est_copiee_defensivement() {
        // Un Hopital est censé être immuable : modifier la liste passée au
        // constructeur APRÈS coup ne doit pas affecter l'état interne.
        List<Specialite> specialitesModifiable = new ArrayList<>(List.of(cardiologie));
        Hopital hopital = new Hopital(1L, "Nom", "Adresse", localisation, specialitesModifiable, 1);

        specialitesModifiable.add(pediatrie);

        assertThat(hopital.getSpecialites()).containsExactly(cardiologie);
    }

    @Test
    void getSpecialites_retourne_une_liste_non_modifiable() {
        Hopital hopital = new Hopital(1L, "Nom", "Adresse", localisation, List.of(cardiologie), 1);

        assertThatThrownBy(() -> hopital.getSpecialites().add(pediatrie))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    // --- Règles métier -------------------------------------------------

    @Test
    void possedeLaSpecialite_renvoie_vrai_si_presente() {
        Hopital hopital = new Hopital(1L, "Nom", "Adresse", localisation, List.of(cardiologie), 1);

        assertThat(hopital.possedeLaSpecialite(cardiologie)).isTrue();
        assertThat(hopital.possedeLaSpecialite(pediatrie)).isFalse();
    }

    @Test
    void disposeDeLits_selon_le_nombre_de_lits() {
        Hopital avecLits = new Hopital(1L, "Nom", "Adresse", localisation, List.of(cardiologie), 3);
        Hopital sansLit = new Hopital(2L, "Nom", "Adresse", localisation, List.of(cardiologie), 0);

        assertThat(avecLits.disposeDeLits()).isTrue();
        assertThat(sansLit.disposeDeLits()).isFalse();
    }

    @Test
    void peutPrendreEnCharge_exige_a_la_fois_la_specialite_et_un_lit() {
        Hopital bonneSpecialiteMaisComplet =
                new Hopital(1L, "Nom", "Adresse", localisation, List.of(cardiologie), 0);
        Hopital bonneSpecialiteEtDisponible =
                new Hopital(2L, "Nom", "Adresse", localisation, List.of(cardiologie), 1);
        Hopital mauvaiseSpecialite =
                new Hopital(3L, "Nom", "Adresse", localisation, List.of(pediatrie), 5);

        assertThat(bonneSpecialiteMaisComplet.peutPrendreEnCharge(cardiologie)).isFalse();
        assertThat(bonneSpecialiteEtDisponible.peutPrendreEnCharge(cardiologie)).isTrue();
        assertThat(mauvaiseSpecialite.peutPrendreEnCharge(cardiologie)).isFalse();
    }

    @Test
    void reserverLit_decremente_le_nombre_de_lits_disponibles() {
        Hopital hopital = new Hopital(1L, "Nom", "Adresse", localisation, List.of(cardiologie), 2);

        hopital.reserverLit();

        assertThat(hopital.getLitsDisponibles()).isEqualTo(1);
    }

    @Test
    void reserverLit_leve_noAvailableBedException_si_complet() {
        Hopital hopitalComplet = new Hopital(1L, "Nom", "Adresse", localisation, List.of(cardiologie), 0);

        assertThatThrownBy(hopitalComplet::reserverLit)
                .isInstanceOf(NoAvailableBedException.class);

        // Aucune décrémentation ne doit avoir eu lieu suite à l'échec.
        assertThat(hopitalComplet.getLitsDisponibles()).isZero();
    }

    // --- Identité --------------------------------------------------------

    @Test
    void deux_hopitaux_avec_le_meme_id_sont_egaux_meme_si_le_reste_differe() {
        Hopital hopitalA = new Hopital(1L, "Nom A", "Adresse A", localisation, List.of(cardiologie), 5);
        Hopital hopitalB = new Hopital(1L, "Nom B", "Adresse B", localisation, List.of(pediatrie), 0);

        assertThat(hopitalA).isEqualTo(hopitalB);
        assertThat(hopitalA).hasSameHashCodeAs(hopitalB);
    }

    @Test
    void deux_hopitaux_avec_des_id_differents_ne_sont_pas_egaux() {
        Hopital hopitalA = new Hopital(1L, "Nom", "Adresse", localisation, List.of(cardiologie), 5);
        Hopital hopitalB = new Hopital(2L, "Nom", "Adresse", localisation, List.of(cardiologie), 5);

        assertThat(hopitalA).isNotEqualTo(hopitalB);
    }
}