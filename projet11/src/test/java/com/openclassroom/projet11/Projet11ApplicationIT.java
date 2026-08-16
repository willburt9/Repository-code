package com.openclassroom.projet11;

import com.openclassroom.projet11.adapter.in.web.dto.HopitalResponse;
import com.openclassroom.projet11.adapter.in.web.dto.RecommandationResponse;
import com.openclassroom.projet11.adapter.in.web.dto.ReservationRequest;
import com.openclassroom.projet11.adapter.in.web.dto.ReservationResponse;
import com.openclassroom.projet11.domain.port.out.DistanceProviderPort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Test de bout en bout : vraie base H2 (schema.sql/data.sql), vrais
 * controllers, vrai routage HTTP via un port aléatoire.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class Projet11ApplicationIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockitoBean
    private DistanceProviderPort distanceProvider;

    @Test
    void parcours_complet_recommandation_puis_reservation() {
        when(distanceProvider.calculerDistance(any(), any())).thenReturn(1.5);
        when(distanceProvider.calculerTempsTrajet(any(), any())).thenReturn(4.0);

        // 1. GET /hopitaux
        ResponseEntity<HopitalResponse[]> hopitaux =
                restTemplate.getForEntity("/hopitaux", HopitalResponse[].class);
        assertThat(hopitaux.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(hopitaux.getBody()).hasSize(10);

        // 2. GET /recommandations (Cardiologie, id 121, cf. data.sql)
        String url = "/recommandations?latitude=48.8383&longitude=2.3651&specialiteId=121";
        ResponseEntity<RecommandationResponse> recommandation =
                restTemplate.getForEntity(url, RecommandationResponse.class);
        assertThat(recommandation.getStatusCode()).isEqualTo(HttpStatus.OK);
        int litsAvantReservation = recommandation.getBody().litsDisponibles();

        // 3. POST /reservations sur l'hôpital recommandé
        ReservationRequest requete = new ReservationRequest(recommandation.getBody().hopitalId());
        ResponseEntity<ReservationResponse> reservation =
                restTemplate.postForEntity("/reservations", requete, ReservationResponse.class);

        assertThat(reservation.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(reservation.getBody().litsDisponiblesRestants()).isEqualTo(litsAvantReservation - 1);
    }
}