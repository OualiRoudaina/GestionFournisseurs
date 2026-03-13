package com.gestionfournisseurs.repository;

import com.gestionfournisseurs.entity.CommandeAchat;
import com.gestionfournisseurs.entity.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CommandeAchatRepository extends JpaRepository<CommandeAchat, Long> {
    
    List<CommandeAchat> findByFournisseur(Fournisseur fournisseur);
    
    List<CommandeAchat> findByFournisseurId(Long fournisseurId);
    
    List<CommandeAchat> findByStatut(String statut);
    
    List<CommandeAchat> findByDateBetween(LocalDate dateDebut, LocalDate dateFin);
    
    List<CommandeAchat> findByFournisseurAndStatut(Fournisseur fournisseur, String statut);
    
    @Query("SELECT c FROM CommandeAchat c WHERE c.fournisseur.id = :fournisseurId AND c.date >= :dateDebut")
    List<CommandeAchat> findRecentCommandesByFournisseur(@Param("fournisseurId") Long fournisseurId, 
                                                        @Param("dateDebut") LocalDate dateDebut);
    
    @Query("SELECT SUM(c.montant) FROM CommandeAchat c WHERE c.fournisseur.id = :fournisseurId")
    Double getTotalMontantByFournisseur(@Param("fournisseurId") Long fournisseurId);
}
