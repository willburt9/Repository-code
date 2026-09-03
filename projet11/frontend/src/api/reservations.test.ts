import { describe, it, expect, vi } from "vitest";
import { apiClient } from "./client";
import { reserverLit } from "./reservations";
import type { ReservationResponse } from "../types/api";

vi.mock("./client", () => ({
  apiClient: { get: vi.fn(), post: vi.fn() },
}));

describe("reserverLit", () => {
  it("envoie le hopitalId en POST /reservations", async () => {
    const reponse: ReservationResponse = {
      hopitalId:7,
      horodatage: "2026-08-16 20:50:43.37",
      referencePatient: "PAT-338ef318-3dd5-47b4-b069-9ad86c344148",
      litsDisponiblesRestants: 3,
    };
    vi.mocked(apiClient.post).mockResolvedValueOnce({ data: reponse });

    const resultat = await reserverLit(7);

    expect(apiClient.post).toHaveBeenCalledWith("/reservations", { hopitalId: 7 });
    expect(resultat).toEqual(reponse);
  });
});