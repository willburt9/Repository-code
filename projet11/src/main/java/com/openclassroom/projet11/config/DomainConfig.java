package com.openclassroom.projet11.config;

import com.openclassroom.projet11.domain.port.out.DistanceProviderPort;
import com.openclassroom.projet11.domain.service.EmergencyRoutingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration Spring pour les beans du domaine.
 * DomainConfig
 */
@Configuration
public class DomainConfig {

    /**
     * Crée un bean pour le service de routage d'urgence.
     * @param distanceProviderPort
     * @return un bean EmergencyRoutingService
     */
    @Bean
    public EmergencyRoutingService emergencyRoutingService(DistanceProviderPort distanceProviderPort) {
        return new EmergencyRoutingService(distanceProviderPort);
    }
}