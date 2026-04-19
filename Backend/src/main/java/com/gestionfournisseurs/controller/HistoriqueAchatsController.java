package com.gestionfournisseurs.controller;

import com.gestionfournisseurs.entity.HistoriqueAchats;
import com.gestionfournisseurs.service.HistoriqueAchatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/historique-achats")
@Tag(name = "Historique des achats", description = "API pour la gestion de l'historique des achats")
public class HistoriqueAchatsController {

    @Autowired
    private HistoriqueAchatsService historiqueAchatsService;

    @GetMapping
    @Operation(summary = "Récupérer tout l'historique des achats", description = "Retourne la liste complète de l'historique des achats")
    public ResponseEntity<List<HistoriqueAchats>> getAllHistoriqueAchats() {
        List<HistoriqueAchats> historique = historiqueAchatsService.getAllHistoriqueAchats();
        return ResponseEntity.ok(historique);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un enregistrement d'historique par son ID", description = "Retourne un enregistrement d'historique spécifique basé sur son ID")
    public ResponseEntity<HistoriqueAchats> getHistoriqueAchatById(
            @Parameter(description = "ID de l'enregistrement d'historique à récupérer") @PathVariable Long id) {
        return historiqueAchatsService.getHistoriqueAchatById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Créer un nouvel enregistrement d'historique", description = "Crée et retourne un nouvel enregistrement d'historique d'achat")
    public ResponseEntity<HistoriqueAchats> createHistoriqueAchat(
            @Parameter(description = "Enregistrement d'historique à créer") @Valid @RequestBody HistoriqueAchats historiqueAchat) {
        HistoriqueAchats createdHistorique = historiqueAchatsService.saveHistoriqueAchat(historiqueAchat);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdHistorique);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un enregistrement d'historique", description = "Met à jour et retourne un enregistrement d'historique existant")
    public ResponseEntity<HistoriqueAchats> updateHistoriqueAchat(
            @Parameter(description = "ID de l'enregistrement d'historique à mettre à jour") @PathVariable Long id,
            @Parameter(description = "Détails de l'enregistrement d'historique mis à jour") @Valid @RequestBody HistoriqueAchats historiqueAchatDetails) {
        HistoriqueAchats updatedHistorique = historiqueAchatsService.updateHistoriqueAchat(id, historiqueAchatDetails);
        return updatedHistorique != null ? ResponseEntity.ok(updatedHistorique) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un enregistrement d'historique", description = "Supprime un enregistrement d'historique basé sur son ID")
    public ResponseEntity<Void> deleteHistoriqueAchat(
            @Parameter(description = "ID de l'enregistrement d'historique à supprimer") @PathVariable Long id) {
        historiqueAchatsService.deleteHistoriqueAchat(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/fournisseur/{fournisseurId}")
    @Operation(summary = "Récupérer l'historique par fournisseur", description = "Retourne tout l'historique des achats d'un fournisseur spécifique")
    public ResponseEntity<List<HistoriqueAchats>> getHistoriqueByFournisseurId(
            @Parameter(description = "ID du fournisseur") @PathVariable Long fournisseurId) {
        List<HistoriqueAchats> historique = historiqueAchatsService.getHistoriqueByFournisseurId(fournisseurId);
        return ResponseEntity.ok(historique);
    }

    @GetMapping("/produit/{produit}")
    @Operation(summary = "Récupérer l'historique par produit", description = "Retourne tout l'historique des achats pour un produit spécifique")
    public ResponseEntity<List<HistoriqueAchats>> getHistoriqueByProduit(
            @Parameter(description = "Nom du produit") @PathVariable String produit) {
        List<HistoriqueAchats> historique = historiqueAchatsService.getHistoriqueByProduit(produit);
        return ResponseEntity.ok(historique);
    }

    @GetMapping("/date-range")
    @Operation(summary = "Récupérer l'historique par plage de dates", description = "Retourne l'historique des achats dans une plage de dates spécifique")
    public ResponseEntity<List<HistoriqueAchats>> getHistoriqueByDateRange(
            @Parameter(description = "Date de début") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @Parameter(description = "Date de fin") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        List<HistoriqueAchats> historique = historiqueAchatsService.getHistoriqueByDateRange(dateDebut, dateFin);
        return ResponseEntity.ok(historique);
    }

    @GetMapping("/fournisseur/{fournisseurId}/stats/average-delai")
    @Operation(summary = "Calculer le délai moyen de livraison par fournisseur", description = "Calcule et retourne le délai moyen de livraison pour un fournisseur spécifique")
    public ResponseEntity<Double> getAverageDelaiLivraisonByFournisseur(
            @Parameter(description = "ID du fournisseur") @PathVariable Long fournisseurId) {
        Double averageDelai = historiqueAchatsService.getAverageDelaiLivraisonByFournisseur(fournisseurId);
        return ResponseEntity.ok(averageDelai);
    }

    @GetMapping("/fastest-delivery/{produit}")
    @Operation(summary = "Récupérer les livraisons les plus rapides pour un produit", description = "Retourne les enregistrements d'historique avec les délais de livraison les plus rapides pour un produit spécifique")
    public ResponseEntity<List<HistoriqueAchats>> getFastestDeliveryForProduct(
            @Parameter(description = "Nom du produit") @PathVariable String produit) {
        List<HistoriqueAchats> fastestDeliveries = historiqueAchatsService.getFastestDeliveryForProduct(produit);
        return ResponseEntity.ok(fastestDeliveries);
    }

    @GetMapping("/fournisseur/{fournisseurId}/stats/total-achats")
    @Operation(summary = "Calculer le nombre total d'achats par fournisseur", description = "Calcule et retourne le nombre total d'achats pour un fournisseur spécifique")
    public ResponseEntity<Long> getTotalAchatsByFournisseur(
            @Parameter(description = "ID du fournisseur") @PathVariable Long fournisseurId) {
        Long totalAchats = historiqueAchatsService.getTotalAchatsByFournisseur(fournisseurId);
        return ResponseEntity.ok(totalAchats);
    }
}
