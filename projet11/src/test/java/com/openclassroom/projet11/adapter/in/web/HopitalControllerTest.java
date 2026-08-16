package com.openclassroom.projet11.adapter.in.web;

import com.openclassroom.projet11.application.port.in.ListerHopitauxUseCase;
import com.openclassroom.projet11.domain.model.GroupeSpecialite;
import com.openclassroom.projet11.domain.model.Hopital;
import com.openclassroom.projet11.domain.model.Location;
import com.openclassroom.projet11.domain.model.Specialite;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * HopitalControllerTest
 * Teste le controller REST : pas de base ni appel externe, le use case est simulé.
 */
@WebMvcTest(HopitalController.class)
class HopitalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ListerHopitauxUseCase listerHopitauxUseCase;

    @Test
    void get_hopitaux_retourne_200_et_la_liste_serialisee() throws Exception {
        GroupeSpecialite groupe = new GroupeSpecialite(5L, "Groupe de médecine générale");
        Specialite cardiologie = new Specialite(121L, "Cardiologie", groupe);
        Hopital hopital = new Hopital(1L, "Hôpital Test", "1 rue de Test",
                new Location(48.85, 2.35), List.of(cardiologie), 3);

        when(listerHopitauxUseCase.listerTous()).thenReturn(List.of(hopital));

        mockMvc.perform(get("/hopitaux"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom").value("Hôpital Test"))
                .andExpect(jsonPath("$[0].litsDisponibles").value(3))
                .andExpect(jsonPath("$[0].specialites[0]").value("Cardiologie"));
    }
}