import { describe, it, expect, vi } from "vitest";
import { apiClient } from "./client";
import { fetchHopitaux } from "./hopitaux";
import type { HopitalResponse } from "../types/api";

// On mocke tout le module client : ainsi aucune vraie requête HTTP n'est
// jamais tentée pendant les tests.
vi.mock("./client", () => ({
  apiClient: { get: vi.fn(), post: vi.fn() },
}));

describe("fetchHopitaux", () => {
  it("appelle GET /hopitaux et retourne les données", async () => {
    const hopitaux: HopitalResponse[] = [
      {
        id: 1,
        nom: "Hôpital Bichat",
        adresse: "46 Rue Henri Huchard, Paris",
        latitude: 48.86056,
        longitude: 2.3367934,
        litsDisponibles: 5,
        specialites: ["Cardiologie", "Anesthésie"],
      },
    ];
    vi.mocked(apiClient.get).mockResolvedValueOnce({ data: hopitaux });

    const resultat = await fetchHopitaux();

    expect(apiClient.get).toHaveBeenCalledWith("/hopitaux");
    expect(resultat).toEqual(hopitaux);
  });

  it("propage l'erreur si l'appel échoue", async () => {
    vi.mocked(apiClient.get).mockRejectedValueOnce(new Error("Réseau indisponible"));

    await expect(fetchHopitaux()).rejects.toThrow("Réseau indisponible");
  });
});