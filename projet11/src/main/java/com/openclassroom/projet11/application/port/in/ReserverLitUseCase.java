package com.openclassroom.projet11.application.port.in;

/**
 * ReserverLitUseCase
 * Interface représentant le cas d'utilisation de réservation d'un lit dans un hôpital.
 * Elle définit la méthode reserver qui prend en paramètre l'identifiant de l'hôpital et retourne un objet ReservationResultat contenant les détails de la réservation.
 * @param hopitalId l'identifiant unique de l'hôpital pour lequel la réservation est effectuée
 * @return un objet ReservationResultat contenant les détails de la réservation, y compris la référence patient anonymisée et le nombre de lits disponibles après la réservation
 */
public interface ReserverLitUseCase {
    ReservationResultat reserver(Long hopitalId);
}