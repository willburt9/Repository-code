package com.openclassroom.projet11.adapter.in.web;

import com.openclassroom.projet11.application.port.in.RecommanderHopitalUseCase;
import com.openclassroom.projet11.domain.exception.HopitalNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
}