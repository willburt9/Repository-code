package com.openclassroom.projet11.adapter.out.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Point d'accès unique au logger d'audit métier ("AUDIT"), configuré dans
 * logback-spring.xml pour écrire dans un fichier journalier dédié
 * (logs/audit-yyyy-MM-dd.log), séparé des logs techniques Spring/Tomcat.
 * <p>
 * Répond à l'exigence "Journalisation structurée des recommandations et des
 * événements de réservation" de la checklist de conformité (Plan de test BDD §5).
 * <p>
 * Format des messages en clé=valeur (logfmt), facilement grep-able/parsable :
 * {@code event=recommandation.demande latitude=48.83 longitude=2.36 specialiteId=121}
 */
public final class AuditLog {

    public static final Logger LOGGER = LoggerFactory.getLogger("AUDIT");

    private AuditLog() {
    }
}
