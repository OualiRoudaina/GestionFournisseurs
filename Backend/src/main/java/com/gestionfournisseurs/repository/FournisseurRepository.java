package com.gestionfournisseurs.repository;

import com.gestionfournisseurs.entity.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FournisseurRepository extends JpaRepository<Fournisseur, Long> {
    
    List<Fournisseur> findByNomContainingIgnoreCase(String nom);
    
    List<Fournisseur> findByQualiteServiceGreaterThan(Integer qualiteMin);
    
    @Query("SELECT f FROM Fournisseur f WHERE f.note >= :noteMin ORDER BY f.note DESC")
    List<Fournisseur> findTopSuppliersByNote(Double noteMin);
    
    @Query("SELECT AVG(f.note) FROM Fournisseur f WHERE f.note IS NOT NULL")
    Double getAverageNote();
}
