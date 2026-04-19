package com.gestionfournisseurs.controller;

import com.gestionfournisseurs.entity.LigneCommandeAchat;
import com.gestionfournisseurs.entity.HistoriqueAchats;
import com.gestionfournisseurs.service.ComparaisonOffresService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comparaison-offres")
@Tag(name = "Comparaison des offres", description = "API pour la comparaison des offres fournisseurs")
public class ComparaisonOffresController {

    @Autowired
    private ComparaisonOffresService comparaisonOffresService;

    @GetMapping("/produit/{produit}/prix")
    @Operation(summary = "Comparer les prix d'un produit", description = "Retourne les différentes offres de prix pour un produit spécifique")
    public ResponseEntity<List<LigneCommandeAchat>> comparerPrixProduit(
            @Parameter(description = "Nom du produit à comparer") @PathVariable String produit) {
        List<LigneCommandeAchat> offres = comparaisonOffresService.comparerPrixProduit(produit);
        return ResponseEntity.ok(offres);
    }

    @GetMapping("/produit/{produit}/livraison")
    @Operation(summary = "Comparer les délais de livraison d'un produit", description = "Retourne les différents délais de livraison pour un produit spécifique")
    public ResponseEntity<List<HistoriqueAchats>> comparerDelaisLivraison(
            @Parameter(description = "Nom du produit à comparer") @PathVariable String produit) {
        List<HistoriqueAchats> delais = comparaisonOffresService.comparerDelaisLivraisonProduit(produit);
        return ResponseEntity.ok(delais);
    }

    @GetMapping("/produit/{produit}/complet")
    @Operation(summary = "Comparaison complète des offres pour un produit", description = "Retourne une comparaison complète incluant prix et délais de livraison pour un produit")
    public ResponseEntity<Map<String, Object>> comparaisonCompleteProduit(
            @Parameter(description = "Nom du produit à comparer") @PathVariable String produit) {
        Map<String, Object> comparaison = comparaisonOffresService.comparaisonCompleteProduit(produit);
        return ResponseEntity.ok(comparaison);
    }

    @GetMapping("/fournisseurs/{produit}")
    @Operation(summary = "Comparer les fournisseurs pour un produit", description = "Retourne tous les fournisseurs qui proposent un produit spécifique avec leurs offres")
    public ResponseEntity<Map<String, Object>> comparerFournisseursPourProduit(
            @Parameter(description = "Nom du produit") @PathVariable String produit) {
        Map<String, Object> comparaisonFournisseurs = comparaisonOffresService.comparerFournisseursPourProduit(produit);
        return ResponseEntity.ok(comparaisonFournisseurs);
    }

    @GetMapping("/scores/{produit}")
    @Operation(summary = "Calculer les scores globaux des fournisseurs", description = "Retourne les scores globaux des fournisseurs pour un produit basé sur prix, délai et qualité")
    public ResponseEntity<List<Map<String, Object>>> calculerScoreGlobalProduit(
            @Parameter(description = "Nom du produit") @PathVariable String produit) {
        List<Map<String, Object>> scores = comparaisonOffresService.calculerScoreGlobalParProduit(produit);
        return ResponseEntity.ok(scores);
    }

    @GetMapping("/produits")
    @Operation(summary = "Lister tous les produits disponibles", description = "Retourne la liste de tous les produits disponibles pour la comparaison")
    public ResponseEntity<List<String>> getProduitsDisponibles() {
        List<String> produits = comparaisonOffresService.getProduitsDisponibles();
        return ResponseEntity.ok(produits);
    }
}
