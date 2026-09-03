import { describe, it, expect, vi } from "vitest";
import { apiClient } from "./client";
import { fetchRecommandation } from "./recommandations";
import type { RecommandationResponse } from "../types/api";

vi.mock("./client", () => ({
  apiClient: { get: vi.fn(), post: vi.fn() },
}));

describe("fetchRecommandation", () => {
  it("appelle GET /recommandations avec latitude, longitude et specialiteId en params", async () => {
    const recommandation: RecommandationResponse = {
      hopitalId: 1,
      nom: "Hôpital Bichat",
      adresse: "46 Rue Henri Huchard, Paris",
      latitude:  48.86056,
      longitude: 2.3367934,
      litsDisponibles: 4,
      distanceKm: 3.2,
      dureeMinutes: 12,
    };
    vi.mocked(apiClient.get).mockResolvedValueOnce({ data: recommandation });

    const resultat = await fetchRecommandation({
      latitude: 48.86056,
      longitude: 2.3367934,
      specialiteId: 10,
    });

    expect(apiClient.get).toHaveBeenCalledWith("/recommandations", {
      params: { latitude: 48.86056, longitude: 2.3367934, specialiteId: 10 },
    });
    expect(resultat).toEqual(recommandation);
  });

  it("propage l'erreur si aucun hôpital ne correspond", async () => {
    vi.mocked(apiClient.get).mockRejectedValueOnce(new Error("Aucun hôpital trouvé"));

    await expect(
      fetchRecommandation({ latitude: 0, longitude: 0, specialiteId: 1 })
    ).rejects.toThrow("Aucun hôpital trouvé");
  });
});