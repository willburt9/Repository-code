import { describe, it, expect } from "vitest";
import { render, screen } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import { AppLayout } from "./AppLayout";

describe("AppLayout", () => {
  it("affiche la navigation et le contenu enfant", () => {
    render(
      <MemoryRouter initialEntries={["/urgences"]}>
        <AppLayout>
          <div>Contenu de la page</div>
        </AppLayout>
      </MemoryRouter>
    );

    expect(screen.getByText("Urgences")).toBeInTheDocument();
    expect(screen.getByText("Hôpitaux")).toBeInTheDocument();
    expect(screen.getByText("Contenu de la page")).toBeInTheDocument();
  });
});
