package com.openclassroom.projet11.adapter.out.persistence;

import com.openclassroom.projet11.domain.exception.HopitalNotFoundException;
import com.openclassroom.projet11.domain.model.Hopital;
import com.openclassroom.projet11.domain.model.Specialite;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test d'intégration
 */
@JdbcTest
@Import(HopitalRepositoryAdapter.class)
class HopitalRepositoryAdapterIT {

    @Autowired
    private HopitalRepositoryAdapter hopitalRepository;

    @Test
    void findAll_retourne_tous_les_hopitaux_avec_leurs_specialites() {
        List<Hopital> hopitaux = hopitalRepository.findAll();

        assertThat(hopitaux).hasSize(10); // 10 hôpitaux dans data.sql
        assertThat(hopitaux)
                .extracting(Hopital::getNom)
                .contains("Hôpital Pitié-Salpêtrière", "Hôpital Lariboisière");
    }

     @Test
    void findAll_retourne_les_hopitaux_tries_par_id_croissant() {
        List<Hopital> hopitaux = hopitalRepository.findAll();
 
        assertThat(hopitaux)
                .extracting(Hopital::getId)
                .isSorted();
    }
 
    @Test
    void findAll_ne_duplique_pas_les_hopitaux_malgre_la_jointure_multi_specialites() {
        List<Hopital> hopitaux = hopitalRepository.findAll();
 
        List<Long> ids = hopitaux.stream().map(Hopital::getId).toList();
        assertThat(ids).doesNotHaveDuplicates();
    }

    @Test
    void findById_reconstruit_lagregat_complet_avec_ses_specialites() {
        Hopital hopital = hopitalRepository.findById(1L);

        assertThat(hopital.getNom()).isEqualTo("Hôpital Pitié-Salpêtrière");
        assertThat(hopital.getAdresse()).isNotBlank();
        assertThat(hopital.getSpecialites()).isNotEmpty();
    }

    @Test
    void findById_reconstruit_correctement_un_hopital_ayant_plusieurs_specialites() {
        Hopital hopital = hopitalRepository.findById(1L);
 
        assertThat(hopital.getSpecialites())
                .as("l'hôpital de test doit avoir plusieurs spécialités pour valider l'agrégation 1-N")
                .hasSizeGreaterThan(1);
 
        assertThat(hopital.getSpecialites())
                .extracting(Specialite::getNom)
                .doesNotContainNull()
                .doesNotHaveDuplicates();
 
        assertThat(hopital.getSpecialites())
                .allSatisfy(specialite -> {
                    assertThat(specialite.getGroupe()).isNotNull();
                    assertThat(specialite.getGroupe().getNom()).isNotBlank();
                });
    }

    @Test
    void findById_leve_hopitalNotFoundException_si_id_inconnu() {
        assertThatThrownBy(() -> hopitalRepository.findById(9999L))
                .isInstanceOf(HopitalNotFoundException.class);
    }

    @Test
    void mettreAJourLitsDisponibles_persiste_reellement_la_nouvelle_valeur() {
        hopitalRepository.mettreAJourLitsDisponibles(1L, 99);

        Hopital hopital = hopitalRepository.findById(1L);
        assertThat(hopital.getLitsDisponibles()).isEqualTo(99);
    }

     @Test
    void mettreAJourLitsDisponibles_ne_modifie_pas_les_autres_hopitaux() {
        hopitalRepository.mettreAJourLitsDisponibles(1L, 0);
 
        Hopital autreHopital = hopitalRepository.findById(2L);
        assertThat(autreHopital.getLitsDisponibles()).isNotEqualTo(0);
    }
 
    @Test
    void mettreAJourLitsDisponibles_leve_hopitalNotFoundException_si_id_inconnu() {
        assertThatThrownBy(() -> hopitalRepository.mettreAJourLitsDisponibles(9999L, 5))
                .isInstanceOf(HopitalNotFoundException.class);
    }
}