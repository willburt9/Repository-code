package com.openclassroom.projet11.adapter.out.distance;

import com.openclassroom.projet11.adapter.out.distance.dto.DirectionsRequest;
import com.openclassroom.projet11.adapter.out.distance.dto.DirectionsResponse;
import com.openclassroom.projet11.domain.model.Location;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * Client HTTP permettant de communiquer avec l'API
 * OpenRouteService Directions.
 *
 * <p>
 * Cette classe est responsable uniquement de l'accès externe
 * à l'API OpenRouteService.
 * </p>
 *
 * <p>
 * Elle ne contient aucune règle métier.
 * La transformation des données en informations métier
 * est réalisée par l'adaptateur {@code DistanceProviderAdapter}.
 * </p>
 */
@Component
public class OpenRouteServiceClient {

    /**
     * Client HTTP Spring Framework 6.
     *
     * Remplace RestTemplate dans les applications Spring Boot 3.
     */
    private final RestClient restClient;

    /**
     * Clé API OpenRouteService.
     */
    private final String apiKey;

    /**
     * Constructeur.
     *
     * @param apiKey            clé permettant l'accès à l'API ORS
     * @param baseUrl           URL de base de l'API ORS
     * @param connectTimeoutMs  délai maximal (ms) pour établir la connexion TCP
     * @param readTimeoutMs     délai maximal (ms) pour recevoir la réponse
     */
    public OpenRouteServiceClient(
            @Value("${openrouteservice.api.key}") String apiKey,
            @Value("${openrouteservice.api.url}") String baseUrl,
            @Value("${openrouteservice.connect-timeout-ms}") int connectTimeoutMs,
            @Value("${openrouteservice.read-timeout-ms}") int readTimeoutMs) {

        this.apiKey = apiKey;

        // Sans timeout explicite, un appel ORS qui ne répond pas bloque le
        // thread de requête indéfiniment (risque d'épuisement du pool Tomcat
        // sous charge). Ces valeurs sont volontairement courtes : ce système
        // vise un temps de réponse global < 200 ms.
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(connectTimeoutMs);
        requestFactory.setReadTimeout(readTimeoutMs);

        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(requestFactory)
                .build();
    }

    /**
     * Calcule un itinéraire routier entre deux positions GPS.
     *
     * @param depart  localisation de départ
     * @param arrivee localisation d'arrivée
     * @return réponse OpenRouteService
     */
    /**
     * Calcule un itinéraire routier entre deux positions GPS.
     *
     * @param depart  localisation de départ
     * @param arrivee localisation d'arrivée
     * @return réponse OpenRouteService
     */
    public DirectionsResponse calculerRoute(
            Location depart,
            Location arrivee) {
 
        DirectionsRequest requete = DirectionsRequest.de(
                depart.getLongitude(), depart.getLatitude(),
                arrivee.getLongitude(), arrivee.getLatitude()
        );
 
        DirectionsResponse response =
                restClient
                        .post()
 
                        .uri("/v2/directions/driving-car")
 
                        /*
                         * Authentification OpenRouteService
                         */
                        .header(
                                "Authorization",
                                apiKey
                        )
 
                        .contentType(MediaType.APPLICATION_JSON)
 
                        .body(requete)
 
                        .retrieve()
 
                        .body(DirectionsResponse.class);
 
        if (response == null) {
 
            throw new IllegalStateException(
                    "Aucune réponse reçue d'OpenRouteService."
            );
        }
 
        return response;
    }
}