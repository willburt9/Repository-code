package com.openclassroom.projet11.adapter.out.persistence;

import com.openclassroom.projet11.application.port.out.HopitalRepositoryPort;
import com.openclassroom.projet11.domain.exception.HopitalNotFoundException;
import com.openclassroom.projet11.domain.model.GroupeSpecialite;
import com.openclassroom.projet11.domain.model.Hopital;
import com.openclassroom.projet11.domain.model.Location;
import com.openclassroom.projet11.domain.model.Specialite;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Adaptateur de sortie JDBC pour le référentiel des hôpitaux.
 * <p>
 * {@link Hopital} étant immuable et exigeant sa liste complète de spécialités
 * au constructeur, la jointure hopital/hopital_specialite/specialite/groupe_specialite
 * est agrégée en mémoire (par id d'hôpital) avant reconstruction de l'agrégat.
 */
@Repository
public class HopitalRepositoryAdapter implements HopitalRepositoryPort {

    /**
     * Requête SQL pour récupérer tous les hôpitaux avec leurs spécialités et groupes de spécialités.
     */
    private static final String SELECT_BASE = """
            SELECT h.id AS hopital_id, h.nom AS hopital_nom, h.adresse AS hopital_adresse,
                   h.latitude, h.longitude, h.lits_disponibles,
                   s.id AS specialite_id, s.nom AS specialite_nom,
                   g.id AS groupe_id, g.nom AS groupe_nom
            FROM hopital h
            JOIN hopital_specialite hs ON hs.hopital_id = h.id
            JOIN specialite s ON s.id = hs.specialite_id
            JOIN groupe_specialite g ON g.id = s.groupe_id
            """;
 

    private final JdbcTemplate jdbcTemplate;

    /**
     * Construit un adaptateur de sortie JDBC pour le référentiel des hôpitaux.
     * @param jdbcTemplate
     */
    public HopitalRepositoryAdapter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Récupère tous les hôpitaux avec leurs spécialités et groupes de spécialités.
     *
     * @return liste des hôpitaux
     */
    @Override
    public List<Hopital> findAll() {
        return extraireHopitaux(SELECT_BASE + " ORDER BY h.id").values().stream()
                .map(HopitalEnConstruction::build)
                .toList();
    }

    @Override
    public Hopital findById(Long id) {
        return extraireHopitaux(SELECT_BASE + " WHERE h.id = ?", id).values().stream()
                .findFirst()
                .map(HopitalEnConstruction::build)
                .orElseThrow(() -> new HopitalNotFoundException("Aucun hôpital trouvé pour l'id " + id));
    }
 
    /**
     * Met à jour le nombre de lits disponibles pour un hôpital donné.
     * @param hopitalId identifiant unique de l'hôpital
     * @param nouveauNombreDeLits nouveau nombre de lits disponibles    
     */
    @Override
    public void mettreAJourLitsDisponibles(Long hopitalId, int nouveauNombreDeLits) {
        int lignesModifiees = jdbcTemplate.update(
                "UPDATE hopital SET lits_disponibles = ? WHERE id = ?",
                nouveauNombreDeLits, hopitalId);
 
        if (lignesModifiees == 0) {
            throw new HopitalNotFoundException("Aucun hôpital trouvé pour l'id " + hopitalId);
        }
    }

    /**
     * Exécute la requête de jointure et agrège les lignes par id d'hôpital
     * (une ligne SQL = un hôpital + une spécialité, à cause de la jointure).
     */
    private Map<Long, HopitalEnConstruction> extraireHopitaux(String sql, Object... params) {
        Map<Long, HopitalEnConstruction> parHopital = new LinkedHashMap<>();
 
        jdbcTemplate.query(sql, (RowCallbackHandler) rs -> {
            long hopitalId = rs.getLong("hopital_id");
            String nom = rs.getString("hopital_nom");
            String adresse = rs.getString("hopital_adresse");
            double latitude = rs.getDouble("latitude");
            double longitude = rs.getDouble("longitude");
            int litsDisponibles = rs.getInt("lits_disponibles");
 
            long specialiteId = rs.getLong("specialite_id");
            String specialiteNom = rs.getString("specialite_nom");
            long groupeId = rs.getLong("groupe_id");
            String groupeNom = rs.getString("groupe_nom");
 
            HopitalEnConstruction enConstruction = parHopital.computeIfAbsent(hopitalId, id ->
                    new HopitalEnConstruction(id, nom, adresse, latitude, longitude, litsDisponibles));
 
            GroupeSpecialite groupe = new GroupeSpecialite(groupeId, groupeNom);
            Specialite specialite = new Specialite(specialiteId, specialiteNom, groupe);
            enConstruction.specialites.add(specialite);
        }, params);
 
        return parHopital;
    }

    /** Accumulateur interne : Hopital étant immuable, on assemble ses données avant instanciation. */
    private static final class HopitalEnConstruction {
        private final Long id;
        private final String nom;
        private final String adresse;
        private final double latitude;
        private final double longitude;
        private final int litsDisponibles;
        private final List<Specialite> specialites = new ArrayList<>();

        private HopitalEnConstruction(Long id, String nom, String adresse, double latitude, double longitude, int litsDisponibles) {
            this.id = id;
            this.nom = nom;
            this.adresse = adresse;
            this.latitude = latitude;
            this.longitude = longitude;
            this.litsDisponibles = litsDisponibles;
        }

        /**
         * Construit un objet Hopital à partir des données accumulées.
         * @return l'objet Hopital construit
         */
        private Hopital build() {
            return new Hopital(id, nom, adresse, new Location(latitude, longitude), specialites, litsDisponibles);
        }
    }
}