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
 * Client HTTP permettant de communiquer avec une instance
 * locale de GraphHopper.
 *
 * <p>
 * Cette classe est uniquement responsable de l'appel HTTP
 * vers l'API GraphHopper.
 * </p>
 *
 * <p>
 * Elle ne contient aucune règle métier.
 * </p>
 */
@Component
public class GraphHopperClient {

    /**
     * Client HTTP Spring Framework 6.
     *
     * Remplace RestTemplate dans les applications Spring Boot 3.
     */
    private final RestClient restClient;

    /**
     * Constructeur.
     *
     * @param baseUrl           URL de base de l'API GraphHopper
     * @param connectTimeoutMs  délai maximal (ms) pour établir la connexion TCP
     * @param readTimeoutMs     délai maximal (ms) pour recevoir la réponse
     */
    public GraphHopperClient(
            @Value("${graphhopper.api.url}") String baseUrl,
            @Value("${graphhopper.connect-timeout-ms}") int connectTimeoutMs,
            @Value("${graphhopper.read-timeout-ms}") int readTimeoutMs) {

        // Sans timeout explicite, un appel GraphHopper qui ne répond pas bloque le
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
     * <p>
     * GraphHopper expose ici l'endpoint :
     *
     * <pre>
     * GET /route
     * </pre>
     *
     * avec deux paramètres {@code point}.
     * </p>
     *
     * @param depart localisation de départ
     * @param arrivee localisation d'arrivée
     * @return réponse GraphHopper
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
                        .get()
 
                        .uri(uriBuilder -> uriBuilder
                                .path("/route")
                                .queryParam(
                                        "point",
                                        requete.getPointDepart()
                                )
                                .queryParam(
                                        "point",
                                        requete.getPointArrivee()
                                )
                                .queryParam(
                                        "profile",
                                        requete.getProfile()
                                )
                                .queryParam(
                                        "locale",
                                        requete.getLocale()
                                )
                                .build()
                        )
                        .retrieve()
 
                        .body(DirectionsResponse.class);
 
        if (response == null) {
 
            throw new IllegalStateException(
                    "Aucune réponse reçue de GraphHopper."
            );
        }
 
        return response;
    }
}