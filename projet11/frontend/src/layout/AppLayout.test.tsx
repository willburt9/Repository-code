import { describe, it, expect } from "vitest";
import { render, screen } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import { AppLayout } from "./AppLayout";

function renderLayout() {
  return render(
    <MemoryRouter initialEntries={["/urgences"]}>
      <AppLayout>
        <div>Contenu de la page</div>
      </AppLayout>
    </MemoryRouter>
  );
}

describe("AppLayout", () => {
  it("affiche la navigation et le contenu enfant", () => {
    renderLayout();

    expect(screen.getByText("Urgences")).toBeInTheDocument();
    expect(screen.getByText("Hôpitaux")).toBeInTheDocument();
    expect(screen.getByText("Contenu de la page")).toBeInTheDocument();
  });
});
