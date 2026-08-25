package com.openclassroom.projet11.adapter.in.web.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

import com.openclassroom.projet11.adapter.out.logging.AuditLog;
import com.openclassroom.projet11.domain.exception.BusinessException;

/**
 * Gestionnaire global des exceptions REST.
 *
 * Toutes les exceptions non traitées
 * par les contrôleurs passent ici.
 * <p>
 * Chaque exception est journalisée dans le log d'audit ("AUDIT"), y compris
 * les échecs (pas seulement les succès déjà logués dans les contrôleurs) :
 * sans ça, une panne du fournisseur de distance externe (OpenRouteService)
 * restait invisible dans les journaux métier.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {


        /**
         * Gestion des erreurs métier générales.
         * <p>
         * Utilise le statut HTTP porté par l'exception elle-même
         * (exception.getStatus()) plutôt qu'un statut fixe.
         */
        @ExceptionHandler(BusinessException.class)
        public ResponseEntity<ErrorResponse> handleBusinessException(
                BusinessException exception) {

                AuditLog.LOGGER.warn("event=erreur type={} code={} status={} message=\"{}\"",
                        exception.getClass().getSimpleName(),
                        exception.getCode(),
                        exception.getStatus().value(),
                        exception.getMessage());

                ErrorResponse response =
                        new ErrorResponse(
                                exception.getStatus().value(),
                                exception.getCode(),
                                exception.getMessage()
                        );

                return ResponseEntity
                        .status(exception.getStatus())
                        .body(response);
        }


        /**
         * Gestion des erreurs de paramètres invalides.
         */
        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
                IllegalArgumentException exception) {

                AuditLog.LOGGER.warn("event=erreur type={} status={} message=\"{}\"",
                        exception.getClass().getSimpleName(),
                        HttpStatus.BAD_REQUEST.value(),
                        exception.getMessage());

                ErrorResponse response =
                        new ErrorResponse(
                                HttpStatus.BAD_REQUEST.value(),
                                "Bad Request",
                                exception.getMessage()
                        );

                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(response);
        }


        /**
         * Gestion des erreurs de validation Bean Validation (@Valid sur un
         * @RequestBody).
         */
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
                MethodArgumentNotValidException exception) {

                String message = exception.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .findFirst()
                        .map(FieldError::getDefaultMessage)
                        .orElse("Requête invalide.");

                AuditLog.LOGGER.warn("event=erreur type={} status={} message=\"{}\"",
                        exception.getClass().getSimpleName(),
                        HttpStatus.BAD_REQUEST.value(),
                        message);

                ErrorResponse response =
                        new ErrorResponse(
                                HttpStatus.BAD_REQUEST.value(),
                                "VALIDATION_ERROR",
                                message
                        );

                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(response);
        }


        /**
         * Panne du fournisseur de distance externe (OpenRouteService injoignable,
         * timeout, erreur HTTP non 2xx...). Le message technique n'est jamais
         * renvoyé tel quel au client (il pourrait révéler des détails internes) ;
         * il est journalisé côté serveur pour investigation.
         */
        @ExceptionHandler(RestClientException.class)
        public ResponseEntity<ErrorResponse> handleRestClientException(
                RestClientException exception) {

                AuditLog.LOGGER.error("event=erreur type={} status={} message=\"{}\"",
                        exception.getClass().getSimpleName(),
                        HttpStatus.SERVICE_UNAVAILABLE.value(),
                        exception.getMessage());

                ErrorResponse response =
                        new ErrorResponse(
                                HttpStatus.SERVICE_UNAVAILABLE.value(),
                                "DISTANCE_PROVIDER_UNAVAILABLE",
                                "Le service de calcul d'itinéraire est temporairement indisponible. Réessayez dans quelques instants."
                        );

                return ResponseEntity
                        .status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body(response);
        }


        /**
         * Filet de sécurité final : toute exception non prévue ci-dessus
         * (bug, état inattendu comme "Aucun itinéraire trouvé"...) est
         * capturée ici plutôt que de remonter en Whitelabel Error Page.
         * Le détail technique reste uniquement dans le log serveur, jamais
         * dans la réponse HTTP (évite une fuite d'information interne).
         */
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGenericException(
                Exception exception) {

                AuditLog.LOGGER.error("event=erreur type={} status={} message=\"{}\"",
                        exception.getClass().getSimpleName(),
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        exception.getMessage());

                ErrorResponse response =
                        new ErrorResponse(
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "INTERNAL_ERROR",
                                "Une erreur interne inattendue est survenue."
                        );

                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(response);
        }

        @ExceptionHandler(MissingServletRequestParameterException.class)
        public ResponseEntity<ErrorResponse> handleMissingParameter(MissingServletRequestParameterException exception) {

                
                AuditLog.LOGGER.error("event=erreur type={} status={} message=\"{}\"",
                        exception.getClass().getSimpleName(),
                        HttpStatus.BAD_REQUEST.value(),
                        exception.getMessage());

                ErrorResponse response = 
                        new ErrorResponse(
                                HttpStatus.BAD_REQUEST.value(),
                                "MISSING_PARAMETER",
                                "Paramètre requis manquant : " + exception.getParameterName()
                        );
         
                return ResponseEntity
                        .badRequest()
                        .body(response);
        }

}