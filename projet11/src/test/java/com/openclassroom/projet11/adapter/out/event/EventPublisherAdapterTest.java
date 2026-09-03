package com.openclassroom.projet11.adapter.out.event;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.openclassroom.projet11.domain.model.ReservationLit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires de {@link EventPublisherAdapter}.
 */
class EventPublisherAdapterTest {

    private final EventPublisherAdapter adapter = new EventPublisherAdapter();

    private Logger logger;
    private ListAppender<ILoggingEvent> appender;

    @BeforeEach
    void demarrerCaptureDesLogs() {
        logger = (Logger) LoggerFactory.getLogger(EventPublisherAdapter.class);
        appender = new ListAppender<>();
        appender.start();
        logger.addAppender(appender);
    }

    @AfterEach
    void arreterCaptureDesLogs() {
        logger.detachAppender(appender);
    }

    @Test
    void publier_devraitJournaliserLesInformationsDeLaReservation() {
        Instant horodatage = Instant.parse("2026-01-15T10:30:00Z");
        ReservationLit reservation = new ReservationLit(42L, "PAT-abc123", horodatage);

        adapter.publier(reservation);

        assertEquals(1, appender.list.size());

        ILoggingEvent evenement = appender.list.get(0);
        assertEquals(Level.INFO, evenement.getLevel());
        assertArrayEquals(
                new Object[]{42L, "PAT-abc123", horodatage},
                evenement.getArgumentArray());
    }

    @Test
    void publier_devraitLeverException_quandReservationEstNull() {
        assertThrows(NullPointerException.class, () -> adapter.publier(null));
    }
}