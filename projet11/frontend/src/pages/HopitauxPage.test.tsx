import { describe, it, expect, vi, beforeEach, afterEach } from "vitest";
import { render, screen, waitFor, within, cleanup } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { HopitauxPage } from "./HopitauxPage";
import { fetchHopitaux } from "../api/hopitaux";

vi.mock("../api/hopitaux");

const hopitaux = [
  {
    id: 1,
    nom: "Hôpital Bichat",
    adresse: "46 Rue Henri Huchard, Paris",
    latitude: 48.86056,
    longitude: 2.3367934,
    litsDisponibles: 8,
    specialites: ["Cardiologie", "Anesthésie"],
  },
  {
    id: 2,
    nom: "Hôpital Cochin",
    adresse: "Paris",
    latitude: 48.86056,
    longitude: 2.3367934,
    litsDisponibles: 2,
    specialites: ["Urologie"],
  },
];

beforeEach(() => {
  vi.mocked(fetchHopitaux).mockResolvedValue(hopitaux);
});

describe("HopitauxPage", () => {
  it("affiche les hôpitaux renvoyés par l'API dans la grille", async () => {
    render(<HopitauxPage />);

    await waitFor(() => expect(screen.getByText("Hôpital Bichat")).toBeInTheDocument());
    expect(screen.getByText("Hôpital Cochin")).toBeInTheDocument();
  });

  it("calcule correctement le total de lits disponibles", async () => {
    render(<HopitauxPage />);

    await waitFor(() => expect(screen.getByText("10")).toBeInTheDocument());
  });

  it("compte les hôpitaux en alerte (≤ 5 lits)", async () => {
    render(<HopitauxPage />);

    // Seul "Hôpital Cochin" (2 lits) est en alerte ici -> 1
    await waitFor(() => {
      const alertes = screen.getAllByText("1");
      expect(alertes.length).toBeGreaterThan(0);
    });
  });

  it("affiche un message d'erreur si l'appel API échoue", async () => {
    vi.mocked(fetchHopitaux).mockRejectedValueOnce({ message: "Erreur serveur" });

    render(<HopitauxPage />);

    expect(await screen.findByText("Erreur serveur")).toBeInTheDocument();
  });

  it("affiche un chip “+N” pour les spécialités au-delà du seuil et permet de les déplier", async () => {
    vi.mocked(fetchHopitaux).mockResolvedValueOnce([
      {
        id: 3,
        nom: "Hôpital Necker",
        adresse: "149 Rue de Sèvres, Paris",
        latitude: 48.846,
        longitude: 2.3149,
        litsDisponibles: 6,
        specialites: ["Pédiatrie", "Cardiologie", "Neurologie", "Urologie", "Anesthésie"],
      },
    ]);

    render(<HopitauxPage />);

    await screen.findByText("Hôpital Necker");

    const chipPlus = screen.getByText("+2");
    expect(chipPlus).toBeInTheDocument();
    expect(screen.queryByText("Urologie")).not.toBeInTheDocument();

    await userEvent.click(chipPlus);

    expect(screen.getByText("Urologie")).toBeInTheDocument();
    expect(screen.getByText("Réduire")).toBeInTheDocument();

    await userEvent.click(screen.getByText("Réduire"));

    expect(screen.queryByText("Urologie")).not.toBeInTheDocument();
    expect(screen.getByText("+2")).toBeInTheDocument();
  });
});
