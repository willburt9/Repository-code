package com.openclassroom.projet11.adapter.out.persistence;

import com.openclassroom.projet11.domain.exception.HopitalNotFoundException;
import com.openclassroom.projet11.domain.model.Hopital;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test d'intégration : à faire tourner séparément des
 * tests unitaires.
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
    void findById_reconstruit_lagregat_complet_avec_ses_specialites() {
        Hopital hopital = hopitalRepository.findById(1L);

        assertThat(hopital.getNom()).isEqualTo("Hôpital Pitié-Salpêtrière");
        assertThat(hopital.getAdresse()).isNotBlank();
        assertThat(hopital.getSpecialites()).isNotEmpty();
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
}