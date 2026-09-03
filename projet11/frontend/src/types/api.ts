// Types miroirs des DTOs du backend (adapter/in/web/dto/*.java).
// Toute évolution d'un DTO Java doit être répercutée ici.

export interface HopitalResponse {
  id: number;
  nom: string;
  adresse: string;
  latitude: number;
  longitude: number;
  litsDisponibles: number;
  specialites: string[];
}

export interface SpecialiteResponse {
  id: number;
  nom: string;
}

export interface GroupeSpecialiteResponse {
  id: number;
  nom: string;
  specialites: SpecialiteResponse[];
}

export interface RecommandationResponse {
  hopitalId: number;
  nom: string;
  adresse: string;
  latitude: number;
  longitude: number;
  litsDisponibles: number;
  distanceKm: number;
  dureeMinutes: number;
}

export interface ReservationRequest {
  hopitalId: number;
}

export interface ReservationResponse {
  hopitalId: number;
  referencePatient: string;
  horodatage: string;
  litsDisponiblesRestants: number;
}

export interface ErrorResponse {
  message: string;
  status: number;
  horodatage: string;
}
