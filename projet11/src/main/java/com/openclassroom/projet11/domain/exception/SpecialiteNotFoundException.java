package com.openclassroom.projet11.domain.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception métier pour indiquer qu'aucune spécialité n'a été trouvée.
 */
public class SpecialiteNotFoundException extends BusinessException {
    /**
     * Construit une exception métier avec un message d'erreur détaillé.
     * Le message d'erreur est "Aucune spécialité trouvée".
     */
    public SpecialiteNotFoundException(String message) {
        super(
            HttpStatus.NOT_FOUND,
            "SPECIALITE_NOT_FOUND",
            message
        );
    }
}
