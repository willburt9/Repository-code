import { useState, type ReactNode } from "react";
import { NavLink, useLocation } from "react-router-dom";
import {
  AppBar,
  Avatar,
  Box,
  Drawer,
  IconButton,
  List,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Menu,
  MenuItem,
  Toolbar,
  Typography,
} from "@mui/material";
import EmergencyIcon from "@mui/icons-material/EmergencyShareRounded";
import LocalHospitalIcon from "@mui/icons-material/LocalHospitalRounded";
import NotificationsIcon from "@mui/icons-material/NotificationsRounded";
import AddCircleIcon from "@mui/icons-material/AddCircleRounded";
import logo from "../assets/logo.png";

const DRAWER_WIDTH = 240;

const NAV_ITEMS = [
  { label: "Urgences", to: "/urgences", icon: <EmergencyIcon /> },
  { label: "Hôpitaux", to: "/hopitaux", icon: <LocalHospitalIcon /> },
];

interface AppLayoutProps {
  children: ReactNode;
}

export function AppLayout({ children }: AppLayoutProps) {
  const location = useLocation();
  const [profileAnchor, setProfileAnchor] = useState<HTMLElement | null>(null);

  return (
    <Box sx={{ display: "flex", minHeight: "100vh", bgcolor: "background.default" }}>
      <AppBar
        position="fixed"
        color="inherit"
        sx={{ zIndex: (t) => t.zIndex.drawer + 1, bgcolor: "white" }}
      >
        <Toolbar sx={{ gap: 2 }}>
          <Box sx={{ display: "flex", alignItems: "center", gap: 1, minWidth: 200 }}>
            <img src={logo} alt="Logo" width={48} height={32} />
            <Box>
              <Typography variant="subtitle1" sx={{ fontWeight: "700", lineHeight: "1.1" }} color="primary">
                Med<span style={{ fontWeight: "900"}}>Head</span>
              </Typography>
              <Typography variant="caption" color="text.secondary" sx={{ letterSpacing:"0.8px", fontWeight:"600"}}>
                URGENCES
              </Typography>
            </Box>
          </Box>


          <Box sx={{ flexGrow: 1 }} />

          <IconButton> 
              <NotificationsIcon />
          </IconButton>

          <Box
            sx={{ display: "flex", alignItems: "center", gap: 1, cursor: "pointer" }}
            onClick={(e) => setProfileAnchor(e.currentTarget)}
          >
            <Avatar sx={{ bgcolor: "primary.main", width: 36, height: 36 }}>DR</Avatar>
            <Box sx={{ display: { xs: "none", sm: "block" } }}>
              <Typography variant="body2" sx={{ fontWeight: "600", lineHeight: "1.1" }}>
                Dr. J.DOE
              </Typography>
              <Typography variant="caption" color="text.secondary">
                Médecin urgentiste
              </Typography>
            </Box>
          </Box>
          <Menu anchorEl={profileAnchor} open={!!profileAnchor} onClose={() => setProfileAnchor(null)}>
            <MenuItem onClick={() => setProfileAnchor(null)}>Mon profil</MenuItem>
            <MenuItem onClick={() => setProfileAnchor(null)}>Paramètres</MenuItem>
            <MenuItem onClick={() => setProfileAnchor(null)}>Aide &amp; Support</MenuItem>
            <MenuItem onClick={() => setProfileAnchor(null)}>Se déconnecter</MenuItem>
          </Menu>
        </Toolbar>
      </AppBar>

      <Drawer
        variant="permanent"
        sx={{
          width: DRAWER_WIDTH,
          flexShrink: 0,
          [`& .MuiDrawer-paper`]: { width: DRAWER_WIDTH, boxSizing: "border-box" },
        }}
      >
        <Toolbar />
        <Box sx={{ px: 2, pt: 2 }}>
          <Typography variant="overline" color="text.secondary">
            Navigation
          </Typography>
        </Box>
        <List sx={{ px: 1 }}>
          {NAV_ITEMS.map((item) => (
            <ListItemButton
              key={item.to}
              component={NavLink}
              to={item.to}
              selected={location.pathname.startsWith(item.to)}
              sx={{ borderRadius: 2, mx: 1, mb: 0.5 }}
            >
              <ListItemIcon sx={{ minWidth: 36 }}>{item.icon}</ListItemIcon>
              <ListItemText primary={item.label} />
            </ListItemButton>
          ))}
        </List>
      </Drawer>

      <Box component="main" sx={{ flexGrow: 1, p: 3 }}>
        <Toolbar />
        {children}
      </Box>
    </Box>
  );
}
