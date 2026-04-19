package com.gestionachats.controller;

import com.gestionachats.entity.LigneCommandeAchat;
import com.gestionachats.service.LigneCommandeAchatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lignes-commande")
@Tag(name = "Lignes de commande", description = "API pour la gestion des lignes de commande d'achat")
public class LigneCommandeAchatController {

    @Autowired
    private LigneCommandeAchatService ligneCommandeAchatService;

    @GetMapping
    @Operation(summary = "Récupérer toutes les lignes de commande", description = "Retourne la liste de toutes les lignes de commande")
    public ResponseEntity<List<LigneCommandeAchat>> getAllLignesCommande() {
        List<LigneCommandeAchat> lignesCommande = ligneCommandeAchatService.getAllLignesCommande();
        return ResponseEntity.ok(lignesCommande);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une ligne de commande par son ID", description = "Retourne une ligne de commande spécifique basée sur son ID")
    public ResponseEntity<LigneCommandeAchat> getLigneCommandeById(
            @Parameter(description = "ID de la ligne de commande à récupérer") @PathVariable Long id) {
        return ligneCommandeAchatService.getLigneCommandeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Créer une nouvelle ligne de commande", description = "Crée et retourne une nouvelle ligne de commande")
    public ResponseEntity<LigneCommandeAchat> createLigneCommande(
            @Parameter(description = "Ligne de commande à créer") @Valid @RequestBody LigneCommandeAchat ligneCommande) {
        LigneCommandeAchat createdLigneCommande = ligneCommandeAchatService.saveLigneCommande(ligneCommande);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLigneCommande);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une ligne de commande", description = "Met à jour et retourne une ligne de commande existante")
    public ResponseEntity<LigneCommandeAchat> updateLigneCommande(
            @Parameter(description = "ID de la ligne de commande à mettre à jour") @PathVariable Long id,
            @Parameter(description = "Détails de la ligne de commande mise à jour") @Valid @RequestBody LigneCommandeAchat ligneCommandeDetails) {
        LigneCommandeAchat updatedLigneCommande = ligneCommandeAchatService.updateLigneCommande(id, ligneCommandeDetails);
        return updatedLigneCommande != null ? ResponseEntity.ok(updatedLigneCommande) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une ligne de commande", description = "Supprime une ligne de commande basée sur son ID")
    public ResponseEntity<Void> deleteLigneCommande(
            @Parameter(description = "ID de la ligne de commande à supprimer") @PathVariable Long id) {
        ligneCommandeAchatService.deleteLigneCommande(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/commande/{commandeId}")
    @Operation(summary = "Récupérer les lignes par commande", description = "Retourne toutes les lignes d'une commande spécifique")
    public ResponseEntity<List<LigneCommandeAchat>> getLignesByCommandeId(
            @Parameter(description = "ID de la commande") @PathVariable Long commandeId) {
        List<LigneCommandeAchat> lignesCommande = ligneCommandeAchatService.getLignesByCommandeId(commandeId);
        return ResponseEntity.ok(lignesCommande);
    }

    @GetMapping("/produit/{produit}")
    @Operation(summary = "Récupérer les lignes par produit", description = "Retourne toutes les lignes contenant un produit spécifique")
    public ResponseEntity<List<LigneCommandeAchat>> getLignesByProduit(
            @Parameter(description = "Nom du produit") @PathVariable String produit) {
        List<LigneCommandeAchat> lignesCommande = ligneCommandeAchatService.getLignesByProduit(produit);
        return ResponseEntity.ok(lignesCommande);
    }

    @GetMapping("/fournisseur/{fournisseurId}")
    @Operation(summary = "Récupérer les lignes par fournisseur", description = "Retourne toutes les lignes de commande d'un fournisseur spécifique")
    public ResponseEntity<List<LigneCommandeAchat>> getLignesByFournisseurId(
            @Parameter(description = "ID du fournisseur") @PathVariable Long fournisseurId) {
        List<LigneCommandeAchat> lignesCommande = ligneCommandeAchatService.getLignesByFournisseurId(fournisseurId);
        return ResponseEntity.ok(lignesCommande);
    }

    @GetMapping("/products")
    @Operation(summary = "Récupérer tous les produits", description = "Retourne la liste de tous les produits uniques")
    public ResponseEntity<List<String>> getAllProducts() {
        List<String> products = ligneCommandeAchatService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/best-prices/{produit}")
    @Operation(summary = "Récupérer les meilleurs prix pour un produit", description = "Retourne les lignes de commande avec les meilleurs prix pour un produit spécifique")
    public ResponseEntity<List<LigneCommandeAchat>> getBestPricesForProduct(
            @Parameter(description = "Nom du produit") @PathVariable String produit) {
        List<LigneCommandeAchat> bestPrices = ligneCommandeAchatService.getBestPricesForProduct(produit);
        return ResponseEntity.ok(bestPrices);
    }

    @GetMapping("/commande/{commandeId}/total")
    @Operation(summary = "Calculer le total d'une commande", description = "Calcule et retourne le montant total d'une commande")
    public ResponseEntity<Double> calculateTotalCommande(
            @Parameter(description = "ID de la commande") @PathVariable Long commandeId) {
        Double total = ligneCommandeAchatService.calculateTotalCommande(commandeId);
        return ResponseEntity.ok(total);
    }
}
