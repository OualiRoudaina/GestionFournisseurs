package com.gestionfournisseurs.repository;

import com.gestionfournisseurs.entity.CommandeAchat;
import com.gestionfournisseurs.entity.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommandeAchatRepository extends JpaRepository<CommandeAchat, Long> {

    @Query("SELECT DISTINCT c FROM CommandeAchat c LEFT JOIN FETCH c.fournisseur")
    List<CommandeAchat> findAllWithFournisseur();

    @Query("SELECT DISTINCT c FROM CommandeAchat c LEFT JOIN FETCH c.fournisseur WHERE c.id = :id")
    Optional<CommandeAchat> findByIdWithFournisseur(@Param("id") Long id);

    @Query("SELECT DISTINCT c FROM CommandeAchat c LEFT JOIN FETCH c.fournisseur WHERE c.fournisseur = :fournisseur")
    List<CommandeAchat> findByFournisseur(@Param("fournisseur") Fournisseur fournisseur);

    @Query("SELECT DISTINCT c FROM CommandeAchat c LEFT JOIN FETCH c.fournisseur WHERE c.fournisseur.id = :fournisseurId")
    List<CommandeAchat> findByFournisseurId(@Param("fournisseurId") Long fournisseurId);

    @Query("SELECT DISTINCT c FROM CommandeAchat c LEFT JOIN FETCH c.fournisseur WHERE c.statut = :statut")
    List<CommandeAchat> findByStatut(@Param("statut") String statut);

    @Query("SELECT DISTINCT c FROM CommandeAchat c LEFT JOIN FETCH c.fournisseur WHERE c.date BETWEEN :dateDebut AND :dateFin")
    List<CommandeAchat> findByDateBetween(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin);

    @Query("SELECT DISTINCT c FROM CommandeAchat c LEFT JOIN FETCH c.fournisseur WHERE c.fournisseur = :fournisseur AND c.statut = :statut")
    List<CommandeAchat> findByFournisseurAndStatut(@Param("fournisseur") Fournisseur fournisseur, @Param("statut") String statut);

    @Query("SELECT DISTINCT c FROM CommandeAchat c LEFT JOIN FETCH c.fournisseur WHERE c.fournisseur.id = :fournisseurId AND c.date >= :dateDebut")
    List<CommandeAchat> findRecentCommandesByFournisseur(@Param("fournisseurId") Long fournisseurId,
                                                          @Param("dateDebut") LocalDate dateDebut);

    @Query("SELECT SUM(c.montant) FROM CommandeAchat c WHERE c.fournisseur.id = :fournisseurId")
    Double getTotalMontantByFournisseur(@Param("fournisseurId") Long fournisseurId);
}
