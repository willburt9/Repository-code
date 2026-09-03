package com.openclassroom.projet11.domain.exception;

import org.springframework.http.HttpStatus;

/**
 * Classe de base des exceptions métier de l'application.
 *
 * <p>
 * Une exception métier représente une erreur fonctionnelle
 * (par exemple : spécialité inexistante, aucun hôpital trouvé,
 * itinéraire impossible, etc.).
 * </p>
 *
 * <p>
 * Chaque exception métier possède :
 * <ul>
 *     <li>un code métier unique ;</li>
 *     <li>un statut HTTP à retourner au client ;</li>
 *     <li>un message explicite.</li>
 * </ul>
 * </p>
 */
public abstract class BusinessException extends RuntimeException {

    /**
     * Code métier de l'erreur.
     */
    private final String code;

    /**
     * Statut HTTP associé à l'erreur.
     */
    private final HttpStatus status;

    /**
     * Initialise une nouvelle exception métier.
     *
     * @param status  statut HTTP à renvoyer
     * @param code    code métier de l'erreur
     * @param message message détaillé
     */
    protected BusinessException(
            HttpStatus status,
            String code,
            String message) {

        super(message);
        this.status = status;
        this.code = code;
    }

    /**
     * Retourne le code métier.
     *
     * @return code de l'erreur
     */
    public String getCode() {
        return code;
    }

    /**
     * Retourne le statut HTTP associé.
     *
     * @return statut HTTP
     */
    public HttpStatus getStatus() {
        return status;
    }
}