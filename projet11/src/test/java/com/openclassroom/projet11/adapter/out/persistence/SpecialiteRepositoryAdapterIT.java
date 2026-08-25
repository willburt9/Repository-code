package com.openclassroom.projet11.adapter.out.persistence;

import com.openclassroom.projet11.domain.exception.SpecialiteNotFoundException;
import com.openclassroom.projet11.domain.model.Specialite;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.context.annotation.Import;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test d'intégration
 */
@JdbcTest
@Import(SpecialiteRepositoryAdapter.class)
class SpecialiteRepositoryAdapterIT {

    @Autowired
    private SpecialiteRepositoryAdapter specialiteRepository;

    @Test
    void findAll_retourne_le_referentiel_nhs_non_vide() {
        List<Specialite> specialites = specialiteRepository.findAll();

        assertThat(specialites).isNotEmpty();
    }

    @Test
    void findAll_associe_chaque_specialite_a_un_groupe_valide() {
        List<Specialite> specialites = specialiteRepository.findAll();

        assertThat(specialites).allSatisfy(specialite -> {
            assertThat(specialite.getNom()).isNotBlank();
            assertThat(specialite.getGroupe()).isNotNull();
            assertThat(specialite.getGroupe().getNom()).isNotBlank();
        });
    }

    @Test
    void findAll_ne_contient_pas_dids_dupliques() {
        List<Specialite> specialites = specialiteRepository.findAll();

        List<Long> ids = specialites.stream().map(Specialite::getId).toList();
        assertThat(ids).doesNotHaveDuplicates();
    }

    @Test
    void findAll_trie_par_nom_de_groupe_puis_nom_de_specialite() {
        List<Specialite> specialites = specialiteRepository.findAll();

        List<Specialite> attendu = specialites.stream()
                .sorted(Comparator
                        .comparing((Specialite s) -> s.getGroupe().getNom())
                        .thenComparing(Specialite::getNom))
                .toList();

        assertThat(specialites)
                .extracting(Specialite::getNom)
                .containsExactlyElementsOf(attendu.stream().map(Specialite::getNom).toList());
    }

    @Test
    void findById_retourne_la_specialite_correspondante() {
        // On récupère un id réellement présent en base plutôt que de supposer sa valeur,
        // le jeu de données NHS n'étant pas garanti de commencer à 1.
        Specialite reference = specialiteRepository.findAll().get(0);

        Specialite trouvee = specialiteRepository.findById(reference.getId());

        assertThat(trouvee.getId()).isEqualTo(reference.getId());
        assertThat(trouvee.getNom()).isEqualTo(reference.getNom());
        assertThat(trouvee.getGroupe().getId()).isEqualTo(reference.getGroupe().getId());
        assertThat(trouvee.getGroupe().getNom()).isEqualTo(reference.getGroupe().getNom());
    }

    @Test
    void findById_leve_specialiteNotFoundException_si_id_inconnu() {
        assertThatThrownBy(() -> specialiteRepository.findById(999_999L))
                .isInstanceOf(SpecialiteNotFoundException.class);
    }
}