import { describe, it, expect, vi, beforeEach } from "vitest";
import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { UrgencesPage } from "./UrgencesPage";
import { fetchSpecialites } from "../api/specialites";
import { fetchRecommandation } from "../api/recommandations";
import { reserverLit } from "../api/reservations";
import { ApiError } from "../api/client";

// On mocke la couche API : la page ne doit dépendre que de ces fonctions
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

  it("affiche une erreur si le chargement des spécialités échoue", async () => {
    vi.mocked(fetchSpecialites).mockRejectedValueOnce(new ApiError("Chargement impossible", 500));

    render(<UrgencesPage />);

    expect(await screen.findByText("Chargement impossible")).toBeInTheDocument();
  });

  it("trie les spécialités par ordre alphabétique dans la liste déroulante", async () => {
    vi.mocked(fetchSpecialites).mockResolvedValueOnce([
      {
        id: 1,
        nom: "Médecine",
        specialites: [
          { id: 20, nom: "Neurologie" },
          { id: 10, nom: "Cardiologie" },
        ],
      },
    ]);

    render(<UrgencesPage />);
    await waitFor(() => expect(fetchSpecialites).toHaveBeenCalled());

    await userEvent.click(screen.getByLabelText(/spécialité requise/i));
    const options = await screen.findAllByRole("option");

    expect(options[0]).toHaveTextContent("Cardiologie");
    expect(options[1]).toHaveTextContent("Neurologie");
  });

  it("permet de modifier manuellement la latitude et la longitude", async () => {
    render(<UrgencesPage />);
    await waitFor(() => expect(fetchSpecialites).toHaveBeenCalled());

    const champLatitude = screen.getByLabelText(/latitude gps/i);
    const champLongitude = screen.getByLabelText(/longitude gps/i);

    await userEvent.clear(champLatitude);
    await userEvent.type(champLatitude, "45.75");
    await userEvent.clear(champLongitude);
    await userEvent.type(champLongitude, "4.85");

    expect(champLatitude).toHaveValue("45.75");
    expect(champLongitude).toHaveValue("4.85");
  });

  it("récupère la position GPS de l'utilisateur au clic sur géolocaliser", async () => {
    const getCurrentPosition = vi.fn((success: PositionCallback) => {
      success({
        coords: { latitude: 45.75889, longitude: 4.84139 },
      } as GeolocationPosition);
    });
    Object.defineProperty(window.navigator, "geolocation", {
      value: { getCurrentPosition },
      configurable: true,
    });

    render(<UrgencesPage />);
    await waitFor(() => expect(fetchSpecialites).toHaveBeenCalled());

    await userEvent.click(screen.getByTitle("Me géolocaliser"));

    expect(getCurrentPosition).toHaveBeenCalled();
    expect(screen.getByLabelText(/latitude gps/i)).toHaveValue("45.7589");
    expect(screen.getByLabelText(/longitude gps/i)).toHaveValue("4.8414");
  });

  it("affiche un indicateur de chargement pendant la recherche", async () => {
    let resoudre: (value: typeof recommandation) => void = () => {};
    vi.mocked(fetchRecommandation).mockReturnValueOnce(
      new Promise((resolve) => {
        resoudre = resolve;
      })
    );

    render(<UrgencesPage />);
    await waitFor(() => expect(fetchSpecialites).toHaveBeenCalled());
    await userEvent.click(screen.getByLabelText(/spécialité requise/i));
    await userEvent.click(await screen.findByRole("option", { name: /Cardiologie/i }));
    await userEvent.click(screen.getByRole("button", { name: /trouver/i }));

    expect(screen.getByRole("button", { name: /trouver/i })).toBeDisabled();

    resoudre(recommandation);
    await screen.findByText("Hôpital Bichat");
  });

  it("permet de fermer le message d'erreur affiché", async () => {
    render(<UrgencesPage />);
    await waitFor(() => expect(fetchSpecialites).toHaveBeenCalled());

    await userEvent.click(screen.getByRole("button", { name: /trouver/i }));
    await screen.findByText(/sélectionne une spécialité/i);

    const boutonFermer = screen.getByRole("button", { name: /close/i });
    await userEvent.click(boutonFermer);

    await waitFor(() =>
      expect(screen.queryByText(/sélectionne une spécialité/i)).not.toBeInTheDocument()
    );
  });

  it("affiche l'état initial invitant à lancer une recherche", async () => {
    render(<UrgencesPage />);

    expect(screen.getByText("Prêt à rechercher")).toBeInTheDocument();
  });

  it("ferme la notification de confirmation après réservation", async () => {
    vi.mocked(fetchRecommandation).mockResolvedValueOnce(recommandation);
    vi.mocked(reserverLit).mockResolvedValueOnce({
      hopitalId: 7,
      horodatage: "2026-08-16 20:50:43.37",
      referencePatient: "PAT-338ef318-3dd5-47b4-b069-9ad86c344148",
      litsDisponiblesRestants: 3,
    });

    await selectionnerSpecialiteEtChercher();
    await screen.findByText("Hôpital Bichat");

    await userEvent.click(screen.getByRole("button", { name: /réserver un lit/i }));
    await screen.findByText(/PAT-338ef318-3dd5-47b4-b069-9ad86c344148/);

    await userEvent.keyboard("{Escape}");

    await waitFor(() =>
      expect(screen.queryByText(/PAT-338ef318-3dd5-47b4-b069-9ad86c344148/)).not.toBeInTheDocument()
    );
  });
});
