package com.openclassroom.projet11.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * SpecialiteTest
 * Tests unitaires pour la classe Specialite.
 */
class SpecialiteTest {

    private final GroupeSpecialite groupe = new GroupeSpecialite(5L, "Groupe de médecine générale");

    @Test
    void construit_une_specialite_valide_et_trim_le_nom() {
        Specialite specialite = new Specialite(121L, "  Cardiologie  ", groupe);

        assertThat(specialite.getId()).isEqualTo(121L);
        assertThat(specialite.getNom()).isEqualTo("Cardiologie"); // trim()
        assertThat(specialite.getGroupe()).isEqualTo(groupe);
    }

    @Test
    void rejette_un_id_null() {
        assertThatThrownBy(() -> new Specialite(null, "Cardiologie", groupe))
                .isInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void rejette_un_nom_vide_ou_blanc(String nomInvalide) {
        assertThatThrownBy(() -> new Specialite(121L, nomInvalide, groupe))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejette_un_groupe_null() {
        assertThatThrownBy(() -> new Specialite(121L, "Cardiologie", null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void appartientAu_renvoie_vrai_pour_le_bon_groupe() {
        Specialite specialite = new Specialite(121L, "Cardiologie", groupe);

        assertThat(specialite.appartientAu(groupe)).isTrue();
    }

    @Test
    void appartientAu_renvoie_faux_pour_un_autre_groupe() {
        Specialite specialite = new Specialite(121L, "Cardiologie", groupe);
        GroupeSpecialite autreGroupe = new GroupeSpecialite(8L, "Groupe de pathologie");

        assertThat(specialite.appartientAu(autreGroupe)).isFalse();
    }

    @Test
    void deux_specialites_avec_le_meme_id_sont_egales_meme_si_le_nom_differe() {
        // equals()/hashCode() sont basés uniquement sur l'id (cf. code source) :
        // ce test documente ce choix explicitement.
        Specialite specialiteA = new Specialite(121L, "Cardiologie", groupe);
        Specialite specialiteB = new Specialite(121L, "Nom différent", groupe);

        assertThat(specialiteA).isEqualTo(specialiteB);
        assertThat(specialiteA).hasSameHashCodeAs(specialiteB);
    }

    @Test
    void deux_specialites_avec_des_id_differents_ne_sont_pas_egales() {
        Specialite specialiteA = new Specialite(121L, "Cardiologie", groupe);
        Specialite specialiteB = new Specialite(122L, "Cardiologie", groupe);

        assertThat(specialiteA).isNotEqualTo(specialiteB);
    }

    @Test
    void toString_retourne_le_nom() {
        Specialite specialite = new Specialite(121L, "Cardiologie", groupe);

        assertThat(specialite.toString()).isEqualTo("Cardiologie");
    }
}