package com.gestionfournisseurs.dto;    

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public class CommandeAchatDTO {
    
    private Long id;
    private Long fournisseurId;
    private String fournisseurNom;
    
    @NotNull(message = "La date de commande est obligatoire")
    private LocalDate date;
    
    @NotBlank(message = "Le statut est obligatoire")
    private String statut;
    
    @NotNull(message = "Le montant est obligatoire")
    @Positive(message = "Le montant doit être positif")
    private Double montant;

    public CommandeAchatDTO() {}

    public CommandeAchatDTO(Long fournisseurId, LocalDate date, String statut, Double montant) {
        this.fournisseurId = fournisseurId;
        this.date = date;
        this.statut = statut;
        this.montant = montant;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }


}
