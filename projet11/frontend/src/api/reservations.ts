import { apiClient } from "./client";
import type { ReservationRequest, ReservationResponse } from "../types/api";

export async function reserverLit(hopitalId: number): Promise<ReservationResponse> {
  const requete: ReservationRequest = { hopitalId };
  const { data } = await apiClient.post<ReservationResponse>("/reservations", requete);
  return data;
}
