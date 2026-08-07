import axios from "axios";

// En dev, le backend Spring Boot tourne sur le port 8080, le front Vite sur 5173.
// Il faut activer le CORS côté backend (adapter/config/CorsConfig.java).
export const apiClient = axios.create({
  baseURL: "http://localhost:8080",
  headers: { "Content-Type": "application/json" },
});

export class ApiError extends Error {
  readonly status: number;

  constructor(message: string, status: number) {
    super(message);
    this.name = "ApiError";
    this.status = status;
  }
}

apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    const backendMessage = error?.response?.data?.message;
    const status = error?.response?.status ?? 0;
    return Promise.reject(new ApiError(backendMessage ?? "Erreur réseau inattendue.", status));
  }
);
