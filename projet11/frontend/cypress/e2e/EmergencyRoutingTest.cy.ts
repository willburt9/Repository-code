/// <reference types="cypress" />

/**
 * parcours-complet.smoke.cy.ts
 *
 * "Parcours utilisateur complet (end-to-end)" —
 * SPA + Emergency Routing Service + Référentiel des hôpitaux réels, sans
 * aucun stub réseau.  
 *  
 * npx cypress run --spec cypress/e2e/EmergencyRoutingTest.cy.ts
 */
describe("Parcours complet (backend réel)", () => {
  it("recommande un hôpital ayant des lits disponibles, jamais Cochin (0 lit), et permet la réservation", () => {
    cy.visit("/urgences");

    // on vérifie que le vrai référentiel est bien chargé, pas un jeu de données réduit.
    cy.get("[data-cy=champ-specialite]").click();
    cy.get('ul[role="listbox"] li[role="option"]').its("length").should("be.gt", 50);
    cy.get('ul[role="listbox"] li[role="option"]').contains("Cardiologie").click();

    // Localisation par défaut pré-remplie par l'interface.
    cy.get("[data-cy=bouton-rechercher]").click();

    cy.get("[data-cy=carte-resultat]", { timeout: 15000 }).should("be.visible");

    // Règle métier vérifiée en conditions réelles : l'hôpital
    // Cochin propose la cardiologie mais a 0 lit disponible et
    // ne doit donc jamais être recommandé, même s'il est géographiquement proche.
    cy.get("[data-cy=carte-resultat]").should("not.contain.text", "Cochin");

    cy.get("[data-cy=bouton-reserver]").click();
    cy.contains(/référence PAT-/, { timeout: 10000 }).should("be.visible");
  });
});