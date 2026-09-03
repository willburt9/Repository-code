package com.openclassroom.projet11.adapter.in.web.error;

import com.openclassroom.projet11.domain.exception.HopitalNotFoundException;
import com.openclassroom.projet11.domain.exception.NoAvailableBedException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * GlobalExceptionHandlerTest
 *
 * Tests unitaires
 */
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void business_exception_hopitalNotFound_est_traduite_en_404_avec_son_code_metier() {
        ResponseEntity<ErrorResponse> reponse =
                handler.handleBusinessException(new HopitalNotFoundException("Cardiologie"));

        assertThat(reponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(reponse.getBody()).isNotNull();
        assertThat(reponse.getBody().getStatus()).isEqualTo(404);
        assertThat(reponse.getBody().getError()).isEqualTo("HOPITAL_NOT_FOUND");
        assertThat(reponse.getBody().getMessage()).contains("Cardiologie");
        assertThat(reponse.getBody().getTimestamp()).isNotNull();
    }

    @Test
    void business_exception_noAvailableBed_est_traduite_en_409_avec_son_code_metier() {
        ResponseEntity<ErrorResponse> reponse =
                handler.handleBusinessException(new NoAvailableBedException("Hôpital complet"));

        assertThat(reponse.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(reponse.getBody().getError()).isEqualTo("NO_AVAILABLE_BED");
        assertThat(reponse.getBody().getMessage()).isEqualTo("Hôpital complet");
    }

    @Test
    void illegalArgumentException_est_traduite_en_400_avec_le_message_original() {
        ResponseEntity<ErrorResponse> reponse =
                handler.handleIllegalArgumentException(new IllegalArgumentException("paramètre invalide"));

        assertThat(reponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(reponse.getBody().getError()).isEqualTo("Bad Request");
        assertThat(reponse.getBody().getMessage()).isEqualTo("paramètre invalide");
    }

    @Test
    void panne_du_fournisseur_de_distance_est_traduite_en_503_sans_fuite_du_detail_technique() {
        RestClientException erreurTechnique =
                new RestClientException("Connection refused: connect to 127.0.0.1:8989");

        ResponseEntity<ErrorResponse> reponse = handler.handleRestClientException(erreurTechnique);

        assertThat(reponse.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
        assertThat(reponse.getBody().getError()).isEqualTo("DISTANCE_PROVIDER_UNAVAILABLE");
        assertThat(reponse.getBody().getMessage()).doesNotContain("127.0.0.1");
    }

    /**
     * Filet de sécurité final : toute exception non prévue (bug, état
     * inattendu type "Aucun itinéraire trouvé") doit rester un 500 générique
     * côté client, sans jamais exposer le message d'exception brut.
     */
    @Test
    void exception_non_prevue_est_traduite_en_500_generique_sans_fuite_de_detail_technique() {
        ResponseEntity<ErrorResponse> reponse =
                handler.handleGenericException(new IllegalStateException("Aucun itinéraire trouvé par GraphHopper."));

        assertThat(reponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(reponse.getBody().getError()).isEqualTo("INTERNAL_ERROR");
        assertThat(reponse.getBody().getMessage()).doesNotContain("GraphHopper");
    }
}