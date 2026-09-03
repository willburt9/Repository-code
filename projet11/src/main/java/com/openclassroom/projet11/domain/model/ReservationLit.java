package com.openclassroom.projet11.domain.model;

 
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
 
/**
 * Événement métier "réservation de lit", publié lorsqu'un hôpital est retenu
 * suite à une recommandation.
 * La référence patient est générée ici, jamais reçue du client : aucune
 * donnée patient réelle n'entre jamais dans le système.
 */
public record ReservationLit(Long hopitalId, String referencePatientAnonymisee, Instant horodatage) {
 
    public ReservationLit {
        Objects.requireNonNull(hopitalId, "L'identifiant de l'hôpital est obligatoire.");
        Objects.requireNonNull(referencePatientAnonymisee, "La référence patient anonymisée est obligatoire.");
        Objects.requireNonNull(horodatage, "L'horodatage est obligatoire.");
    }
 
    /**
     * Crée une réservation pour l'hôpital donné, en générant une référence
     * patient anonymisée côté serveur (jamais fournie par l'appelant).
     */
    public static ReservationLit pour(Long hopitalId) {
        return new ReservationLit(hopitalId, "PAT-" + UUID.randomUUID(), Instant.now());
    }
}
 