package com.openclassroom.projet11.application.usecase;

import com.openclassroom.projet11.application.port.in.ReservationResultat;
import com.openclassroom.projet11.application.port.in.ReserverLitUseCase;
import com.openclassroom.projet11.application.port.out.HopitalRepositoryPort;
import com.openclassroom.projet11.domain.model.Hopital;
import com.openclassroom.projet11.domain.model.ReservationLit;
import com.openclassroom.projet11.domain.port.out.EventPublisherPort;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Orchestre la réservation : charge l'hôpital, délègue la règle métier
 * (lit disponible ?) à {@link Hopital#reserverLit()}, persiste le nouveau
 * nombre de lits, puis publie l'événement de réservation.
 */
@Service
public class ReserverLitService implements ReserverLitUseCase {

    private final HopitalRepositoryPort hopitalRepository;
    private final EventPublisherPort eventPublisher;

    public ReserverLitService(HopitalRepositoryPort hopitalRepository, EventPublisherPort eventPublisher) {
        this.hopitalRepository = Objects.requireNonNull(hopitalRepository);
        this.eventPublisher = Objects.requireNonNull(eventPublisher);
    }

    @Override
    public ReservationResultat reserver(Long hopitalId) {
        Hopital hopital = hopitalRepository.findById(hopitalId);

        hopital.reserverLit(); // règle métier : lève NoAvailableBedException si complet

        hopitalRepository.mettreAJourLitsDisponibles(hopitalId, hopital.getLitsDisponibles());

        ReservationLit reservation = ReservationLit.pour(hopitalId);
        eventPublisher.publier(reservation);

        return new ReservationResultat(reservation, hopital.getLitsDisponibles());
    }
}