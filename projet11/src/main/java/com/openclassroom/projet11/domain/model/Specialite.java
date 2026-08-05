package com.openclassroom.projet11.domain.model;

import java.util.Objects;

/**
 * Représente une spécialité médicale reconnue par le NHS.
 *
 * <p>
 * Une spécialité appartient obligatoirement à un groupe de spécialités.
 * </p>
 *
 * <p>
 * Exemple :
 * </p>
 * <ul>
 *     <li>Groupe : Médecine générale</li>
 *     <li>Spécialité : Cardiologie</li>
 * </ul>
 */
public final class Specialite {

    private final Long id;
    private final String nom;
    private final GroupeSpecialite groupe;

    /**
     * Initialise une spécialité médicale.
     *
     * @param id identifiant unique
     * @param nom nom de la spécialité
     * @param groupe groupe auquel appartient la spécialité
     */
    public Specialite(
            Long id,
            String nom,
            GroupeSpecialite groupe) {

        this.id = Objects.requireNonNull(id,
                "L'identifiant est obligatoire.");

        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException(
                    "Le nom de la spécialité est obligatoire.");
        }

        this.groupe = Objects.requireNonNull(
                groupe,
                "Une spécialité doit appartenir à un groupe.");

        this.nom = nom.trim();
    }

    /**
     * Retourne l'identifiant de la spécialité.
     *
     * @return identifiant unique
     */
    public Long getId() {
        return id;
    }

    /**
     * Retourne le nom de la spécialité.
     *
     * @return nom de la spécialité
     */
    public String getNom() {
        return nom;
    }

    /**
     * Retourne le groupe auquel appartient la spécialité.
     *
     * @return groupe de spécialités
     */
    public GroupeSpecialite getGroupe() {
        return groupe;
    }

    /**
     * Vérifie si cette spécialité appartient au groupe fourni.
     *
     * @param groupe groupe recherché
     * @return true si la spécialité appartient au groupe
     */
    public boolean appartientAu(GroupeSpecialite groupe) {
        return this.groupe.equals(groupe);
    }

    @Override
    public boolean equals(Object object) {

        if (this == object) {
            return true;
        }

        if (!(object instanceof Specialite other)) {
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