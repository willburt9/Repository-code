package com.openclassroom.projet11.adapter.in.web.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.openclassroom.projet11.domain.exception.BusinessException;

/**
 * Gestionnaire global des exceptions REST.
 *
 * Toutes les exceptions non traitées
 * par les contrôleurs passent ici.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {


        /**
         * Gestion des erreurs métier générales.
         */
        @ExceptionHandler(BusinessException.class)
        public ResponseEntity<ErrorResponse> handleBusinessException(
                BusinessException exception) {


        ErrorResponse response =
                new ErrorResponse(
                        HttpStatus.NOT_FOUND.value(),
                        exception.getCode(),
                        exception.getMessage()
                );


        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
        }



        /**
         * Gestion des erreurs de paramètres invalides.
         */
        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
                IllegalArgumentException exception) {


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


}