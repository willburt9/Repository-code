package com.openclassroom.projet11.adapter.out.event;

import com.openclassroom.projet11.domain.model.ReservationLit;
import com.openclassroom.projet11.domain.port.out.EventPublisherPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Adaptateur factice du port de publication d'événements : journalise la
 * réservation (cf. Définition de l'architecture §9.c), en attendant un
 * véritable bus d'événements hors du périmètre de la PoC.
 */
@Component
public class EventPublisherAdapter implements EventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(EventPublisherAdapter.class);

    @Override
    public void publier(ReservationLit reservation) {
        log.info("Réservation de lit publiée : hopitalId={}, référence={}, horodatage={}",
                reservation.hopitalId(), reservation.referencePatientAnonymisee(), reservation.horodatage());
    }
}