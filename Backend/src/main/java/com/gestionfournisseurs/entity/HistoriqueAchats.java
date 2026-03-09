package com.gestionfournisseurs.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

@Entity
@Table(name = "historique_achats")
public class HistoriqueAchats {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fournisseur_id", nullable = false)
    @JsonIgnore
    private Fournisseur fournisseur;
    
    @NotBlank(message = "Le nom du produit est obligatoire")
    @Column(name = "produit", nullable = false)
    private String produit;
    
    @NotNull(message = "La quantité est obligatoire")
    @Positive(message = "La quantité doit être positive")
    @Column(nullable = false)
    private Integer quantite;
    
    @NotNull(message = "Le délai de livraison est obligatoire")
    @Positive(message = "Le délai de livraison doit être positif")
    @Column(name = "delai_livraison", nullable = false)
    private Integer delaiLivraison;
    
    @Column(name = "date_achat")
    private LocalDate dateAchat;

    public HistoriqueAchats() {}

    public HistoriqueAchats(Fournisseur fournisseur, String produit, Integer quantite, Integer delaiLivraison, LocalDate dateAchat) {
        this.fournisseur = fournisseur;
        this.produit = produit;
        this.quantite = quantite;
        this.delaiLivraison = delaiLivraison;
        this.dateAchat = dateAchat;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Fournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public String getProduit() {
        return produit;
    }

    public void setProduit(String produit) {
        this.produit = produit;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public Integer getDelaiLivraison() {
        return delaiLivraison;
    }

    public void setDelaiLivraison(Integer delaiLivraison) {
        this.delaiLivraison = delaiLivraison;
    }

    public LocalDate getDateAchat() {
        return dateAchat;
    }

    public void setDateAchat(LocalDate dateAchat) {
        this.dateAchat = dateAchat;
    }
}
