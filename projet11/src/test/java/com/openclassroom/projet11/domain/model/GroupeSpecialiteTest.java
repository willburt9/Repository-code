package com.openclassroom.projet11.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * GroupeSpecialiteTest
 * Tests unitaires pour la classe GroupeSpecialite.
 */
class GroupeSpecialiteTest {

    @Test
    void construit_un_groupe_valide_et_trim_le_nom() {
        GroupeSpecialite groupe = new GroupeSpecialite(5L, "  Groupe de médecine générale  ");

        assertThat(groupe.getId()).isEqualTo(5L);
        assertThat(groupe.getNom()).isEqualTo("Groupe de médecine générale");
    }

    @Test
    void rejette_un_id_null() {
        assertThatThrownBy(() -> new GroupeSpecialite(null, "Nom"))
                .isInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void rejette_un_nom_vide_ou_blanc(String nomInvalide) {
        assertThatThrownBy(() -> new GroupeSpecialite(5L, nomInvalide))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void deux_groupes_avec_le_meme_id_sont_egaux() {
        GroupeSpecialite groupeA = new GroupeSpecialite(5L, "Nom A");
        GroupeSpecialite groupeB = new GroupeSpecialite(5L, "Nom B");

        assertThat(groupeA).isEqualTo(groupeB);
        assertThat(groupeA).hasSameHashCodeAs(groupeB);
    }

    @Test
    void toString_retourne_le_nom() {
        GroupeSpecialite groupe = new GroupeSpecialite(5L, "Groupe de médecine générale");

        assertThat(groupe.toString()).isEqualTo("Groupe de médecine générale");
    }
}