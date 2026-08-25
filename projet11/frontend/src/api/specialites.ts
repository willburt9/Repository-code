import { apiClient } from "./client";
import type { GroupeSpecialiteResponse } from "../types/api";

export async function fetchSpecialites(): Promise<GroupeSpecialiteResponse[]> {
  const { data } = await apiClient.get<GroupeSpecialiteResponse[]>("/specialites");
  
  return data;
}
