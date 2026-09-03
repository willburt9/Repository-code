import { defineConfig } from "vitest/config";
import react from "@vitejs/plugin-react";

// Place ce fichier à la racine du projet (à côté de vite.config.ts).
// Si tu as déjà un vite.config.ts, tu peux simplement fusionner
// la clé "test" dedans plutôt que d'avoir deux fichiers séparés.
export default defineConfig({
  plugins: [react()],
  test: {
    environment: "jsdom",
    setupFiles: "./src/test/setup.ts",
    globals: true,
    css: true,
    coverage: {
      provider: "v8",
      reporter: ["text", "html"],
      exclude: ["**/*.test.{ts,tsx}", "src/test/**"],
    },
  },
});