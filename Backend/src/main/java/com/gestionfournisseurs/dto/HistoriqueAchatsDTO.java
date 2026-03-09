package com.gestionfournisseurs.dto;

import java.time.LocalDate;
import java.util.List;

public class HistoriqueAchatsDTO {
    
    private Long id;
    private Long fournisseurId;
    private String fournisseurNom;
    
    private LocalDate dateCommande;
    
    private String statut;
    
    private Double montantTotal;
    
    private List<LigneCommandeAchatDTO> lignesCommande;
    
    private String utilisateurCreation;
    private LocalDate dateCreation;

    public HistoriqueAchatsDTO() {}

    public HistoriqueAchatsDTO(Long fournisseurId, String fournisseurNom, LocalDate dateCommande, String statut, Double montantTotal) {
        this.fournisseurId = fournisseurId;
        this.fournisseurNom = fournisseurNom;
        this.dateCommande = dateCommande;
        this.statut = statut;
        this.montantTotal = montantTotal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFournisseurId() {
        return fournisseurId;
    }

    public void setFournisseurId(Long fournisseurId) {
        this.fournisseurId = fournisseurId;
    }

    public String getFournisseurNom() {
        return fournisseurNom;
    }

    public void setFournisseurNom(String fournisseurNom) {
        this.fournisseurNom = fournisseurNom;
    }

    public LocalDate getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(LocalDate dateCommande) {
        this.dateCommande = dateCommande;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(Double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public List<LigneCommandeAchatDTO> getLignesCommande() {
        return lignesCommande;
    }

    public void setLignesCommande(List<LigneCommandeAchatDTO> lignesCommande) {
        this.lignesCommande = lignesCommande;
    }

    public String getUtilisateurCreation() {
        return utilisateurCreation;
    }

    public void setUtilisateurCreation(String utilisateurCreation) {
        this.utilisateurCreation = utilisateurCreation;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }
}
