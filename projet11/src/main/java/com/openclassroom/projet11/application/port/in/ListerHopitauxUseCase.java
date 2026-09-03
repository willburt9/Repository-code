package com.openclassroom.projet11.application.port.in;

import com.openclassroom.projet11.domain.model.Hopital;

import java.util.List;

public interface ListerHopitauxUseCase {

    List<Hopital> listerTous();
}