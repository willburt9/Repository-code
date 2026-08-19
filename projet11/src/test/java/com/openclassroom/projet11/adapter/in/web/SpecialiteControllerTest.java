package com.openclassroom.projet11.adapter.in.web;

import com.openclassroom.projet11.application.port.in.GroupeAvecSpecialites;
import com.openclassroom.projet11.application.port.in.ListerSpecialitesUseCase;
import com.openclassroom.projet11.domain.model.GroupeSpecialite;
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
 * SpecialiteControllerTest
 * Teste le controller REST GET /specialites
 */
@WebMvcTest(SpecialiteController.class)
class SpecialiteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ListerSpecialitesUseCase listerSpecialitesUseCase;

    @Test
    void get_specialites_retourne_200_et_les_groupes_avec_leurs_specialites() throws Exception {
        GroupeSpecialite groupe = new GroupeSpecialite(5L, "Groupe de médecine générale");
        Specialite cardiologie = new Specialite(121L, "Cardiologie", groupe);
        Specialite pediatrie = new Specialite(122L, "Pédiatrie", groupe);

        when(listerSpecialitesUseCase.listerGroupes())
                .thenReturn(List.of(new GroupeAvecSpecialites(groupe, List.of(cardiologie, pediatrie))));

        mockMvc.perform(get("/specialites"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(5))
                .andExpect(jsonPath("$[0].nom").value("Groupe de médecine générale"))
                .andExpect(jsonPath("$[0].specialites[0].nom").value("Cardiologie"))
                .andExpect(jsonPath("$[0].specialites[1].nom").value("Pédiatrie"));
    }

    @Test
    void get_specialites_retourne_une_liste_vide_si_aucun_groupe_reference() throws Exception {
        when(listerSpecialitesUseCase.listerGroupes()).thenReturn(List.of());

        mockMvc.perform(get("/specialites"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }
}