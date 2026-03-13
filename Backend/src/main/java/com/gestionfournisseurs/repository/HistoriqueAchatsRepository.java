package com.gestionfournisseurs.repository;

import com.gestionfournisseurs.entity.HistoriqueAchats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HistoriqueAchatsRepository extends JpaRepository<HistoriqueAchats, Long> {
    
    List<HistoriqueAchats> findByFournisseurId(Long fournisseurId);
    
    List<HistoriqueAchats> findByProduitContainingIgnoreCase(String produit);
    
    List<HistoriqueAchats> findByDateAchatBetween(LocalDate dateDebut, LocalDate dateFin);
    
    @Query("SELECT AVG(h.delaiLivraison) FROM HistoriqueAchats h WHERE h.fournisseur.id = :fournisseurId")
    Double getAverageDelaiLivraisonByFournisseur(@Param("fournisseurId") Long fournisseurId);
    
    @Query("SELECT h FROM HistoriqueAchats h WHERE h.produit = :produit ORDER BY h.delaiLivraison ASC")
    List<HistoriqueAchats> findFastestDeliveryForProduct(@Param("produit") String produit);
    
    @Query("SELECT COUNT(h) FROM HistoriqueAchats h WHERE h.fournisseur.id = :fournisseurId")
    Long getTotalAchatsByFournisseur(@Param("fournisseurId") Long fournisseurId);
}
