package com.gestionfournisseurs.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class FournisseurDTO {
    
    private Long id;
    
    @NotBlank(message = "Le nom du fournisseur est obligatoire")
    @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères")
    private String nom;
    
    @Email(message = "L'email doit être valide")
    @Size(max = 100, message = "L'email ne doit pas dépasser 100 caractères")
    private String contact;
    
    private Integer qualiteService;
    private Double note;

    public FournisseurDTO() {}

    public FournisseurDTO(String nom, String contact, Integer qualiteService, Double note) {
        this.nom = nom;
        this.contact = contact;
        this.qualiteService = qualiteService;
        this.note = note;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Integer getQualiteService() {
        return qualiteService;
    }

    public void setQualiteService(Integer qualiteService) {
        this.qualiteService = qualiteService;
    }

    public Double getNote() {
        return note;
    }

    public void setNote(Double note) {
        this.note = note;
    }
}
