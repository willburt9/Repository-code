package com.openclassroom.projet11.adapter.in.web;

import com.openclassroom.projet11.application.port.in.RecommandationResultat;
import com.openclassroom.projet11.application.port.in.RecommanderHopitalUseCase;
import com.openclassroom.projet11.domain.exception.HopitalNotFoundException;
import com.openclassroom.projet11.domain.model.GroupeSpecialite;
import com.openclassroom.projet11.domain.model.Hopital;
import com.openclassroom.projet11.domain.model.Location;
import com.openclassroom.projet11.domain.model.Specialite;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

/**
 * RecommandationControllerTest
 * Test du controller REST : pas de base ni appel externe, le use case est simulé.
 */
@WebMvcTest(RecommandationController.class)
class RecommandationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RecommanderHopitalUseCase recommanderHopitalUseCase;

    private Hopital unHopital() {
        GroupeSpecialite groupe = new GroupeSpecialite(5L, "Groupe de médecine générale");
        Specialite cardiologie = new Specialite(121L, "Cardiologie", groupe);
        return new Hopital(
                1L,
                "Hôpital Cochin",
                "12 rue de la Santé",
                new Location(48.85, 2.35),
                List.of(cardiologie),
                2
        );
    }
 
    @Test
    void cas_nominal_retourne_200_et_le_corps_attendu() throws Exception {
        RecommandationResultat resultat = new RecommandationResultat(unHopital(), 3.456, 6.789);
        when(recommanderHopitalUseCase.recommander(any(), anyLong())).thenReturn(resultat);
 
        mockMvc.perform(get("/recommandations")
                        .param("latitude", "48.85")
                        .param("longitude", "2.35")
                        .param("specialiteId", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hopitalId").value(1))
                .andExpect(jsonPath("$.nom").value("Hôpital Cochin"))
                .andExpect(jsonPath("$.adresse").value("12 rue de la Santé"))
                .andExpect(jsonPath("$.litsDisponibles").value(2))
                .andExpect(jsonPath("$.distanceKm").value(3.46))
                .andExpect(jsonPath("$.dureeMinutes").value(6.79));
    }
    
    @Test
    void aucun_hopital_eligible_retourne_404() throws Exception {
        when(recommanderHopitalUseCase.recommander(any(), anyLong()))
                .thenThrow(new HopitalNotFoundException("Neurochirurgie"));

        mockMvc.perform(get("/recommandations")
                        .param("latitude", "48.8383")
                        .param("longitude", "2.3651")
                        .param("specialiteId", "173"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("HOPITAL_NOT_FOUND"));
    }

    @Test
    void latitude_hors_bornes_retourne_400_sans_appeler_le_use_case() throws Exception {

        mockMvc.perform(get("/recommandations")
                        .param("latitude", "1000")
                        .param("longitude", "2.3651")
                        .param("specialiteId", "121"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void longitude_hors_bornes_retourne_400_sans_appeler_le_use_case() throws Exception {
 
        mockMvc.perform(get("/recommandations")
                        .param("latitude", "48.8383")
                        .param("longitude", "1000")
                        .param("specialiteId", "121"))
                .andExpect(status().isBadRequest());
 
        verifyNoInteractions(recommanderHopitalUseCase);
    }
 
    @Test
    void latitude_manquante_retourne_400_sans_appeler_le_use_case() throws Exception {
 
        mockMvc.perform(get("/recommandations")
                        .param("longitude", "2.3651")
                        .param("specialiteId", "121"))
                .andExpect(status().isBadRequest());
 
        verifyNoInteractions(recommanderHopitalUseCase);
    }
 
    @Test
    void specialiteId_manquant_retourne_400_sans_appeler_le_use_case() throws Exception {
 
        mockMvc.perform(get("/recommandations")
                        .param("latitude", "48.8383")
                        .param("longitude", "2.3651"))
                .andExpect(status().isBadRequest());
 
        verifyNoInteractions(recommanderHopitalUseCase);
    }
}