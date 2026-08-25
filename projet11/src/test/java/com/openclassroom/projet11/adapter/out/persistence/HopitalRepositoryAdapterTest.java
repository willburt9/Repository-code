package com.openclassroom.projet11.adapter.out.persistence;

import com.openclassroom.projet11.domain.exception.HopitalNotFoundException;
import com.openclassroom.projet11.domain.model.Hopital;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires de {@link HopitalRepositoryAdapter}.
 */
@ExtendWith(MockitoExtension.class)
class HopitalRepositoryAdapterTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private HopitalRepositoryAdapter adapter;

    /** Crée un ResultSet mocké représentant une ligne de la jointure hopital/specialite/groupe. */
    private ResultSet creerLigne(long hopitalId, String hopitalNom, String adresse,
                                  double latitude, double longitude, int lits,
                                  long specialiteId, String specialiteNom,
                                  long groupeId, String groupeNom) throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getLong("hopital_id")).thenReturn(hopitalId);
        when(rs.getString("hopital_nom")).thenReturn(hopitalNom);
        when(rs.getString("hopital_adresse")).thenReturn(adresse);
        when(rs.getDouble("latitude")).thenReturn(latitude);
        when(rs.getDouble("longitude")).thenReturn(longitude);
        when(rs.getInt("lits_disponibles")).thenReturn(lits);
        when(rs.getLong("specialite_id")).thenReturn(specialiteId);
        when(rs.getString("specialite_nom")).thenReturn(specialiteNom);
        when(rs.getLong("groupe_id")).thenReturn(groupeId);
        when(rs.getString("groupe_nom")).thenReturn(groupeNom);
        return rs;
    }

    /** Simule le comportement réel de jdbcTemplate.query(sql, handler, params) : rejoue les lignes fournies. */
    private void simulerLignes(ResultSet... lignes) {
        doAnswer(invocation -> {
            RowCallbackHandler handler = invocation.getArgument(1);
            for (ResultSet ligne : lignes) {
                handler.processRow(ligne);
            }
            return null;
        }).when(jdbcTemplate).query(anyString(), any(RowCallbackHandler.class), any(Object[].class));
    }

    @Test
    void findAll_devraitAgregerLesSpecialitesParHopital() throws SQLException {
        ResultSet ligneHopital1Cardiologie = creerLigne(1L, "Hôpital Cochin", "1 Rue de la Santé",
                48.8566, 2.3522, 10, 100L, "Cardiologie", 10L, "Médecine");
        ResultSet ligneHopital1Pneumologie = creerLigne(1L, "Hôpital Cochin", "1 Rue de la Santé",
                48.8566, 2.3522, 10, 101L, "Pneumologie", 10L, "Médecine");
        ResultSet ligneHopital2Chirurgie = creerLigne(2L, "Hôpital Tenon", "10 Rue de la République",
                45.7640, 4.8357, 5, 102L, "Chirurgie", 11L, "Chirurgie");
        simulerLignes(ligneHopital1Cardiologie, ligneHopital1Pneumologie, ligneHopital2Chirurgie);

        List<Hopital> resultats = adapter.findAll();

        assertEquals(2, resultats.size());

        Hopital hopital1 = resultats.stream()
                .filter(h -> h.getId().equals(1L))
                .findFirst()
                .orElseThrow();
        assertEquals("Hôpital Cochin", hopital1.getNom());
        assertEquals("1 Rue de la Santé", hopital1.getAdresse());
        assertEquals(48.8566, hopital1.getLocalisation().getLatitude());
        assertEquals(2.3522, hopital1.getLocalisation().getLongitude());
        assertEquals(10, hopital1.getLitsDisponibles());
        assertEquals(2, hopital1.getSpecialites().size());
        assertTrue(hopital1.getSpecialites().stream().anyMatch(s -> s.getNom().equals("Cardiologie")));
        assertTrue(hopital1.getSpecialites().stream().anyMatch(s -> s.getNom().equals("Pneumologie")));

        Hopital hopital2 = resultats.stream()
                .filter(h -> h.getId().equals(2L))
                .findFirst()
                .orElseThrow();
        assertEquals(1, hopital2.getSpecialites().size());
        assertEquals("Chirurgie", hopital2.getSpecialites().get(0).getNom());
        assertEquals("Chirurgie", hopital2.getSpecialites().get(0).getGroupe().getNom());
    }

    @Test
    void findAll_devraitRetournerListeVide_quandAucunHopital() {
        simulerLignes(); // aucune ligne

        List<Hopital> resultats = adapter.findAll();

        assertTrue(resultats.isEmpty());
    }

    @Test
    void findById_devraitRetournerHopital_quandIlExiste() throws SQLException {
        ResultSet ligneHopital1Cardiologie = creerLigne(1L, "Hôpital Cochin", "1 Rue de la Santé",
                48.8566, 2.3522, 10, 100L, "Cardiologie", 10L, "Médecine");
        ResultSet ligneHopital1Pneumologie = creerLigne(1L, "Hôpital Cochin", "1 Rue de la Santé",
                48.8566, 2.3522, 10, 101L, "Pneumologie", 10L, "Médecine");
        simulerLignes(ligneHopital1Cardiologie, ligneHopital1Pneumologie);

        Hopital resultat = adapter.findById(1L);

        assertEquals(1L, resultat.getId());
        assertEquals("Hôpital Cochin", resultat.getNom());
        assertEquals(2, resultat.getSpecialites().size());
    }

    @Test
    void findById_devraitLeverException_quandHopitalIntrouvable() {
        simulerLignes(); // aucune ligne renvoyée par la requête

        HopitalNotFoundException exception = assertThrows(
                HopitalNotFoundException.class,
                () -> adapter.findById(999L));

        assertTrue(exception.getMessage().contains("999"));
    }

    @Test
    void mettreAJourLitsDisponibles_devraitReussir_quandHopitalExiste() {
        when(jdbcTemplate.update(anyString(), eq(20), eq(1L))).thenReturn(1);

        assertDoesNotThrow(() -> adapter.mettreAJourLitsDisponibles(1L, 20));

        verify(jdbcTemplate).update(anyString(), eq(20), eq(1L));
    }

    @Test
    void mettreAJourLitsDisponibles_devraitLeverException_quandHopitalIntrouvable() {
        when(jdbcTemplate.update(anyString(), eq(20), eq(999L))).thenReturn(0);

        HopitalNotFoundException exception = assertThrows(
                HopitalNotFoundException.class,
                () -> adapter.mettreAJourLitsDisponibles(999L, 20));

        assertTrue(exception.getMessage().contains("999"));
    }
}