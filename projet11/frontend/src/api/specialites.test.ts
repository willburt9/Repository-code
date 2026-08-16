import { describe, it, expect, vi } from "vitest";
import { apiClient } from "./client";
import { fetchSpecialites } from "./specialites";
import type { GroupeSpecialiteResponse } from "../types/api";

vi.mock("./client", () => ({
  apiClient: { get: vi.fn(), post: vi.fn() },
}));

describe("fetchSpecialites", () => {
  it("appelle GET /specialites et retourne les groupes de spécialités", async () => {
    const groupes: GroupeSpecialiteResponse[] = [
      {
        id: 1,
        nom: "Médecine",
        specialites: [
          { id: 10, nom: "Cardiologie" },
          { id: 11, nom: "Pédiatrie" },
        ],
      },
    ];
    vi.mocked(apiClient.get).mockResolvedValueOnce({ data: groupes });

    const resultat = await fetchSpecialites();

    expect(apiClient.get).toHaveBeenCalledWith("/specialites");
    expect(resultat).toEqual(groupes);
  });

  it("propage l'erreur si l'appel échoue", async () => {
    vi.mocked(apiClient.get).mockRejectedValueOnce(new Error("Réseau indisponible"));

    await expect(fetchSpecialites()).rejects.toThrow("Réseau indisponible");
  });
});