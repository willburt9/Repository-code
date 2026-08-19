package com.openclassroom.projet11.adapter.out.distance;

import com.openclassroom.projet11.adapter.out.distance.dto.DirectionsResponse;
import com.openclassroom.projet11.adapter.out.distance.dto.Route;
import com.openclassroom.projet11.domain.model.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
 
import java.util.Collections;
import java.util.List;
 
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
 
/**
 * DistanceProviderAdapterTest
 *
 * Test : "Vérifier que la distance retournée
 * correspond à un itinéraire routier simulé et non à une distance
 * euclidienne" 
 */
@ExtendWith(MockitoExtension.class)
class DistanceProviderAdapterTest {
 
    @Mock
    private GraphHopperClient client;
 
    private DistanceProviderAdapter adapter;
 
    private final Location depart = new Location(48.8383, 2.3651);
    private final Location arrivee = new Location(48.8390, 2.3660);
 
    @BeforeEach
    void setUp() {
        // TTL de cache large pour ne pas dépendre du temps d'exécution du test.
        adapter = new DistanceProviderAdapter(client, 300);
    }
 
    @Test
    void calculerDistance_convertit_les_metres_de_graphhopper_en_kilometres() {
        when(client.calculerRoute(depart, arrivee)).thenReturn(unItineraire(12345.0, 600_000));
 
        double distanceKm = adapter.calculerDistance(depart, arrivee);
 
        assertThat(distanceKm).isEqualTo(12.345);
    }
 
    @Test
    void calculerTempsTrajet_convertit_les_millisecondes_de_graphhopper_en_minutes() {
        when(client.calculerRoute(depart, arrivee)).thenReturn(unItineraire(12345.0, 600_000));
 
        double dureeMinutes = adapter.calculerTempsTrajet(depart, arrivee);
 
        assertThat(dureeMinutes).isEqualTo(10.0);
    }
 
    /**
     * Protection anti-quota : un même trajet demandé deux fois (typiquement
     * calculerDistance() puis calculerTempsTrajet() pour le même hôpital
     * retenu, comme le fait RecommanderHopitalService) ne doit déclencher
     * qu'un seul appel réseau vers GraphHopper.
     */
    @Test
    void un_meme_trajet_demande_deux_fois_ne_declenche_quun_seul_appel_a_graphhopper() {
        when(client.calculerRoute(depart, arrivee)).thenReturn(unItineraire(5000.0, 300_000));
 
        adapter.calculerDistance(depart, arrivee);
        adapter.calculerTempsTrajet(depart, arrivee);
 
        verify(client, times(1)).calculerRoute(depart, arrivee);
    }
 
    @Test
    void deux_trajets_differents_declenchent_bien_deux_appels_distincts() {
        Location autreArrivee = new Location(48.9000, 2.4200);
        when(client.calculerRoute(depart, arrivee)).thenReturn(unItineraire(5000.0, 300_000));
        when(client.calculerRoute(depart, autreArrivee)).thenReturn(unItineraire(9000.0, 400_000));
 
        adapter.calculerDistance(depart, arrivee);
        adapter.calculerDistance(depart, autreArrivee);
 
        verify(client, times(1)).calculerRoute(depart, arrivee);
        verify(client, times(1)).calculerRoute(depart, autreArrivee);
    }
 
    @Test
    void aucun_itineraire_retourne_par_graphhopper_leve_illegalStateException() {
        DirectionsResponse reponseVide = new DirectionsResponse();
        reponseVide.setRoutes(Collections.emptyList());
        when(client.calculerRoute(depart, arrivee)).thenReturn(reponseVide);
 
        assertThatThrownBy(() -> adapter.calculerDistance(depart, arrivee))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Aucun itinéraire trouvé");
    }
 
    private DirectionsResponse unItineraire(double distanceMetres, long dureeMillisecondes) {
        Route route = new Route();
        route.setDistance(distanceMetres);
        route.setTime(dureeMillisecondes);
 
        DirectionsResponse reponse = new DirectionsResponse();
        reponse.setRoutes(List.of(route));
        return reponse;
    }
}