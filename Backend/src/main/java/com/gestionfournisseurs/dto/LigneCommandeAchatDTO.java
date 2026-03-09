package com.gestionfournisseurs.dto;  

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class LigneCommandeAchatDTO {
    
    private Long id;
    private Long commandeAchatId;
    
    @NotNull(message = "L'identifiant du produit est obligatoire")
    private Long produitId;
    
    private String produitNom;
    
    @NotNull(message = "La quantité est obligatoire")
    @Positive(message = "La quantité doit être positive")
    private Integer quantite;
    
    @NotNull(message = "Le prix unitaire est obligatoire")
    @Positive(message = "Le prix unitaire doit être positif")
    private Double prixUnitaire;
    
    private Double prixTotal;

    public LigneCommandeAchatDTO() {}

    public LigneCommandeAchatDTO(Long commandeAchatId, Long produitId, String produitNom, Integer quantite, Double prixUnitaire) {
        this.commandeAchatId = commandeAchatId;
        this.produitId = produitId;
        this.produitNom = produitNom;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.prixTotal = quantite * prixUnitaire;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCommandeAchatId() {
        return commandeAchatId;
    }

    public void setCommandeAchatId(Long commandeAchatId) {
        this.commandeAchatId = commandeAchatId;
    }

    public Long getProduitId() {
        return produitId;
    }

    public void setProduitId(Long produitId) {
        this.produitId = produitId;
    }

    public String getProduitNom() {
        return produitNom;
    }

    public void setProduitNom(String produitNom) {
        this.produitNom = produitNom;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
        this.prixTotal = this.quantite * this.prixUnitaire;
    }

    public Double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(Double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
        this.prixTotal = this.quantite * this.prixUnitaire;
    }

    public Double getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(Double prixTotal) {
        this.prixTotal = prixTotal;
    }
}
