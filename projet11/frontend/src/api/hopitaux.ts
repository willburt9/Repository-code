import { apiClient } from "./client";
import type { HopitalResponse } from "../types/api";

export async function fetchHopitaux(): Promise<HopitalResponse[]> {
  const { data } = await apiClient.get<HopitalResponse[]>("/hopitaux");
  return data;
}
