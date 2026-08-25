package com.openclassroom.projet11.adapter.out.persistence;

import com.openclassroom.projet11.domain.exception.SpecialiteNotFoundException;
import com.openclassroom.projet11.domain.model.GroupeSpecialite;
import com.openclassroom.projet11.domain.model.Specialite;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires de {@link SpecialiteRepositoryAdapter}.
 */
@ExtendWith(MockitoExtension.class)
class SpecialiteRepositoryAdapterTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private SpecialiteRepositoryAdapter adapter;

    private final GroupeSpecialite groupeMedecine = new GroupeSpecialite(10L, "Médecine");
    private final GroupeSpecialite groupeChirurgie = new GroupeSpecialite(11L, "Chirurgie");
    private final Specialite cardiologie = new Specialite(100L, "Cardiologie", groupeMedecine);
    private final Specialite chirurgieGenerale = new Specialite(102L, "Chirurgie générale", groupeChirurgie);

    @SuppressWarnings("unchecked")
    @Test
    void findAll_devraitRetournerToutesLesSpecialites() {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class)))
                .thenReturn(List.of(cardiologie, chirurgieGenerale));

        List<Specialite> resultats = adapter.findAll();

        assertEquals(2, resultats.size());
        assertTrue(resultats.contains(cardiologie));
        assertTrue(resultats.contains(chirurgieGenerale));
    }

    @SuppressWarnings("unchecked")
    @Test
    void findAll_devraitRetournerListeVide_quandAucuneSpecialite() {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class)))
                .thenReturn(Collections.emptyList());

        List<Specialite> resultats = adapter.findAll();

        assertTrue(resultats.isEmpty());
    }

    @SuppressWarnings("unchecked")
    @Test
    void findById_devraitRetournerSpecialite_quandElleExiste() {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(100L)))
                .thenReturn(List.of(cardiologie));

        Specialite resultat = adapter.findById(100L);

        assertEquals(100L, resultat.getId());
        assertEquals("Cardiologie", resultat.getNom());
        assertEquals("Médecine", resultat.getGroupe().getNom());
    }

    @SuppressWarnings("unchecked")
    @Test
    void findById_devraitLeverException_quandSpecialiteIntrouvable() {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(999L)))
                .thenReturn(Collections.emptyList());

        SpecialiteNotFoundException exception = assertThrows(
                SpecialiteNotFoundException.class,
                () -> adapter.findById(999L));

        assertTrue(exception.getMessage().contains("999"));
    }

    @SuppressWarnings("unchecked")
    @Test
    void rowMapper_devraitConstruireGroupeEtSpecialiteDepuisLeResultSet() throws SQLException {
        ArgumentCaptor<RowMapper<Specialite>> captor = ArgumentCaptor.forClass(RowMapper.class);
        when(jdbcTemplate.query(anyString(), captor.capture())).thenReturn(Collections.emptyList());
        adapter.findAll(); // déclenche l'appel à jdbcTemplate.query et capture le RowMapper réel

        RowMapper<Specialite> rowMapper = captor.getValue();

        ResultSet rs = mock(ResultSet.class);
        when(rs.getLong("specialite_id")).thenReturn(100L);
        when(rs.getString("specialite_nom")).thenReturn("Cardiologie");
        when(rs.getLong("groupe_id")).thenReturn(10L);
        when(rs.getString("groupe_nom")).thenReturn("Médecine");

        Specialite resultat = rowMapper.mapRow(rs, 0);

        assertEquals(100L, resultat.getId());
        assertEquals("Cardiologie", resultat.getNom());
        assertEquals(10L, resultat.getGroupe().getId());
        assertEquals("Médecine", resultat.getGroupe().getNom());
    }
}