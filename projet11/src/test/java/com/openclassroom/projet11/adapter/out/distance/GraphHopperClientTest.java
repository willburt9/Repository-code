package com.openclassroom.projet11.adapter.out.distance;

import com.openclassroom.projet11.adapter.out.distance.dto.DirectionsResponse;
import com.openclassroom.projet11.domain.model.Location;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires de {@link GraphHopperClient}.
 */
class GraphHopperClientTest {

    private HttpServer serveur;
    private GraphHopperClient client;

    private final AtomicReference<String> cheminRecu = new AtomicReference<>();
    private final AtomicReference<Map<String, List<String>>> queryParamsRecus = new AtomicReference<>();

    /** Corps JSON et code HTTP que le faux serveur doit renvoyer pour le prochain appel. */
    private volatile int codeReponseAEnvoyer = 200;
    private volatile String corpsAEnvoyer = "{\"paths\":[]}";

    @BeforeEach
    void demarrerServeur() throws IOException {
        serveur = HttpServer.create(new InetSocketAddress("localhost", 8989), 0);
        serveur.createContext("/route", this::gererRequete);
        serveur.start();

        int port = serveur.getAddress().getPort();
        client = new GraphHopperClient("http://localhost:" + port, 2000, 2000);
    }

    @AfterEach
    void arreterServeur() {
        serveur.stop(0);
    }

    private void gererRequete(HttpExchange exchange) throws IOException {
        cheminRecu.set(exchange.getRequestURI().getPath());
        queryParamsRecus.set(parserQuery(exchange.getRequestURI().getRawQuery()));

        byte[] corps = corpsAEnvoyer == null
                ? new byte[0]
                : corpsAEnvoyer.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().add("Content-Type", "application/json");

        if (corpsAEnvoyer == null) {
            // Simule une réponse sans corps (ex. 204 No Content)
            exchange.sendResponseHeaders(codeReponseAEnvoyer, -1);
        } else {
            exchange.sendResponseHeaders(codeReponseAEnvoyer, corps.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(corps);
            }
        }
        exchange.close();
    }

    /** Parse une query string en conservant l'ordre et les valeurs multiples pour une même clé (ex. "point"). */
    private Map<String, List<String>> parserQuery(String rawQuery) {
        Map<String, List<String>> resultat = new LinkedHashMap<>();
        if (rawQuery == null || rawQuery.isBlank()) {
            return resultat;
        }
        for (String paire : rawQuery.split("&")) {
            String[] cle_valeur = paire.split("=", 2);
            String cle = URLDecoder.decode(cle_valeur[0], StandardCharsets.UTF_8);
            String valeur = cle_valeur.length > 1
                    ? URLDecoder.decode(cle_valeur[1], StandardCharsets.UTF_8)
                    : "";
            resultat.computeIfAbsent(cle, k -> new ArrayList<>()).add(valeur);
        }
        return resultat;
    }

    @Test
    void calculerRoute_devraitEnvoyerLaBonneRequeteAGraphHopper() {
        Location depart = new Location(48.8566, 2.3522);   // Paris
        Location arrivee = new Location(45.7640, 4.8357);  // Lyon

        client.calculerRoute(depart, arrivee);

        assertEquals("/route", cheminRecu.get());

        Map<String, List<String>> params = queryParamsRecus.get();

        String pointDepartAttendu = depart.getLatitude() + "," + depart.getLongitude();
        String pointArriveeAttendu = arrivee.getLatitude() + "," + arrivee.getLongitude();

        assertEquals(List.of(pointDepartAttendu, pointArriveeAttendu), params.get("point"));
        assertEquals(List.of("car"), params.get("profile"));
        assertEquals(List.of("fr"), params.get("locale"));
    }

    @Test
    void calculerRoute_devraitRetournerLaReponseDeserialisee() {
        corpsAEnvoyer = "{\"paths\":[]}";

        DirectionsResponse reponse = client.calculerRoute(
                new Location(48.8566, 2.3522),
                new Location(45.7640, 4.8357));

        assertNotNull(reponse);
        assertNotNull(reponse.getRoutes());
        assertTrue(reponse.getRoutes().isEmpty());
    }

    @Test
    void calculerRoute_devraitLeverException_quandGraphHopperNeRepondRien() {
        codeReponseAEnvoyer = 204;
        corpsAEnvoyer = null; // pas de corps -> RestClient.body(...) renvoie null

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> client.calculerRoute(
                        new Location(48.8566, 2.3522),
                        new Location(45.7640, 4.8357)));

        assertEquals("Aucune réponse reçue de GraphHopper.", exception.getMessage());
    }
}