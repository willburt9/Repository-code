package com.openclassroom.projet11.adapter.in.web;

import com.openclassroom.projet11.application.port.in.ReservationResultat;
import com.openclassroom.projet11.application.port.in.ReserverLitUseCase;
import com.openclassroom.projet11.domain.exception.NoAvailableBedException;
import com.openclassroom.projet11.domain.model.ReservationLit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * ReservationControllerTest
 * Teste le controller REST POST /reservations
 */
@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReserverLitUseCase reserverLitUseCase;

    @Test
    void reserver_un_lit_disponible_retourne_201_et_la_confirmation() throws Exception {
        ReservationLit reservation = ReservationLit.pour(6L);
        ReservationResultat resultat = new ReservationResultat(reservation, 8);

        when(reserverLitUseCase.reserver(6L)).thenReturn(resultat);

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"hopitalId\": 6}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.hopitalId").value(6))
                .andExpect(jsonPath("$.referencePatient").value(reservation.referencePatientAnonymisee()))
                .andExpect(jsonPath("$.litsDisponiblesRestants").value(8));
    }

    /**
     * Vérifie que le contrôleur traduit bien NoAvailableBedException (levée
     * quand un hôpital devient complet entre la recommandation et la
     * confirmation) en HTTP 409, conformément au statut porté par
     * l'exception métier elle-même (voir BusinessException / GlobalExceptionHandler).
     * Ce cas n'était vérifié qu'au niveau du service (ReserverLitServiceTest),
     * jamais au niveau du contrat HTTP exposé au front-end.
     */
    @Test
    void reserver_un_hopital_complet_retourne_409_avec_le_code_metier() throws Exception {
        when(reserverLitUseCase.reserver(anyLong()))
                .thenThrow(new NoAvailableBedException("Aucun lit disponible pour l'hôpital 1"));

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"hopitalId\": 1}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("NO_AVAILABLE_BED"));
    }

    /**
     * Documente le comportement ATTENDU de la validation d'entrée
     * (@Valid @NotNull sur ReservationRequest.hopitalId).
     *
     * ATTENTION — trou potentiel détecté à la lecture du code : GlobalExceptionHandler
     * ne déclare aucun @ExceptionHandler(MethodArgumentNotValidException.class),
     * seulement un handler générique (Exception.class). Si ce test échoue avec un
     * 500 au lieu d'un 400, cela confirme que la validation @Valid tombe dans le
     * handler générique au lieu du handler 400 attendu : il faut alors ajouter un
     * @ExceptionHandler(MethodArgumentNotValidException.class) dédié dans
     * GlobalExceptionHandler plutôt que d'affaiblir cette assertion.
     */
    @Test
    void reserver_sans_hopitalId_retourne_400() throws Exception {
        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}