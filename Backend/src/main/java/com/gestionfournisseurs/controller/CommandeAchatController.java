package com.gestionfournisseurs.controller;

import com.gestionfournisseurs.entity.CommandeAchat;
import com.gestionfournisseurs.service.CommandeAchatService;
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
@RequestMapping("/api/commandes")
@Tag(name = "Commandes d'achat", description = "API pour la gestion des commandes d'achat")
public class CommandeAchatController {

    @Autowired
    private CommandeAchatService commandeAchatService;

    @GetMapping
    @Operation(summary = "Récupérer toutes les commandes", description = "Retourne la liste de toutes les commandes d'achat")
    public ResponseEntity<List<CommandeAchat>> getAllCommandes() {
        List<CommandeAchat> commandes = commandeAchatService.getAllCommandes();
        return ResponseEntity.ok(commandes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une commande par son ID", description = "Retourne une commande spécifique basée sur son ID")
    public ResponseEntity<CommandeAchat> getCommandeById(
            @Parameter(description = "ID de la commande à récupérer") @PathVariable Long id) {
        return commandeAchatService.getCommandeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Créer une nouvelle commande", description = "Crée et retourne une nouvelle commande d'achat")
    public ResponseEntity<CommandeAchat> createCommande(
            @Parameter(description = "Commande à créer") @Valid @RequestBody CommandeAchat commande) {
        CommandeAchat createdCommande = commandeAchatService.saveCommande(commande);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCommande);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une commande", description = "Met à jour et retourne une commande existante")
    public ResponseEntity<CommandeAchat> updateCommande(
            @Parameter(description = "ID de la commande à mettre à jour") @PathVariable Long id,
            @Parameter(description = "Détails de la commande mise à jour") @Valid @RequestBody CommandeAchat commandeDetails) {
        CommandeAchat updatedCommande = commandeAchatService.updateCommande(id, commandeDetails);
        return updatedCommande != null ? ResponseEntity.ok(updatedCommande) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une commande", description = "Supprime une commande basée sur son ID")
    public ResponseEntity<Void> deleteCommande(
            @Parameter(description = "ID de la commande à supprimer") @PathVariable Long id) {
        commandeAchatService.deleteCommande(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/fournisseur/{fournisseurId}")
    @Operation(summary = "Récupérer les commandes par fournisseur", description = "Retourne toutes les commandes d'un fournisseur spécifique")
    public ResponseEntity<List<CommandeAchat>> getCommandesByFournisseur(
            @Parameter(description = "ID du fournisseur") @PathVariable Long fournisseurId) {
        List<CommandeAchat> commandes = commandeAchatService.getCommandesByFournisseurId(fournisseurId);
        return ResponseEntity.ok(commandes);
    }

    @GetMapping("/statut/{statut}")
    @Operation(summary = "Récupérer les commandes par statut", description = "Retourne toutes les commandes avec un statut spécifique")
    public ResponseEntity<List<CommandeAchat>> getCommandesByStatut(
            @Parameter(description = "Statut de la commande") @PathVariable String statut) {
        List<CommandeAchat> commandes = commandeAchatService.getCommandesByStatut(statut);
        return ResponseEntity.ok(commandes);
    }

    @GetMapping("/date-range")
    @Operation(summary = "Récupérer les commandes par plage de dates", description = "Retourne les commandes dans une plage de dates spécifique")
    public ResponseEntity<List<CommandeAchat>> getCommandesByDateRange(
            @Parameter(description = "Date de début") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @Parameter(description = "Date de fin") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        List<CommandeAchat> commandes = commandeAchatService.getCommandesByDateRange(dateDebut, dateFin);
        return ResponseEntity.ok(commandes);
    }

    @PutMapping("/{id}/statut")
    @Operation(summary = "Mettre à jour le statut d'une commande", description = "Met à jour uniquement le statut d'une commande")
    public ResponseEntity<CommandeAchat> updateStatut(
            @Parameter(description = "ID de la commande") @PathVariable Long id,
            @Parameter(description = "Nouveau statut") @RequestParam String nouveauStatut) {
        CommandeAchat updatedCommande = commandeAchatService.updateStatut(id, nouveauStatut);
        return updatedCommande != null ? ResponseEntity.ok(updatedCommande) : ResponseEntity.notFound().build();
    }
}
