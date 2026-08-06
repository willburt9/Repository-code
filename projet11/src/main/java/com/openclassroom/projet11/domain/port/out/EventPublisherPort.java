package com.openclassroom.projet11.domain.port.out;

import com.openclassroom.projet11.domain.model.ReservationLit;

/**
 * Port sortant de publication d'événements métier.
 * Implémentation PoC : journalisation uniquement,
 * destinée à être remplacée par un véritable bus d'événements.
 */
public interface EventPublisherPort {

    void publier(ReservationLit reservation);
}