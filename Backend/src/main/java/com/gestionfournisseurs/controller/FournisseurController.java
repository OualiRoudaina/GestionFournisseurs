package com.gestionfournisseurs.controller;

import com.gestionfournisseurs.entity.Fournisseur;
import com.gestionfournisseurs.service.FournisseurService;
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
@RequestMapping("/api/fournisseurs")
@Tag(name = "Fournisseurs", description = "API pour la gestion des fournisseurs")
public class FournisseurController {

    @Autowired
    private FournisseurService fournisseurService;

    @GetMapping
    @Operation(summary = "Récupérer tous les fournisseurs", description = "Retourne la liste de tous les fournisseurs")
    public ResponseEntity<List<Fournisseur>> getAllFournisseurs() {
        List<Fournisseur> fournisseurs = fournisseurService.getAllFournisseurs();
        return ResponseEntity.ok(fournisseurs);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un fournisseur par son ID", description = "Retourne un fournisseur spécifique basé sur son ID")
    public ResponseEntity<Fournisseur> getFournisseurById(
            @Parameter(description = "ID du fournisseur à récupérer") @PathVariable Long id) {
        return fournisseurService.getFournisseurById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Créer un nouveau fournisseur", description = "Crée et retourne un nouveau fournisseur")
    public ResponseEntity<Fournisseur> createFournisseur(
            @Parameter(description = "Fournisseur à créer") @Valid @RequestBody Fournisseur fournisseur) {
        Fournisseur createdFournisseur = fournisseurService.saveFournisseur(fournisseur);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFournisseur);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un fournisseur", description = "Met à jour et retourne un fournisseur existant")
    public ResponseEntity<Fournisseur> updateFournisseur(
            @Parameter(description = "ID du fournisseur à mettre à jour") @PathVariable Long id,
            @Parameter(description = "Détails du fournisseur mis à jour") @Valid @RequestBody Fournisseur fournisseurDetails) {
        Fournisseur updatedFournisseur = fournisseurService.updateFournisseur(id, fournisseurDetails);
        return updatedFournisseur != null ? ResponseEntity.ok(updatedFournisseur) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un fournisseur", description = "Supprime un fournisseur basé sur son ID")
    public ResponseEntity<Void> deleteFournisseur(
            @Parameter(description = "ID du fournisseur à supprimer") @PathVariable Long id) {
        fournisseurService.deleteFournisseur(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher des fournisseurs", description = "Recherche des fournisseurs par nom")
    public ResponseEntity<List<Fournisseur>> searchFournisseurs(
            @Parameter(description = "Terme de recherche pour le nom du fournisseur") @RequestParam String nom) {
        List<Fournisseur> fournisseurs = fournisseurService.searchFournisseurs(nom);
        return ResponseEntity.ok(fournisseurs);
    }

    @GetMapping("/top")
    @Operation(summary = "Récupérer les meilleurs fournisseurs", description = "Retourne les fournisseurs avec les meilleures notes")
    public ResponseEntity<List<Fournisseur>> getTopSuppliers() {
        List<Fournisseur> topSuppliers = fournisseurService.getTopSuppliers();
        return ResponseEntity.ok(topSuppliers);
    }

    @GetMapping("/stats/average-note")
    @Operation(summary = "Calculer la note moyenne", description = "Retourne la note moyenne de tous les fournisseurs")
    public ResponseEntity<Double> getAverageNote() {
        Double averageNote = fournisseurService.getAverageNote();
        return ResponseEntity.ok(averageNote);
    }

    @PostMapping("/{id}/evaluate")
    @Operation(summary = "Évaluer un fournisseur", description = "Évalue automatiquement un fournisseur basé sur ses performances")
    public ResponseEntity<Fournisseur> evaluateSupplier(
            @Parameter(description = "ID du fournisseur à évaluer") @PathVariable Long id) {
        Fournisseur evaluatedFournisseur = fournisseurService.evaluateSupplier(id);
        return evaluatedFournisseur != null ? ResponseEntity.ok(evaluatedFournisseur) : ResponseEntity.notFound().build();
    }
}
