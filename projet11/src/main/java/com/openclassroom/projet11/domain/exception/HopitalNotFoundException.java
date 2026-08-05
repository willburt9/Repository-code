package com.openclassroom.projet11.domain.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception métier levée quand aucun hôpital éligible (spécialité + lit disponible)
 * n'est trouvé. Traduite en HTTP 404 par le contrôleur REST (tâche séparée),
 * conformément au résultat attendu du Plan de test BDD FT-01.
 */
public class HopitalNotFoundException extends BusinessException{
    /**
     * Construit une exception métier avec un message d'erreur détaillé.
     * @param specialite
     */
    public HopitalNotFoundException(String specialite) {
        super(
            HttpStatus.NOT_FOUND,
            "HOPITAL_NOT_FOUND", 
            "Aucun hôpital disponible pour la spécialité : " + specialite
        );
    }
}