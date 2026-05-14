package com.gestionfournisseurs.service;

import com.gestionfournisseurs.dto.dashboard.DashboardSummaryDTO;
import com.gestionfournisseurs.dto.dashboard.EvolutionPrixProduitDTO;
import com.gestionfournisseurs.dto.dashboard.MontantMoisDTO;
import com.gestionfournisseurs.dto.dashboard.PrixPointDTO;
import com.gestionfournisseurs.dto.dashboard.RecentCommandeDTO;
import com.gestionfournisseurs.dto.dashboard.TopFournisseurMontantDTO;
import com.gestionfournisseurs.entity.CommandeAchat;
import com.gestionfournisseurs.entity.HistoriqueAchats;
import com.gestionfournisseurs.entity.LigneCommandeAchat;
import com.gestionfournisseurs.repository.CommandeAchatRepository;
import com.gestionfournisseurs.repository.FournisseurRepository;
import com.gestionfournisseurs.repository.HistoriqueAchatsRepository;
import com.gestionfournisseurs.repository.LigneCommandeAchatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    private static final int MAX_EVOLUTION_PRODUITS = 12;
    private static final int MAX_POINTS_PAR_PRODUIT = 80;
    private static final int RECENT_COMMANDES = 5;

    private final CommandeAchatRepository commandeAchatRepository;
    private final LigneCommandeAchatRepository ligneCommandeAchatRepository;
    private final FournisseurRepository fournisseurRepository;
    private final HistoriqueAchatsRepository historiqueAchatsRepository;

    public DashboardService(
            CommandeAchatRepository commandeAchatRepository,
            LigneCommandeAchatRepository ligneCommandeAchatRepository,
            FournisseurRepository fournisseurRepository,
            HistoriqueAchatsRepository historiqueAchatsRepository) {
        this.commandeAchatRepository = commandeAchatRepository;
        this.ligneCommandeAchatRepository = ligneCommandeAchatRepository;
        this.fournisseurRepository = fournisseurRepository;
        this.historiqueAchatsRepository = historiqueAchatsRepository;
    }

    public DashboardSummaryDTO getSummary(LocalDate from, LocalDate to) {
        if (from == null) {
            from = LocalDate.now().minusMonths(12);
        }
        if (to == null) {
            to = LocalDate.now();
        }
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("La date de début doit être antérieure ou égale à la date de fin");
        }

        long totalFournisseurs = fournisseurRepository.count();
        List<CommandeAchat> commandes = commandeAchatRepository.findByDateBetween(from, to);

        List<CommandeAchat> actives = commandes.stream()
                .filter(c -> c.getStatut() == null || !"Annulée".equalsIgnoreCase(c.getStatut()))
                .toList();

        double montantTotalPeriode = actives.stream().mapToDouble(CommandeAchat::getMontant).sum();

        long commandesEnAttente = commandes.stream().filter(c -> "En attente".equals(c.getStatut())).count();
        long commandesLivree = commandes.stream().filter(c -> "Livrée".equals(c.getStatut())).count();
        long commandesAutres = commandes.size() - commandesEnAttente - commandesLivree;

        Map<YearMonth, double[]> monthAgg = new TreeMap<>();
        for (CommandeAchat c : actives) {
            if (c.getDate() == null) {
                continue;
            }
            YearMonth ym = YearMonth.from(c.getDate());
            double[] agg = monthAgg.computeIfAbsent(ym, k -> new double[2]);
            agg[0] += c.getMontant();
            agg[1] += 1;
        }
        List<MontantMoisDTO> montantsParMois = monthAgg.entrySet().stream()
                .map(e -> new MontantMoisDTO(
                        e.getKey().toString(),
                        e.getValue()[0],
                        (long) e.getValue()[1]))
                .toList();

        Map<Long, FournisseurAgg> fourAgg = new HashMap<>();
        for (CommandeAchat c : actives) {
            if (c.getFournisseur() == null || c.getFournisseur().getId() == null) {
                continue;
            }
            Long id = c.getFournisseur().getId();
            FournisseurAgg fa = fourAgg.computeIfAbsent(id, k -> new FournisseurAgg(id, c.getFournisseur().getNom()));
            fa.montant += c.getMontant();
            fa.count++;
        }
        List<TopFournisseurMontantDTO> topFournisseurs = fourAgg.values().stream()
                .sorted(Comparator.comparingDouble((FournisseurAgg a) -> a.montant).reversed())
                .limit(10)
                .map(a -> new TopFournisseurMontantDTO(a.id, a.nom, a.montant, a.count))
                .toList();

        List<HistoriqueAchats> historiques = historiqueAchatsRepository.findByDateAchatBetween(from, to);
        long totalAchatsHistoriquePeriode = historiques.size();
        double moyenneDelai = historiques.stream()
                .mapToInt(HistoriqueAchats::getDelaiLivraison)
                .average()
                .orElse(0.0);

        List<LigneCommandeAchat> lignes = ligneCommandeAchatRepository.findAllWithCommandeForPeriod(from, to);
        List<EvolutionPrixProduitDTO> evolutionPrix = buildEvolutionPrix(lignes);

        List<RecentCommandeDTO> recent = commandes.stream()
                .filter(c -> c.getDate() != null)
                .sorted(Comparator.comparing(CommandeAchat::getDate).reversed())
                .limit(RECENT_COMMANDES)
                .map(this::toRecent)
                .toList();

        return new DashboardSummaryDTO(
                from.toString(),
                to.toString(),
                totalFournisseurs,
                commandes.size(),
                montantTotalPeriode,
                totalAchatsHistoriquePeriode,
                Math.round(moyenneDelai * 10.0) / 10.0,
                commandesEnAttente,
                commandesLivree,
                Math.max(0, commandesAutres),
                montantsParMois,
                topFournisseurs,
                evolutionPrix,
                recent);
    }

    private RecentCommandeDTO toRecent(CommandeAchat c) {
        String fnom = c.getFournisseur() != null ? c.getFournisseur().getNom() : "—";
        return new RecentCommandeDTO(
                c.getId(),
                fnom,
                c.getDate().toString(),
                c.getStatut() != null ? c.getStatut() : "",
                c.getMontant() != null ? c.getMontant() : 0.0);
    }

    private List<EvolutionPrixProduitDTO> buildEvolutionPrix(List<LigneCommandeAchat> lignes) {
        Map<String, List<PrixPointDTO>> byProduit = new HashMap<>();
        for (LigneCommandeAchat l : lignes) {
            if (l.getCommande() == null || l.getCommande().getDate() == null || l.getProduit() == null) {
                continue;
            }
            String produit = l.getProduit().trim();
            if (produit.isEmpty()) {
                continue;
            }
            String date = l.getCommande().getDate().toString();
            String fnom = l.getCommande().getFournisseur() != null
                    ? l.getCommande().getFournisseur().getNom()
                    : "—";
            double prix = l.getPrixUnitaire() != null ? l.getPrixUnitaire() : 0.0;
            byProduit.computeIfAbsent(produit, k -> new ArrayList<>()).add(new PrixPointDTO(date, prix, fnom));
        }
        byProduit.forEach((k, list) -> list.sort(Comparator.comparing(PrixPointDTO::date)));
        return byProduit.entrySet().stream()
                .sorted(Comparator.comparingInt(e -> -e.getValue().size()))
                .limit(MAX_EVOLUTION_PRODUITS)
                .map(e -> new EvolutionPrixProduitDTO(
                        e.getKey(),
                        e.getValue().stream().limit(MAX_POINTS_PAR_PRODUIT).collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    private static final class FournisseurAgg {
        private final Long id;
        private final String nom;
        private double montant;
        private long count;

        private FournisseurAgg(Long id, String nom) {
            this.id = id;
            this.nom = nom != null ? nom : "—";
        }
    }
}
