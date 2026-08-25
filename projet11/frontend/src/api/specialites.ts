import { apiClient } from "./client";
import type { GroupeSpecialiteResponse } from "../types/api";

export async function fetchSpecialites(): Promise<GroupeSpecialiteResponse[]> {
   const response = await apiClient.get<GroupeSpecialiteResponse[]>("/specialites");

  console.log("===== SPECIALITES =====");
  console.log("Status :", response.status);
  console.log("Data :", response.data);
  console.log("Is array :", Array.isArray(response.data));
  console.log("======================");

  return response.data;
}
