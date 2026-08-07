import { Navigate, Route, Routes } from "react-router-dom";
import { AppLayout } from "./layout/AppLayout";
import { UrgencesPage } from "./pages/UrgencesPage";
import { HopitauxPage } from "./pages/HopitauxPage";

export default function App() {
  return (
    <AppLayout>
      <Routes>
        <Route path="/" element={<Navigate to="/urgences" replace />} />
        <Route path="/urgences" element={<UrgencesPage />} />
        <Route path="/hopitaux" element={<HopitauxPage />} />
      </Routes>
    </AppLayout>
  );
}
