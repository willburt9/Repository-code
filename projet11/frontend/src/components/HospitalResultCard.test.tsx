import { describe, it, expect, vi } from "vitest";
import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { HospitalResultCard } from "./HospitalResultCard";
import type { RecommandationResponse } from "../types/api";

const recommandation: RecommandationResponse = {
  hopitalId: 1,
  nom: "Hôpital Bichat",
  adresse: "46 Rue Henri Huchard, Paris",
  latitude: 48.86056,
  longitude: 2.3367934,
  litsDisponibles: 4,
  distanceKm: 3.2,
  dureeMinutes: 12,
};

describe("HospitalResultCard", () => {
  it("affiche le nom, l'adresse, la distance et la durée", () => {
    render(
      <HospitalResultCard recommandation={recommandation} onReserver={vi.fn()} reservationEnCours={false} />
    );

    expect(screen.getByText("Hôpital Bichat")).toBeInTheDocument();
    expect(screen.getByText(/46 Rue Henri Huchard/)).toBeInTheDocument();
    expect(screen.getByText("3.2 km")).toBeInTheDocument();
    expect(screen.getByText("12 min")).toBeInTheDocument();
  });

  it("appelle onReserver au clic sur le bouton de réservation", async () => {
    const onReserver = vi.fn();
    render(
      <HospitalResultCard recommandation={recommandation} onReserver={onReserver} reservationEnCours={false} />
    );

    await userEvent.click(screen.getByRole("button", { name: /réserver un lit/i }));

    expect(onReserver).toHaveBeenCalledTimes(1);
  });

  it("désactive le bouton quand il n'y a plus de lit disponible", () => {
    render(
      <HospitalResultCard
        recommandation={{ ...recommandation, litsDisponibles: 0 }}
        onReserver={vi.fn()}
        reservationEnCours={false}
      />
    );

    expect(screen.getByRole("button", { name: /réserver un lit/i })).toBeDisabled();
  });

  it("affiche 'Réservation en cours...' et désactive le bouton pendant la réservation", () => {
    render(
      <HospitalResultCard recommandation={recommandation} onReserver={vi.fn()} reservationEnCours={true} />
    );

    const bouton = screen.getByRole("button", { name: /réservation en cours/i });
    expect(bouton).toBeDisabled();
  });
});
