import { useEffect, useMemo, useState } from "react";
import { Box, Card, CardContent, Chip, Stack, Typography } from "@mui/material";
import Grid from "@mui/material/Grid";
import {
  DataGrid,
  Toolbar,
  ToolbarButton,
  QuickFilter,
  QuickFilterControl,
  ColumnsPanelTrigger,
  FilterPanelTrigger,
  type GridColDef,
} from "@mui/x-data-grid";
import BedIcon from "@mui/icons-material/BedRounded";
import LocalHospitalIcon from "@mui/icons-material/LocalHospitalRounded";
import SearchIcon from "@mui/icons-material/SearchRounded";
import { fetchHopitaux } from "../api/hopitaux";
import { ApiError } from "../api/client";
import type { HopitalResponse } from "../types/api";

const columns: GridColDef<HopitalResponse>[] = [
  { field: "nom", headerName: "Nom", flex: 1.4, minWidth: 220 },
  { field: "adresse", headerName: "Adresse", flex: 2, minWidth: 260 },
  {
    field: "litsDisponibles",
    headerName: "Lits disponibles",
    width: 160,
    renderCell: (params) => (
      <Chip
        size="small"
        label={params.value}
        color={params.value > 10 ? "success" : params.value > 0 ? "warning" : "error"}
      />
    ),
  },
  {
    field: "specialites",
    headerName: "Spécialités",
    flex: 2,
    minWidth: 280,
    sortable: false,
    renderCell: (params) => (
      <Stack   direction="row"  sx={{ py: 0.5, flexWrap:"wrap", gap: 0.5 }}>
        {(params.value as string[]).slice(0, 3).map((s) => (
          <Chip key={s} size="small" label={s} variant="outlined" />
        ))}
        {(params.value as string[]).length > 3 && (
          <Chip size="small" label={`+${(params.value as string[]).length - 3}`} />
        )}
      </Stack>
    ),
  },
];

function GridToolbarAvecRecherche() {
  return (
    <Toolbar >
      <Box sx={{ display: "flex", gap: 1 }}>
        <ColumnsPanelTrigger
          render={<ToolbarButton />}
        />

        <FilterPanelTrigger
          render={<ToolbarButton />}
        />
      </Box>

      <QuickFilter>
        <QuickFilterControl
          placeholder="Rechercher par nom, ville..."
          slotProps={{
            input: {
              startAdornment: (
                <SearchIcon
                  fontSize="small"
                  sx={{ mr: 1 }}
                />
              ),
            },
          }}
        />
      </QuickFilter>
    </Toolbar>
  );
}

export function HopitauxPage() {
  const [hopitaux, setHopitaux] = useState<HopitalResponse[]>([]);
  const [chargement, setChargement] = useState(true);
  const [erreur, setErreur] = useState<string | null>(null);

  useEffect(() => {
    fetchHopitaux()
      .then(setHopitaux)
      .catch((e: ApiError) => setErreur(e.message))
      .finally(() => setChargement(false));
  }, []);

  const stats = useMemo(() => {
    const totalLits = hopitaux.reduce((acc, h) => acc + h.litsDisponibles, 0);
    const enAlerte = hopitaux.filter((h) => h.litsDisponibles <= 5).length;
    return { totalLits, enAlerte, total: hopitaux.length };
  }, [hopitaux]);

  return (
    <Box>
      <Stack direction="row" sx={{ mb: 1, justifyContent:"space-between", alignItems:"center"}}>
        <Typography variant="h4">Annuaire des hôpitaux</Typography>
      </Stack>
      <Typography variant="body2" color="text.secondary" sx={{ mb: 3 }}>
        {stats.total} établissements
      </Typography>

      <Grid container spacing={2} sx={{ mb: 3 }}>
        <Grid size={{ xs: 12, sm: 4 }}>
          <Card>
            <CardContent sx={{ display: "flex", alignItems: "center", gap: 2 }}>
              <BedIcon color="success" fontSize="large" />
              <Box>
                <Typography variant="h5" sx={{ fontWeight:"700"}}>
                  {stats.totalLits}
                </Typography>
                <Typography variant="caption" color="text.secondary">
                  Total lits disponibles
                </Typography>
              </Box>
            </CardContent>
          </Card>
        </Grid>
        <Grid size={{ xs: 12, sm: 4 }}>
          <Card>
            <CardContent sx={{ display: "flex", alignItems: "center", gap: 2 }}>
              <LocalHospitalIcon color="warning" fontSize="large" />
              <Box>
                <Typography variant="h5" sx={{ fontWeight:"700"}}>
                  {stats.enAlerte}
                </Typography>
                <Typography variant="caption" color="text.secondary">
                  Hôpitaux en alerte (≤ 5 lits)
                </Typography>
              </Box>
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      {erreur && (
        <Typography color="error" sx={{ mb: 2 }}>
          {erreur}
        </Typography>
      )}

      <Card>
        <DataGrid
          rows={hopitaux}
          columns={columns}
          loading={chargement}
          getRowId={(row) => row.id}
          slots={{ toolbar: GridToolbarAvecRecherche }}
          initialState={{
            pagination: { paginationModel: { pageSize: 10 } },
          }}
          pageSizeOptions={[10, 25, 50]}
          disableRowSelectionOnClick
          sx={{
            border: "none",
            "--DataGrid-overlayHeight": "300px",
            "& .MuiDataGrid-columnHeaders": { bgcolor: "grey.50" },
          }}
        />
      </Card>
    </Box>
  );
}
