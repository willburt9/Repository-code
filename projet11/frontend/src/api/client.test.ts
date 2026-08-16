import { describe, it, expect, beforeEach } from "vitest";
import MockAdapter from "axios-mock-adapter";
import { apiClient, ApiError } from "./client";

//
const mock = new MockAdapter(apiClient);

beforeEach(() => {
  mock.reset();
});

describe("apiClient", () => {
  it("laisse passer une réponse réussie sans modification", async () => {
    mock.onGet("/hopitaux").reply(200, [{ id: 1, nom: "Hôpital Bichat" }]);

    const { data } = await apiClient.get("/hopitaux");

    expect(data).toEqual([{ id: 1, nom: "Hôpital Bichat" }]);
  });

  it("transforme une erreur HTTP en ApiError avec le message du backend", async () => {
    mock.onGet("/hopitaux").reply(404, { message: "Aucun hôpital trouvé" });

    await expect(apiClient.get("/hopitaux")).rejects.toMatchObject({
      name: "ApiError",
      message: "Aucun hôpital trouvé",
      status: 404,
    });
  });

  it("utilise un message par défaut si le backend n'en fournit pas", async () => {
    mock.onGet("/hopitaux").reply(500, {});

    await expect(apiClient.get("/hopitaux")).rejects.toBeInstanceOf(ApiError);
    await expect(apiClient.get("/hopitaux")).rejects.toMatchObject({
      message: "Erreur réseau inattendue.",
      status: 500,
    });
  });

  it("utilise le statut 0 en cas d'erreur réseau (pas de réponse du serveur)", async () => {
    mock.onGet("/hopitaux").networkError();

    await expect(apiClient.get("/hopitaux")).rejects.toMatchObject({
      status: 0,
      message: "Erreur réseau inattendue.",
    });
  });
});