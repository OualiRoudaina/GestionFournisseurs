package com.gestionfournisseurs.service;

import com.gestionfournisseurs.entity.LigneCommandeAchat;
import com.gestionfournisseurs.entity.CommandeAchat;
import com.gestionfournisseurs.repository.CommandeAchatRepository;
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

    @Autowired
    private CommandeAchatRepository commandeAchatRepository;

    public List<LigneCommandeAchat> getAllLignesCommande() {
        return ligneCommandeAchatRepository.findAll();
    }

    public Optional<LigneCommandeAchat> getLigneCommandeById(Long id) {
        return ligneCommandeAchatRepository.findById(id);
    }

    public LigneCommandeAchat saveLigneCommande(LigneCommandeAchat ligneCommande) {
        ligneCommande.setCommande(requireManagedCommande(ligneCommande.getCommande()));
        return ligneCommandeAchatRepository.save(ligneCommande);
    }

    public LigneCommandeAchat updateLigneCommande(Long id, LigneCommandeAchat ligneCommandeDetails) {
        return ligneCommandeAchatRepository.findById(id)
            .map(ligneCommande -> {
                if (ligneCommandeDetails.getCommande() != null && ligneCommandeDetails.getCommande().getId() != null) {
                    ligneCommande.setCommande(requireManagedCommande(ligneCommandeDetails.getCommande()));
                }
                ligneCommande.setProduit(ligneCommandeDetails.getProduit());
                ligneCommande.setQuantite(ligneCommandeDetails.getQuantite());
                ligneCommande.setPrixUnitaire(ligneCommandeDetails.getPrixUnitaire());
                return ligneCommandeAchatRepository.save(ligneCommande);
            })
            .orElse(null);
    }

    private CommandeAchat requireManagedCommande(CommandeAchat ref) {
        if (ref == null || ref.getId() == null) {
            throw new IllegalArgumentException("L'identifiant de la commande est obligatoire");
        }
        return commandeAchatRepository.findById(ref.getId())
            .orElseThrow(() -> new IllegalArgumentException("Commande introuvable"));
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
