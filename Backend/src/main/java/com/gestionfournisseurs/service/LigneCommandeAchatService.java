package com.gestionfournisseurs.service;

import com.gestionfournisseurs.entity.LigneCommandeAchat;
import com.gestionfournisseurs.entity.CommandeAchat;
import com.gestionfournisseurs.repository.LigneCommandeAchatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LigneCommandeAchatService {

    @Autowired
    private LigneCommandeAchatRepository ligneCommandeAchatRepository;

    public List<LigneCommandeAchat> getAllLignesCommande() {
        return ligneCommandeAchatRepository.findAll();
    }

    public Optional<LigneCommandeAchat> getLigneCommandeById(Long id) {
        return ligneCommandeAchatRepository.findById(id);
    }

    public LigneCommandeAchat saveLigneCommande(LigneCommandeAchat ligneCommande) {
        return ligneCommandeAchatRepository.save(ligneCommande);
    }

    public LigneCommandeAchat updateLigneCommande(Long id, LigneCommandeAchat ligneCommandeDetails) {
        return ligneCommandeAchatRepository.findById(id)
            .map(ligneCommande -> {
                ligneCommande.setCommande(ligneCommandeDetails.getCommande());
                ligneCommande.setProduit(ligneCommandeDetails.getProduit());
                ligneCommande.setQuantite(ligneCommandeDetails.getQuantite());
                ligneCommande.setPrixUnitaire(ligneCommandeDetails.getPrixUnitaire());
                return ligneCommandeAchatRepository.save(ligneCommande);
            })
            .orElse(null);
    }

    public void deleteLigneCommande(Long id) {
        ligneCommandeAchatRepository.deleteById(id);
    }

    public List<LigneCommandeAchat> getLignesByCommandeId(Long commandeId) {
        return ligneCommandeAchatRepository.findByCommandeId(commandeId);
    }

    public List<LigneCommandeAchat> getLignesByProduit(String produit) {
        return ligneCommandeAchatRepository.findByProduitContainingIgnoreCase(produit);
    }

    public List<LigneCommandeAchat> getLignesByFournisseurId(Long fournisseurId) {
        return ligneCommandeAchatRepository.findByFournisseurId(fournisseurId);
    }

    public List<String> getAllProducts() {
        return ligneCommandeAchatRepository.findAllProducts();
    }

    public List<LigneCommandeAchat> getBestPricesForProduct(String produit) {
        return ligneCommandeAchatRepository.findBestPricesForProduct(produit);
    }

    public Double calculateTotalCommande(Long commandeId) {
        List<LigneCommandeAchat> lignes = getLignesByCommandeId(commandeId);
        return lignes.stream()
            .mapToDouble(LigneCommandeAchat::getTotal)
            .sum();
    }
}
