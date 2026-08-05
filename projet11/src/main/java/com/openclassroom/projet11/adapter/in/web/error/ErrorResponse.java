package com.openclassroom.projet11.adapter.in.web.error;

import java.time.LocalDateTime;

/**
 * Représente le format JSON retourné lors d'une erreur API.
 */
public class ErrorResponse {

    private LocalDateTime timestamp;

    private int status;

    private String error;

    private String message;


    public ErrorResponse(
            int status,
            String error,
            String message) {

        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
    }


    public LocalDateTime getTimestamp() {
        return timestamp;
    }


    public int getStatus() {
        return status;
    }


    public String getError() {
        return error;
    }


    public String getMessage() {
        return message;
    }
}