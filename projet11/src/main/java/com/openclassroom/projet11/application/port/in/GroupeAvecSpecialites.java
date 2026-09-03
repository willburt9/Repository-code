package com.openclassroom.projet11.application.port.in;

import com.openclassroom.projet11.domain.model.GroupeSpecialite;
import com.openclassroom.projet11.domain.model.Specialite;

import java.util.List;

/**
 * Port d'entrée pour lister les spécialités d'un groupe.
 * GroupeAvecSpecialites
 */
public record GroupeAvecSpecialites(GroupeSpecialite groupe, List<Specialite> specialites) {
}