package com.openclassroom.projet11.domain.model;

import java.util.Objects;

/**
 * Représente un groupe de spécialités médicales défini par le NHS.
 *
 * <p>
 * Les groupes permettent de classifier les spécialités médicales
 * (ex. : Groupe chirurgical, Groupe de médecine générale,
 * Groupe de psychiatrie...).
 * </p>
 *
 * <p>
 * Cette classe représente une donnée de référence du domaine.
 * Son identité est portée par son identifiant.
 * </p>
 */
public final class GroupeSpecialite {

    private final Long id;

    private final String nom;

    /**
     * Initialise un groupe de spécialités.
     *
     * @param id identifiant unique
     * @param nom nom du groupe
     */
    public GroupeSpecialite(Long id, String nom) {

        this.id = Objects.requireNonNull(id,
                "L'identifiant du groupe est obligatoire.");

        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException(
                    "Le nom du groupe est obligatoire.");
        }

        this.nom = nom.trim();
    }

    /**
     * Retourne l'identifiant du groupe.
     *
     * @return identifiant unique
     */
    public Long getId() {
        return id;
    }

    /**
     * Retourne le nom du groupe.
     *
     * @return nom du groupe
     */
    public String getNom() {
        return nom;
    }

    @Override
    public boolean equals(Object object) {

        if (this == object) {
            return true;
        }

        if (!(object instanceof GroupeSpecialite other)) {
            return false;
        }

        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return nom;
    }

}