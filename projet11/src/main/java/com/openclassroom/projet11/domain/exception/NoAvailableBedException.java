package com.openclassroom.projet11.domain.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception levée lorsqu'une réservation de lit est impossible
 * car aucun lit n'est disponible dans l'établissement hospitalier.
 *
 * <p>
 * Cette exception représente une règle métier du domaine :
 * un lit ne peut être réservé que si l'hôpital dispose encore
 * d'au moins un lit libre.
 * </p>
 */
public class NoAvailableBedException extends BusinessException {

    /**
     * Crée une exception avec un message explicite.
     *
     * @param message description de l'erreur métier
     */
    public NoAvailableBedException(String message) {
        super(
            HttpStatus.CONFLICT,
            "NO_AVAILABLE_BED",
            message
        );
    }
}