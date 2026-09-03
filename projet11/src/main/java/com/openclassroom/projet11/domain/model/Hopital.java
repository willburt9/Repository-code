package com.openclassroom.projet11.domain.model;

import com.openclassroom.projet11.domain.exception.NoAvailableBedException;

import java.util.List;
import java.util.Objects;

/**
 * Représente un établissement hospitalier.
 *
 * <p>
 * L'hôpital constitue l'agrégat principal du domaine.
 * Il regroupe les informations nécessaires pour déterminer
 * sa capacité à prendre en charge un patient :
 * </p>
 *
 * <ul>
 *     <li>sa localisation ;</li>
 *     <li>son adresse postale ;</li>
 *     <li>les spécialités qu'il prend en charge ;</li>
 *     <li>le nombre de lits actuellement disponibles.</li>
 * </ul>
 */
public final class Hopital {

    private final Long id;

    private final String nom;

    private final String adresse;

    private final Location localisation;

    private final List<Specialite> specialites;

    private int litsDisponibles;

    /**
     * Initialise un établissement hospitalier.
     *
     * @param id identifiant unique
     * @param nom nom de l'établissement
     * @param adresse adresse postale de l'établissement
     * @param localisation coordonnées géographiques
     * @param specialites spécialités proposées
     * @param litsDisponibles nombre de lits disponibles
     */
    public Hopital(
            Long id,
            String nom,
            String adresse,
            Location localisation,
            List<Specialite> specialites,
            int litsDisponibles) {

        this.id = Objects.requireNonNull(id,
                "L'identifiant est obligatoire.");

        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException(
                    "Le nom de l'hôpital est obligatoire.");
        }

        if (adresse == null || adresse.isBlank()) {
            throw new IllegalArgumentException(
                    "L'adresse de l'hôpital est obligatoire.");
        }

        this.localisation = Objects.requireNonNull(
                localisation,
                "La localisation est obligatoire.");

        if (specialites == null || specialites.isEmpty()) {
            throw new IllegalArgumentException(
                    "Un hôpital doit proposer au moins une spécialité.");
        }

        if (litsDisponibles < 0) {
            throw new IllegalArgumentException(
                    "Le nombre de lits disponibles ne peut pas être négatif.");
        }

        this.nom = nom.trim();
        this.adresse = adresse.trim();
        this.specialites = List.copyOf(specialites);
        this.litsDisponibles = litsDisponibles;
    }

    /**
     * Indique si l'établissement possède la spécialité demandée.
     *
     * @param specialite spécialité recherchée
     * @return {@code true} si la spécialité est prise en charge
     */
    public boolean possedeLaSpecialite(Specialite specialite) {
        return specialites.contains(specialite);
    }

    /**
     * Indique si l'établissement dispose d'au moins un lit libre.
     *
     * @return {@code true} si un lit est disponible
     */
    public boolean disposeDeLits() {
        return litsDisponibles > 0;
    }

    /**
     * Détermine si l'établissement est capable de prendre en charge
     * un patient nécessitant la spécialité demandée.
     *
     * @param specialite spécialité recherchée
     * @return {@code true} si l'établissement répond aux critères
     */
    public boolean peutPrendreEnCharge(Specialite specialite) {
        return disposeDeLits()
                && possedeLaSpecialite(specialite);
    }

    /**
     * Réserve un lit.
     *
     * @throws NoAvailableBedException si aucun lit n'est disponible
     */
    public void reserverLit() {

        if (!disposeDeLits()) {
            throw new NoAvailableBedException(
                    "Aucun lit n'est disponible dans l'hôpital \"" + nom + "\".");
        }

        litsDisponibles--;
    }

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public Location getLocalisation() {
        return localisation;
    }

    public List<Specialite> getSpecialites() {
        return List.copyOf(specialites);
    }

    public int getLitsDisponibles() {
        return litsDisponibles;
    }

    @Override
    public boolean equals(Object object) {

        if (this == object) {
            return true;
        }

        if (!(object instanceof Hopital other)) {
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