import { apiClient } from "./client";
import type { RecommandationResponse } from "../types/api";

export interface RecommandationParams {
  latitude: number;
  longitude: number;
  specialiteId: number;
}

export async function fetchRecommandation(
  params: RecommandationParams
): Promise<RecommandationResponse> {
  const { data } = await apiClient.get<RecommandationResponse>("/recommandations", { params });
  return data;
}
