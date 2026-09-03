package com.openclassroom.projet11.application.usecase;

import com.openclassroom.projet11.application.port.in.GroupeAvecSpecialites;
import com.openclassroom.projet11.application.port.in.ListerSpecialitesUseCase;
import com.openclassroom.projet11.application.port.out.SpecialiteRepositoryPort;
import com.openclassroom.projet11.domain.model.GroupeSpecialite;
import com.openclassroom.projet11.domain.model.Specialite;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Service pour lister les spécialités par groupe.
 * ListerSpecialitesService
 */
@Service
public class ListerSpecialitesService implements ListerSpecialitesUseCase {

    private final SpecialiteRepositoryPort specialiteRepository;

    public ListerSpecialitesService(SpecialiteRepositoryPort specialiteRepository) {
        this.specialiteRepository = Objects.requireNonNull(specialiteRepository);
    }

    /**
     * @return la liste de tous les groupes avec leurs spécialités
     */
    @Override
    public List<GroupeAvecSpecialites> listerGroupes() {
        List<Specialite> toutesLesSpecialites = specialiteRepository.findAll();

        Map<GroupeSpecialite, List<Specialite>> parGroupe = toutesLesSpecialites.stream()
                .collect(Collectors.groupingBy(
                        Specialite::getGroupe,
                        LinkedHashMap::new,  
                        Collectors.toList()));

        return parGroupe.entrySet().stream()
                .map(entry -> new GroupeAvecSpecialites(entry.getKey(), entry.getValue()))
                .toList();
    }
}