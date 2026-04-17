package com.gestionfournisseurs.repository;

import com.gestionfournisseurs.entity.LigneCommandeAchat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LigneCommandeAchatRepository extends JpaRepository<LigneCommandeAchat, Long> {
    
    List<LigneCommandeAchat> findByCommandeId(Long commandeId);
    
    List<LigneCommandeAchat> findByProduitContainingIgnoreCase(String produit);
    
    @Query("SELECT l FROM LigneCommandeAchat l WHERE l.commande.fournisseur.id = :fournisseurId")
    List<LigneCommandeAchat> findByFournisseurId(@Param("fournisseurId") Long fournisseurId);
    
    @Query("SELECT DISTINCT l.produit FROM LigneCommandeAchat l")
    List<String> findAllProducts();
    
    @Query("SELECT l FROM LigneCommandeAchat l WHERE l.produit = :produit ORDER BY l.prixUnitaire ASC")
    List<LigneCommandeAchat> findBestPricesForProduct(@Param("produit") String produit);
}
