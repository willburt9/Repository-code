package com.openclassroom.projet11.application.usecase;

import com.openclassroom.projet11.application.port.in.ListerHopitauxUseCase;
import com.openclassroom.projet11.application.port.out.HopitalRepositoryPort;
import com.openclassroom.projet11.domain.model.Hopital;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * Service pour lister tous les hôpitaux.
 * ListerHopitauxService
 */
@Service
public class ListerHopitauxService implements ListerHopitauxUseCase {

    private final HopitalRepositoryPort hopitalRepository;

    public ListerHopitauxService(HopitalRepositoryPort hopitalRepository) {
        this.hopitalRepository = Objects.requireNonNull(hopitalRepository);
    }

    /**
     * Récupère la liste de tous les hôpitaux.
     *
     * @return liste des hôpitaux
     */
    @Override
    public List<Hopital> listerTous() {
        return hopitalRepository.findAll();
    }
}