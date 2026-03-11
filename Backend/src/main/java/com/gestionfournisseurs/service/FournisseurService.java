package com.gestionfournisseurs.service;

import com.gestionfournisseurs.entity.Fournisseur;
import com.gestionfournisseurs.repository.FournisseurRepository;
import com.gestionfournisseurs.repository.CommandeAchatRepository;
import com.gestionfournisseurs.repository.HistoriqueAchatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FournisseurService {

    @Autowired
    private FournisseurRepository fournisseurRepository;
    
    @Autowired
    private CommandeAchatRepository commandeAchatRepository;
    
    @Autowired
    private HistoriqueAchatsRepository historiqueAchatsRepository;

    public List<Fournisseur> getAllFournisseurs() {
        return fournisseurRepository.findAll();
    }

    public Optional<Fournisseur> getFournisseurById(Long id) {
        return fournisseurRepository.findById(id);
    }

    public Fournisseur saveFournisseur(Fournisseur fournisseur) {
        return fournisseurRepository.save(fournisseur);
    }

    public Fournisseur updateFournisseur(Long id, Fournisseur fournisseurDetails) {
        return fournisseurRepository.findById(id)
            .map(fournisseur -> {
                fournisseur.setNom(fournisseurDetails.getNom());
                fournisseur.setContact(fournisseurDetails.getContact());
                fournisseur.setQualiteService(fournisseurDetails.getQualiteService());
                fournisseur.setNote(fournisseurDetails.getNote());
                return fournisseurRepository.save(fournisseur);
            })
            .orElse(null);
    }

    public void deleteFournisseur(Long id) {
        fournisseurRepository.deleteById(id);
    }

    public List<Fournisseur> searchFournisseurs(String nom) {
        return fournisseurRepository.findByNomContainingIgnoreCase(nom);
    }

    public List<Fournisseur> getTopSuppliers() {
        return fournisseurRepository.findTopSuppliersByNote(3.0);
    }

    public Double getAverageNote() {
        return fournisseurRepository.getAverageNote();
    }

    public Fournisseur evaluateSupplier(Long id) {
        return fournisseurRepository.findById(id)
            .map(fournisseur -> {
                Double totalMontant = commandeAchatRepository.getTotalMontantByFournisseur(id);
                Long totalAchats = historiqueAchatsRepository.getTotalAchatsByFournisseur(id);
                Double avgDelai = historiqueAchatsRepository.getAverageDelaiLivraisonByFournisseur(id);
                
                double evaluationScore = 0.0;
                
                if (totalMontant != null && totalMontant > 1000) {
                    evaluationScore += 2;
                }
                
                if (totalAchats != null && totalAchats > 5) {
                    evaluationScore += 2;
                }
                
                if (avgDelai != null && avgDelai < 7) {
                    evaluationScore += 3;
                } else if (avgDelai != null && avgDelai < 14) {
                    evaluationScore += 2;
                } else if (avgDelai != null && avgDelai < 30) {
                    evaluationScore += 1;
                }
                
                if (fournisseur.getQualiteService() != null) {
                    evaluationScore += fournisseur.getQualiteService();
                }
                
                fournisseur.setNote(Math.min(evaluationScore, 10.0));
                return fournisseurRepository.save(fournisseur);
            })
            .orElse(null);
    }
}
