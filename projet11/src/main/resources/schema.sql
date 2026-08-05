/******************************************************************************
 * Projet 11 - OpenClassrooms
 *
 * Schéma de la base de données H2 utilisée pour le développement
 * et les tests de l'application.
 *
 * Les données de référence (groupes et spécialités NHS) sont chargées
 * par le fichier data.sql.
 ******************************************************************************/

DROP TABLE IF EXISTS hopital_specialite;
DROP TABLE IF EXISTS hopital;
DROP TABLE IF EXISTS specialite;
DROP TABLE IF EXISTS groupe_specialite;

/* ============================================================================
 * TABLE : GROUPE_SPECIALITE
 * ----------------------------------------------------------------------------
 * Référentiel des groupes de spécialités médicales NHS.
 * ========================================================================== */

CREATE TABLE groupe_specialite (
    id BIGINT PRIMARY KEY,
    nom VARCHAR(150) NOT NULL UNIQUE
);

/* ============================================================================
 * TABLE : SPECIALITE
 * ----------------------------------------------------------------------------
 * Référentiel des spécialités médicales.
 * Chaque spécialité appartient à un unique groupe.
 * ========================================================================== */

CREATE TABLE specialite (
    id BIGINT PRIMARY KEY,
    nom VARCHAR(200) NOT NULL,
    groupe_id BIGINT NOT NULL,

    CONSTRAINT fk_specialite_groupe
        FOREIGN KEY (groupe_id)
        REFERENCES groupe_specialite(id),

    CONSTRAINT uk_specialite_nom
        UNIQUE (nom)
);

/* ============================================================================
 * TABLE : HOPITAL
 * ----------------------------------------------------------------------------
 * Représente les établissements hospitaliers.
 * ========================================================================== */

CREATE TABLE hopital (
    id BIGINT PRIMARY KEY,
    nom VARCHAR(200) NOT NULL,
    adresse VARCHAR(255) NOT NULL,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL,
    lits_disponibles INTEGER NOT NULL CHECK (lits_disponibles >= 0)
);

/* ============================================================================
 * TABLE : HOPITAL_SPECIALITE
 * ----------------------------------------------------------------------------
 * Association N:N entre les hôpitaux et les spécialités proposées.
 * ========================================================================== */

CREATE TABLE hopital_specialite (
    hopital_id BIGINT NOT NULL,
    specialite_id BIGINT NOT NULL,

    PRIMARY KEY (hopital_id, specialite_id),

    CONSTRAINT fk_hs_hopital
        FOREIGN KEY (hopital_id)
        REFERENCES hopital(id),

    CONSTRAINT fk_hs_specialite
        FOREIGN KEY (specialite_id)
        REFERENCES specialite(id)
);