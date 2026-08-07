import {
  Box,
  Card,
  CardContent,
  Chip,
  Divider,
  Stack,
  Typography,
  Button,
} from "@mui/material";
import LocationOnIcon from "@mui/icons-material/LocationOnRounded";
import AccessTimeIcon from "@mui/icons-material/AccessTimeRounded";
import BedIcon from "@mui/icons-material/BedRounded";
import RouteIcon from "@mui/icons-material/RouteRounded";
import type { RecommandationResponse } from "../types/api";

interface HospitalResultCardProps {
  recommandation: RecommandationResponse;
  onReserver: () => void;
  reservationEnCours: boolean;
}

export function HospitalResultCard({
  recommandation,
  onReserver,
  reservationEnCours,
}: HospitalResultCardProps) {
  return (
    <Card>
      <CardContent sx={{ p: 3 }}>
        <Stack direction="row" sx={{ justifyContent: "space-between", alignItems: "flex-start" }}>
          <Box>
            <Typography variant="h6">{recommandation.nom}</Typography>
            <Stack direction="row" sx={{ color: "text.secondary", mt: 0.5, alignItems: "center", gap: "0.5" }}>
              <LocationOnIcon fontSize="small" />
              <Typography variant="body2">{recommandation.adresse}</Typography>
            </Stack>
          </Box>
          <Chip
            label={`${recommandation.litsDisponibles} lit(s) disponible(s)`}
            color={recommandation.litsDisponibles > 3 ? "success" : "warning"}
            icon={<BedIcon />}
          />
        </Stack>

        <Divider sx={{ my: 2 }} />

        <Stack direction="row" sx={{ gap: 4 }}>
          <Stack direction="row" sx={{ alignItems: "center", gap: "1" }}>
            <RouteIcon color="primary" />
            <Box>
              <Typography variant="caption" color="text.secondary">
                Distance
              </Typography>
              <Typography variant="subtitle1" sx={{ fontWeight: "600" }}>
                {recommandation.distanceKm.toFixed(1)} km
              </Typography>
            </Box>
          </Stack>
          <Stack direction="row" sx={{ alignItems: "center", gap: 1 }}>
            <AccessTimeIcon color="primary" />
            <Box>
              <Typography variant="caption" color="text.secondary">
                Temps de trajet estimé
              </Typography>
              <Typography variant="subtitle1" sx={{ fontWeight: "600" }}>
                {Math.round(recommandation.dureeMinutes)} min
              </Typography>
            </Box>
          </Stack>
        </Stack>

        <Box sx={{ mt: 3, display: "flex", justifyContent: "flex-end" }}>
          <Button
            variant="contained"
            color="secondary"
            size="large"
            disabled={reservationEnCours || recommandation.litsDisponibles === 0}
            onClick={onReserver}
          >
            {reservationEnCours ? "Réservation en cours..." : "Réserver un lit"}
          </Button>
        </Box>
      </CardContent>
    </Card>
  );
}
