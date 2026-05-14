package com.gestionfournisseurs.dto.dashboard;

public record RecentCommandeDTO(Long id, String fournisseurNom, String date, String statut, double montant) {
}
