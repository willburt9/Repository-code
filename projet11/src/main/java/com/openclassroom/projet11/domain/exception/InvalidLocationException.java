package com.openclassroom.projet11.domain.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception métier pour indiquer qu'une localisation est invalide.
 * InvalidLocationException
 */
public class InvalidLocationException extends BusinessException {

    /**
     * Construit une exception métier avec un message d'erreur détaillé.
     * Le message d'erreur est "Localisation invalide".
     */
    public InvalidLocationException(String message) {
        super(
            HttpStatus.BAD_REQUEST,
            "INVALID_LOCATION",
            message
        );
    }
}
