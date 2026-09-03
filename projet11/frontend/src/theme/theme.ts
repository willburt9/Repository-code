import { createTheme } from "@mui/material/styles";
import { frFR } from "@mui/material/locale";
import { frFR as dataGridFrFR } from "@mui/x-data-grid/locales";

// Palette et tokens définis par le cahier de charge du front (MedDispatch).
const palette = {
  primary: "#024D85", // MedHead Blue
  secondary: "#26A69A", // Teal
  success: "#2E7D32",
  warning: "#ED6C02",
  error: "#D32F2F",
  background: "#F5F7FA",
};

export const theme = createTheme(
  {
    palette: {
      mode: "light",
      primary: { main: palette.primary },
      secondary: { main: palette.secondary },
      success: { main: palette.success },
      warning: { main: palette.warning },
      error: { main: palette.error },
      background: { default: palette.background, paper: "#FFFFFF" },
    },
    typography: {
      fontFamily: '"Roboto", "Segoe UI", sans-serif',
      h4: { fontWeight: 600 },
      h5: { fontWeight: 600 },
      h6: { fontWeight: 600 },
    },
    shape: {
      borderRadius: 14, // 12-16px demandé
    },
    components: {
      MuiCard: {
        styleOverrides: {
          root: {
            borderRadius: 16,
            boxShadow: "0 2px 12px rgba(15, 23, 42, 0.06)",
          },
        },
      },
      MuiPaper: {
        styleOverrides: {
          root: {
            backgroundImage: "none",
          },
        },
      },
      MuiButton: {
        styleOverrides: {
          root: {
            borderRadius: 12,
            textTransform: "none",
            fontWeight: 600,
          },
        },
      },
      MuiChip: {
        styleOverrides: {
          root: {
            borderRadius: 8,
            fontWeight: 500,
          },
        },
      },
      MuiAppBar: {
        styleOverrides: {
          root: {
            boxShadow: "0 1px 4px rgba(15, 23, 42, 0.06)",
          },
        },
      },
      MuiDrawer: {
        styleOverrides: {
          paper: {
            border: "none",
          },
        },
      },
    },
  },
  frFR,
  dataGridFrFR
);
