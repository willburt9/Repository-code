package com.openclassroom.projet11.adapter.out.distance;

import com.openclassroom.projet11.adapter.out.distance.dto.DirectionsResponse;
import com.openclassroom.projet11.domain.model.Location;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Value;
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
 * est réalisée par l'adaptateur {@code OpenRouteServiceDistanceAdapter}.
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
     * @param apiKey clé permettant l'accès à l'API ORS
     * @param baseUrl URL de base de l'API ORS
     */
    public OpenRouteServiceClient(
            @Value("${openrouteservice.api.key}") String apiKey,
            @Value("${openrouteservice.api.url}") String baseUrl) {

        this.apiKey = apiKey;

        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }


    /**
     * Calcule un itinéraire routier entre deux positions GPS.
     *
     * @param depart localisation de départ
     * @param arrivee localisation d'arrivée
     * @return réponse OpenRouteService
     */
    public DirectionsResponse calculerRoute(
            Location depart,
            Location arrivee) {


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

                        /*
                        * Indique que le corps envoyé est du JSON.
                        */
                        .contentType(MediaType.APPLICATION_JSON)

                        /*
                        * Corps attendu par OpenRouteService :
                        *
                        * coordinates :
                        * [
                        *   [longitude, latitude],
                        *   [longitude, latitude]
                        * ]
                        */
                        .body("""
                                {
                                "coordinates": [
                                    [
                                    %s,
                                    %s
                                    ],
                                    [
                                    %s,
                                    %s
                                    ]
                                ]
                                }
                                """.formatted(
                                        depart.getLongitude(),
                                        depart.getLatitude(),
                                        arrivee.getLongitude(),
                                        arrivee.getLatitude()
                                ))

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