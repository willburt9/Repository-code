package com.openclassroom.projet11.domain.model;

import com.openclassroom.projet11.domain.exception.InvalidLocationException;

/**
 * Représente une position géographique.
 * <p>
 * Cet objet métier est immuable et sert à représenter une localisation
 * exprimée par une latitude et une longitude.
 * </p>
 *
 * <p>
 * Les coordonnées sont validées conformément aux limites du système GPS :
 * </p>
 * <ul>
 *     <li>Latitude : [-90 ; 90]</li>
 *     <li>Longitude : [-180 ; 180]</li>
 * </ul>
 */
public record Location(
        double latitude,
        double longitude
) {

    /**
     * Initialise une localisation valide.
     *
     * @throws InvalidLocationException si les coordonnées sont invalides.
     */
    public Location {

        if (latitude < -90 || latitude > 90) {
            throw new InvalidLocationException(
                    "La latitude doit être comprise entre -90 et 90 degrés.");
        }

        if (longitude < -180 || longitude > 180) {
            throw new InvalidLocationException(
                    "La longitude doit être comprise entre -180 et 180 degrés.");
        }
    }

    /**
     * Retourne la latitude de la localisation.
     *
     * @return latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Retourne la longitude de la localisation.
     *
     * @return longitude
     */
    public double getLongitude() {
        return longitude;
    }
}