package com.openclassroom.projet11.adapter.out.distance.dto;

/**
 * Requête utilisée pour construire un appel
 * GET /route de GraphHopper.
 */
public class DirectionsRequest {

    private final String pointDepart;
    private final String pointArrivee;
    private final String profile;
    private final String locale;

    private DirectionsRequest(
            String pointDepart,
            String pointArrivee,
            String profile,
            String locale) {

        this.pointDepart = pointDepart;
        this.pointArrivee = pointArrivee;
        this.profile = profile;
        this.locale = locale;
    }

    /**
     * Construit une requête GraphHopper.
     *
     * @param longitudeDepart longitude du départ
     * @param latitudeDepart latitude du départ
     * @param longitudeArrivee longitude de l'arrivée
     * @param latitudeArrivee latitude de l'arrivée
     */
    public static DirectionsRequest de(
            double longitudeDepart,
            double latitudeDepart,
            double longitudeArrivee,
            double latitudeArrivee) {

        return new DirectionsRequest(
                latitudeDepart + "," + longitudeDepart,
                latitudeArrivee + "," + longitudeArrivee,
                "car",
                "fr"
        );
    }

    public String getPointDepart() {
        return pointDepart;
    }

    public String getPointArrivee() {
        return pointArrivee;
    }

    public String getProfile() {
        return profile;
    }

    public String getLocale() {
        return locale;
    }
}