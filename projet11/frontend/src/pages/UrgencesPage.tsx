import { useEffect, useMemo, useState } from "react";
import {
  Alert,
  Box,
  Button,
  Card,
  CardContent,
  IconButton,
  InputAdornment,
  MenuItem,
  Skeleton,
  Snackbar,
  TextField,
  Typography,
} from "@mui/material";
import Grid from "@mui/material/Grid";
import SearchIcon from "@mui/icons-material/SearchRounded";
import MyLocationIcon from "@mui/icons-material/MyLocationRounded";
import PersonSearchIcon from "@mui/icons-material/PersonSearchRounded";
import AddModeratorIcon from "@mui/icons-material/AddModeratorRounded";
import { fetchSpecialites } from "../api/specialites";
import { fetchRecommandation } from "../api/recommandations";
import { reserverLit } from "../api/reservations";
import { ApiError } from "../api/client";
import { HospitalResultCard } from "../components/HospitalResultCard";
import type { GroupeSpecialiteResponse, RecommandationResponse } from "../types/api";

export function UrgencesPage() {
  const [groupes, setGroupes] = useState<GroupeSpecialiteResponse[]>([]);
  const [latitude, setLatitude] = useState("48.86056");
  const [longitude, setLongitude] = useState("2.3367934");
  const [specialiteId, setSpecialiteId] = useState<number | "">("");

  const [recherche, setRecherche] = useState(false);
  const [recommandation, setRecommandation] = useState<RecommandationResponse | null>(null);
  const [erreur, setErreur] = useState<string | null>(null);

  const [reservationEnCours, setReservationEnCours] = useState(false);
  const [confirmation, setConfirmation] = useState<string | null>(null);

  useEffect(() => {
    fetchSpecialites()
      .then(setGroupes)
      .catch((e: ApiError) => setErreur(e.message));
  }, []);

  const specialiteOptions = useMemo(
    () =>
      groupes
        .flatMap((groupe) =>
          groupe.specialites.map((s) => ({ ...s, groupeNom: groupe.nom }))
        )
        .sort((a, b) => a.nom.localeCompare(b.nom, "fr")),
    [groupes]
  );

  async function handleRecherche() {
    if (specialiteId === "") {
      setErreur("Sélectionne une spécialité.");
      return;
    }

    setRecherche(true);
    setErreur(null);
    setRecommandation(null);

    try {
      const resultat = await fetchRecommandation({
        latitude: parseFloat(latitude),
        longitude: parseFloat(longitude),
        specialiteId: Number(specialiteId),
      });
      setRecommandation(resultat);
    } catch (e) {
      setErreur(e instanceof ApiError ? e.message : "Une erreur est survenue.");
    } finally {
      setRecherche(false);
    }
  }

  async function handleReserver() {
    if (!recommandation) return;
    setReservationEnCours(true);
    setErreur(null);

    try {
      const resultat = await reserverLit(recommandation.hopitalId);
      setConfirmation(
        `Lit réservé à ${recommandation.nom} — référence ${resultat.referencePatient}`
      );
      setRecommandation({ ...recommandation, litsDisponibles: resultat.litsDisponiblesRestants });
    } catch (e) {
      setErreur(e instanceof ApiError ? e.message : "Une erreur est survenue.");
    } finally {
      setReservationEnCours(false);
    }
  }

  function handleGeolocaliser() {
    if (!navigator.geolocation) return;
    navigator.geolocation.getCurrentPosition((position) => {
      setLatitude(position.coords.latitude.toFixed(4));
      setLongitude(position.coords.longitude.toFixed(4));
    });
  }

  return (
    <Box>
      <Typography variant="h4" gutterBottom>
        Urgences
      </Typography>
      <Typography variant="body1" color="text.secondary" sx={{ mb: 3 }}>
        Identifiez rapidement l'hôpital le mieux adapté pour votre patient
      </Typography>

      <Card sx={{ mb: 3 }}>
        <CardContent sx={{ p: 3 }}>
          <Box sx={{ display: "flex", alignItems: "center", gap: 1, mb: 2 }}>
            <PersonSearchIcon color="primary" />
            <Box>
              <Typography variant="subtitle1" sx={{ fontWeight: "600" }}>
                Recherche d'hôpital disponible
              </Typography>
              <Typography variant="caption" color="text.secondary">
                Renseignez la localisation et la spécialité requise
              </Typography>
            </Box>
          </Box>

          <Grid container spacing={2} sx={{ alignItems: "center" }}>
            <Grid size={{ xs: 12, sm: 3 }}>
              <TextField
                label="Latitude GPS"
                fullWidth
                value={latitude}
                onChange={(e) => setLatitude(e.target.value)}
                slotProps={{ htmlInput: { "data-cy": "input-latitude" } }}
              />
            </Grid>
            <Grid size={{ xs: 12, sm: 3 }}>
              <TextField
                label="Longitude GPS"
                fullWidth
                value={longitude}
                onChange={(e) => setLongitude(e.target.value)}
                slotProps={{ htmlInput: { "data-cy": "input-longitude" } }}
              />
            </Grid>
            <Grid size={{ xs: 12, sm: 4 }} data-cy="champ-specialite">
              <TextField
                select
                label="Spécialité requise"
                fullWidth
                value={specialiteId}
                onChange={(e) => setSpecialiteId(Number(e.target.value))}
                slotProps={{
                  input: {
                    startAdornment: (
                      <InputAdornment position="start">
                        <AddModeratorIcon fontSize="small" color="action" />
                      </InputAdornment>
                    ),
                  },
                }}
              >
                {specialiteOptions.map((s) => (
                  <MenuItem key={s.id} value={s.id}>
                    {s.nom} — {s.groupeNom}
                  </MenuItem>
                ))}
              </TextField>
            </Grid>
            <Grid size={{ xs: 12, sm: 2 }}>
              <Box sx={{ display: "flex", gap: 1 }}>
                <IconButton onClick={handleGeolocaliser} title="Me géolocaliser">
                  <MyLocationIcon />
                </IconButton>
                <Button
                  variant="contained"
                  fullWidth
                  size="large"
                  startIcon={<SearchIcon />}
                  onClick={handleRecherche}
                  disabled={recherche}
                  data-cy="bouton-rechercher"
                >
                  Trouver
                </Button>
              </Box>
            </Grid>
          </Grid>
        </CardContent>
      </Card>

      {erreur && (
        <Alert severity="error" sx={{ mb: 3 }} onClose={() => setErreur(null)} data-cy="alerte-erreur">
          {erreur}
        </Alert>
      )}

      {recherche && <Skeleton variant="rounded" height={220} sx={{ borderRadius: 4 }} />}

      {!recherche && recommandation && (
        <HospitalResultCard
          recommandation={recommandation}
          onReserver={handleReserver}
          reservationEnCours={reservationEnCours}
        />
      )}

      {!recherche && !recommandation && !erreur && (
        <Card>
          <CardContent sx={{ py: 6, textAlign: "center" }}>
            <PersonSearchIcon sx={{ fontSize: 56, color: "primary.light", mb: 1 }} />
            <Typography variant="h6">Prêt à rechercher</Typography>
            <Typography variant="body2" color="text.secondary">
              Renseignez la localisation GPS du patient et la spécialité requise,
              puis lancez la recherche pour identifier l'hôpital disponible à proximité.
            </Typography>
          </CardContent>
        </Card>
      )}

      <Snackbar
        open={!!confirmation}
        autoHideDuration={5000}
        onClose={() => setConfirmation(null)}
        message={confirmation}
      />
    </Box>
  );
}
