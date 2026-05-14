package com.gestionfournisseurs.dto.dashboard;

import java.util.List;

public record DashboardSummaryDTO(
        String periodeDebut,
        String periodeFin,
        long totalFournisseurs,
        long totalCommandesPeriode,
        double montantTotalPeriode,
        long totalAchatsHistoriquePeriode,
        double moyenneDelaiLivraison,
        long commandesEnAttente,
        long commandesLivree,
        long commandesAutres,
        List<MontantMoisDTO> montantsParMois,
        List<TopFournisseurMontantDTO> topFournisseurs,
        List<EvolutionPrixProduitDTO> evolutionPrixParProduit,
        List<RecentCommandeDTO> recentCommandes
) {
}
