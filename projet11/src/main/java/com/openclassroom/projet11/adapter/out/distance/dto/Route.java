package com.openclassroom.projet11.adapter.out.distance.dto;

/**
 * Représente un itinéraire retourné par GraphHopper.
 */
public class Route {

    /**
     * Distance du trajet en mètres.
     */
    private double distance;

    /**
     * Durée du trajet en millisecondes.
     */
    private long time;

    public Route() {
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}