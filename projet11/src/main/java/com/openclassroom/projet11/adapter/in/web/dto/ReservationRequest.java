package com.openclassroom.projet11.adapter.in.web.dto;

import jakarta.validation.constraints.NotNull;

/**
 * Aucune donnée patient dans le corps de la requête : la référence patient
 * anonymisée est générée côté serveur (Principe C4-II).
 */
public record ReservationRequest(@NotNull Long hopitalId) {
}