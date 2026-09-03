package com.openclassroom.projet11.domain.model;

import com.openclassroom.projet11.domain.exception.InvalidLocationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * LocationTest
 * Tests unitaires pour la classe Location.
 */
class LocationTest {

    @Test
    void accepte_des_coordonnees_valides() {
        Location location = new Location(48.8566, 2.3522);

        assertThat(location.latitude()).isEqualTo(48.8566);
        assertThat(location.longitude()).isEqualTo(2.3522);
    }

    @ParameterizedTest
    @ValueSource(doubles = {-91, 91, 1000})
    void rejette_une_latitude_hors_bornes(double latitudeInvalide) {
        assertThatThrownBy(() -> new Location(latitudeInvalide, 2.35))
                .isInstanceOf(InvalidLocationException.class);
    }

    @ParameterizedTest
    @ValueSource(doubles = {-181, 181, 1000})
    void rejette_une_longitude_hors_bornes(double longitudeInvalide) {
        assertThatThrownBy(() -> new Location(48.85, longitudeInvalide))
                .isInstanceOf(InvalidLocationException.class);
    }

    @Test
    void rejette_une_latitude_nan() {
        // Régression : NaN n'est ni < -90 ni > 90, les comparaisons de bornes
        // seules ne suffisaient pas à le détecter (cf. correction du point 5).
        assertThatThrownBy(() -> new Location(Double.NaN, 2.35))
                .isInstanceOf(InvalidLocationException.class);
    }

    @Test
    void rejette_une_longitude_nan() {
        assertThatThrownBy(() -> new Location(48.85, Double.NaN))
                .isInstanceOf(InvalidLocationException.class);
    }
}