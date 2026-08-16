import { describe, it, expect, vi, beforeEach } from "vitest";
import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { UrgencesPage } from "./UrgencesPage";
import { fetchSpecialites } from "../api/specialites";
import { fetchRecommandation } from "../api/recommandations";
import { reserverLit } from "../api/reservations";
import { ApiError } from "../api/client";

// On mocke la couche API : la page ne doit dépendre que de ces fonctions,
// pas de l'implémentation réseau sous-jacente.
vi.mock("../api/specialites");
vi.mock("../api/recommandations");
vi.mock("../api/reservations");

const groupes = [
  { id: 1, nom: "Médecine", specialites: [{ id: 10, nom: "Cardiologie" }] },
];

const recommandation = {
    hopitalId: 1,
    nom: "Hôpital Bichat",
    adresse: "46 Rue Henri Huchard, Paris",
    latitude:  48.86056,
    longitude: 2.3367934,
    litsDisponibles: 4,
    distanceKm: 3.2,
    dureeMinutes: 12,
};

async function selectionnerSpecialiteEtChercher() {
  render(<UrgencesPage />);
  await waitFor(() => expect(fetchSpecialites).toHaveBeenCalled());
  await userEvent.click(screen.getByLabelText(/spécialité requise/i));
  await userEvent.click(await screen.findByRole("option", { name: /Cardiologie/i }));
  await userEvent.click(screen.getByRole("button", { name: /trouver/i }));
}

beforeEach(() => {
  vi.mocked(fetchSpecialites).mockResolvedValue(groupes);
});

describe("UrgencesPage", () => {
  it("charge la liste des spécialités au montage", async () => {
    render(<UrgencesPage />);
    await waitFor(() => expect(fetchSpecialites).toHaveBeenCalledTimes(1));
  });

  it("affiche une erreur si on cherche sans avoir choisi de spécialité", async () => {
    render(<UrgencesPage />);
    await waitFor(() => expect(fetchSpecialites).toHaveBeenCalled());

    await userEvent.click(screen.getByRole("button", { name: /trouver/i }));

    expect(await screen.findByText(/sélectionne une spécialité/i)).toBeInTheDocument();
  });

  it("affiche le résultat après une recherche réussie", async () => {
    vi.mocked(fetchRecommandation).mockResolvedValueOnce(recommandation);

    await selectionnerSpecialiteEtChercher();

    expect(await screen.findByText("Hôpital Bichat")).toBeInTheDocument();
    expect(fetchRecommandation).toHaveBeenCalledWith(
      expect.objectContaining({ specialiteId: 10 })
    );
  });

  it("affiche le message renvoyé par le backend si la recherche échoue", async () => {
    vi.mocked(fetchRecommandation).mockRejectedValueOnce(
      new ApiError("Aucun hôpital trouvé", 404)
    );

    await selectionnerSpecialiteEtChercher();

    expect(await screen.findByText("Aucun hôpital trouvé")).toBeInTheDocument();
  });

  it("affiche un message générique si l'erreur n'est pas une ApiError", async () => {
    // UrgencesPage fait un `instanceof ApiError` : toute autre erreur
    // (ex. exception JS inattendue) doit retomber sur le message générique.
    vi.mocked(fetchRecommandation).mockRejectedValueOnce(new Error("boom"));

    await selectionnerSpecialiteEtChercher();

    expect(await screen.findByText("Une erreur est survenue.")).toBeInTheDocument();
  });

  it("réserve un lit et affiche la confirmation avec la référence patient", async () => {
    vi.mocked(fetchRecommandation).mockResolvedValueOnce(recommandation);
    vi.mocked(reserverLit).mockResolvedValueOnce({
        hopitalId:7,
        horodatage: "2026-08-16 20:50:43.37",
        referencePatient: "PAT-338ef318-3dd5-47b4-b069-9ad86c344148",
        litsDisponiblesRestants: 3,
    });

    await selectionnerSpecialiteEtChercher();
    await screen.findByText("Hôpital Bichat");

    await userEvent.click(screen.getByRole("button", { name: /réserver un lit/i }));

    expect(await screen.findByText(/PAT-338ef318-3dd5-47b4-b069-9ad86c344148/)).toBeInTheDocument();
  });

  it("affiche une erreur si la réservation échoue", async () => {
    vi.mocked(fetchRecommandation).mockResolvedValueOnce(recommandation);
    vi.mocked(reserverLit).mockRejectedValueOnce(new ApiError("Plus aucun lit disponible", 409));

    await selectionnerSpecialiteEtChercher();
    await screen.findByText("Hôpital Bichat");

    await userEvent.click(screen.getByRole("button", { name: /réserver un lit/i }));

    expect(await screen.findByText("Plus aucun lit disponible")).toBeInTheDocument();
  });
});
