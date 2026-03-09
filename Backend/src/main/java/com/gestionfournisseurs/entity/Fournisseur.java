package com.gestionfournisseurs.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "fournisseurs")
public class Fournisseur {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Le nom du fournisseur est obligatoire")
    @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères")
    @Column(nullable = false)
    private String nom;
    
    @Email(message = "L'email doit être valide")
    @Size(max = 100, message = "L'email ne doit pas dépasser 100 caractères")
    private String contact;
    
    @Column(name = "qualite_service")
    private Integer qualiteService;
    
    private Double note;
    
    @OneToMany(mappedBy = "fournisseur", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CommandeAchat> commandes;
    
    @OneToMany(mappedBy = "fournisseur", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<HistoriqueAchats> historiqueAchats;

    public Fournisseur() {}

    public Fournisseur(String nom, String contact, Integer qualiteService, Double note) {
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

    public List<CommandeAchat> getCommandes() {
        return commandes;
    }

    public void setCommandes(List<CommandeAchat> commandes) {
        this.commandes = commandes;
    }

    public List<HistoriqueAchats> getHistoriqueAchats() {
        return historiqueAchats;
    }

    public void setHistoriqueAchats(List<HistoriqueAchats> historiqueAchats) {
        this.historiqueAchats = historiqueAchats;
    }
}
