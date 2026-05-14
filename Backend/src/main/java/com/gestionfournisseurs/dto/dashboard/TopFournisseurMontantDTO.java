package com.gestionfournisseurs.dto.dashboard;

public record TopFournisseurMontantDTO(Long fournisseurId, String nom, double montantTotal, long nombreCommandes) {
}
