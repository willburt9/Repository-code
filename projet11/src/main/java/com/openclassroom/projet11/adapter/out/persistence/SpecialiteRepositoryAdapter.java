package com.openclassroom.projet11.adapter.out.persistence;

import com.openclassroom.projet11.application.port.out.SpecialiteRepositoryPort;
import com.openclassroom.projet11.domain.exception.SpecialiteNotFoundException;
import com.openclassroom.projet11.domain.model.GroupeSpecialite;
import com.openclassroom.projet11.domain.model.Specialite;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Adapter pour l'accès aux données des spécialités via JDBC.
 * SpecialiteRepositoryAdapter
 */
@Repository
public class SpecialiteRepositoryAdapter implements SpecialiteRepositoryPort {

    /**
     * Requête SQL de base pour récupérer les spécialités avec leurs groupes.
     */
    private static final String SELECT_BASE = """
            SELECT s.id AS specialite_id, s.nom AS specialite_nom, g.id AS groupe_id, g.nom AS groupe_nom
            FROM specialite s
            JOIN groupe_specialite g ON g.id = s.groupe_id
            """;

    /**
     * RowMapper pour mapper les résultats SQL en objets Specialite.
     */
    private static final RowMapper<Specialite> SPECIALITE_ROW_MAPPER = (rs, rowNum) -> {
        GroupeSpecialite groupe = new GroupeSpecialite(rs.getLong("groupe_id"), rs.getString("groupe_nom"));
        return new Specialite(rs.getLong("specialite_id"), rs.getString("specialite_nom"), groupe);
    };

    private final JdbcTemplate jdbcTemplate;

    public SpecialiteRepositoryAdapter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Récupère toutes les spécialités avec leurs groupes.
     */
    @Override
    public List<Specialite> findAll() {
        return jdbcTemplate.query(SELECT_BASE + " ORDER BY g.nom, s.nom", SPECIALITE_ROW_MAPPER);
    }

    /**
     * Récupère une spécialité par son identifiant.
     */
    @Override
    public Specialite findById(Long id) {
        List<Specialite> resultats = jdbcTemplate.query(
                SELECT_BASE + " WHERE s.id = ?", SPECIALITE_ROW_MAPPER, id);

        return resultats.stream().findFirst()
                .orElseThrow(() -> new SpecialiteNotFoundException(
                        "Aucune spécialité trouvée pour l'id " + id));
    }
}